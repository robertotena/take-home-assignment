package com.marionete.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marionete.model.User;
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
public class UserService {

    public static final String USERS_URI = "/marionete/user/";

    private final WebClient userWebClient;

    public UserService(@Qualifier("userWebClient") WebClient userWebClient) {
        this.userWebClient = userWebClient;
    }

    public Mono<User> getUser(final String token) {
        return userWebClient
                .get()
                .uri(USERS_URI)
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(httpStatus -> HttpStatus.TOO_MANY_REQUESTS.equals(httpStatus), response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new ResponseStatusException(response.statusCode())))
                .bodyToMono(String.class)
                .map(s -> {
                    try {
                        return new ObjectMapper().readValue(s, User.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
                        .filter(throwable -> throwable instanceof ResponseStatusException));
    }

}
