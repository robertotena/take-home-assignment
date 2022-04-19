package com.marionete;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.marionete.model.Account;
import com.marionete.model.User;
import com.marionete.model.UserAccount;
import com.marionete.model.UserCredentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.marionete.ApplicationTest.BACKENDS_PORT;
import static com.marionete.services.AccountService.ACCOUNTS_URI;
import static com.marionete.services.UserService.USERS_URI;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        properties = {
                "user-account-service.user-info-api.base-url=http://localhost:" + BACKENDS_PORT,
                "user-account-service.account-info-api.base-url=http://localhost:" + BACKENDS_PORT,
                "login-service.host=localhost",
                "login-service.port=8900"
        },
        classes = {Application.class}
)
@AutoConfigureMockMvc
public class ApplicationTest {

    public static final int BACKENDS_PORT = 8888;

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(BACKENDS_PORT))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebTestClient webTestClient;

    private final User user = new User("name", "surname", "sex", 1);
    private final Account account = new Account("account");
    private final UserAccount expectedResult = new UserAccount(user, account);
    private final UserCredentials userCredentials = new UserCredentials("user", "pass");

    @Test
    public void testApplicationShouldReturnUserAccount() throws Exception {

        stubSuccessfulAccount();
        stubSuccessfulUser();

        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userCredentials)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedResult, mvcResult.getAsyncResult(2000));
    }

    @Test
    public void testApplicationShouldRetryIfServiceUnavailable() throws Exception {
        stubSuccessfulUser();
        stubUnavailableAccountWithTooManyRequests();
        stubSuccessfulAccount();

        final MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post("/marionete/useraccount")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(userCredentials)))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(expectedResult, mvcResult.getAsyncResult(10000));
    }

    @Test
    public void testApplicationShouldFailWhenSurpassMaxRetries() throws Exception {
        stubSuccessfulUser();
        stubUnavailableAccountWithTooManyRequests();
        stubUnavailableAccountWithTooManyRequests();
        stubUnavailableAccountWithTooManyRequests();

        webTestClient.post()
                .uri("/marionete/useraccount")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(objectMapper.writeValueAsString(userCredentials))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    private void stubSuccessfulAccount() throws JsonProcessingException {
        wireMockExtension.stubFor(get(urlEqualTo(ACCOUNTS_URI))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(account))));
    }

    private void stubUnavailableAccountWithTooManyRequests() {
        wireMockExtension.stubFor(get(urlEqualTo(ACCOUNTS_URI))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.TOO_MANY_REQUESTS.value())));
    }

    private void stubSuccessfulUser() throws JsonProcessingException {
        wireMockExtension.stubFor(get(urlEqualTo(USERS_URI))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(objectMapper.writeValueAsString(user))));
    }
}
