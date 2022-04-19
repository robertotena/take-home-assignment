package com.marionete.services;

import com.marionete.model.Account;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.marionete.services.AccountService.ACCOUNTS_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import static org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

public class AccountServiceTest {

    @Test
    public void testGetAccountReturnsAccount() {
        WebClient webClient = mock(WebClient.class);
        RequestHeadersUriSpec uriSpec = mock(RequestHeadersUriSpec.class);
        ResponseSpec responseSpec = mock(ResponseSpec.class);

        AccountService service = new AccountService(webClient);
        String token = "token";
        Account account = new Account("account");

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(ACCOUNTS_URI)).thenReturn(uriSpec);
        when(uriSpec.header(HttpHeaders.AUTHORIZATION, token)).thenReturn(uriSpec);
        when(uriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("{ \"accountNumber\":\"account\"}"));

        Mono<Account> result = service.getAccount(token);

        assertEquals(account, result.block());
        verify(webClient, times(1)).get();
        verify(uriSpec, times(1)).uri(ACCOUNTS_URI);
        verify(uriSpec, times(1)).header(HttpHeaders.AUTHORIZATION, token);
        verify(uriSpec, times(1)).retrieve();
        verify(responseSpec, times(2)).onStatus(any(), any());
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

}
