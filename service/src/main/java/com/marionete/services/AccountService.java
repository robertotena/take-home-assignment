package com.marionete.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.model.Account;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class AccountService {

    public static final String ACCOUNTS_URI = "/marionete/account/";

    private final WebClient accountWebClient;

    public AccountService(@Qualifier("accountWebClient") WebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    public Mono<Account> getAccount(final String token) {
        return accountWebClient
                .get()
                .uri(ACCOUNTS_URI)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.TOO_MANY_REQUESTS.equals(httpStatus), response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .bodyToMono(String.class)
                .map(s -> {
                    try {
                        return new ObjectMapper().readValue(s, Account.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof ResponseStatusException));
    }

}
