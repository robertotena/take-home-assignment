package com.marionete.config;

import com.marionete.grpc.LoginClient;
import com.marionete.grpc.LoginServer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Configuration
public class UserAccountConfig {

    @Value("${user-account-service.account-info-api.base-url}")
    private String accountInfoApiBaseUrl;

    @Value("${user-account-service.user-info-api.base-url}")
    private String userInfoApiBaseUrl;

    @Value("${login-service.host}")
    private String loginServerHost;

    @Value("${login-service.port}")
    private Integer loginServerPort;

    @Bean
    @Qualifier("accountWebClient")
    public WebClient getAccountInfoWebClient() {
        return WebClient.builder().baseUrl(accountInfoApiBaseUrl).build();
    }

    @Bean
    @Qualifier("userWebClient")
    public WebClient getUserInfoWebClient() {
        return WebClient.builder().baseUrl(userInfoApiBaseUrl).build();
    }

    @Bean
    public LoginServer loginServer() throws IOException {
        final LoginServer loginServer = new LoginServer(loginServerPort);
        loginServer.start();
        return loginServer;
    }

    @Bean
    public LoginClient getLoginClient() {
        return new LoginClient(loginServerHost, loginServerPort);
    }
}
