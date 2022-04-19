package com.marionete.services;

import com.marionete.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.marionete.services.UserService.USERS_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import static org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

public class UserServiceTest {

    @Test
    public void testGetUserReturnsAccount() {
        WebClient webClient = mock(WebClient.class);
        RequestHeadersUriSpec uriSpec = mock(RequestHeadersUriSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        UserService service = new UserService(webClient);
        String token = "token";
        User user = new User("name", "surname", "sex", 1);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(USERS_URI)).thenReturn(uriSpec);
        when(uriSpec.header(HttpHeaders.AUTHORIZATION, token)).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{ \"name\":\"name\", \"surname\":\"surname\", \"sex\":\"sex\", \"age\": 1}"));

        Mono<User> result = service.getUser(token);

        assertEquals(user, result.block());
        verify(webClient, times(1)).get();
        verify(uriSpec, times(1)).uri(USERS_URI);
        verify(uriSpec, times(1)).header(HttpHeaders.AUTHORIZATION, token);
        verify(uriSpec, times(1)).retrieve();
        verify(responseSpec, times(2)).onStatus(any(), any());
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

}
