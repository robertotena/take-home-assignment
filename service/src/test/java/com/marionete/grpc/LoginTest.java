package com.marionete.grpc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoginTest {

    private LoginServer loginServer;
    private LoginClient loginClient;

    @BeforeEach
    public void setUp() throws IOException {
        int port = 8999;
        loginServer = new LoginServer(port);
        loginServer.start();
        loginClient = new LoginClient("localhost", port);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        loginServer.shutdown();
    }

    @Test
    public void testLoginGrpcService() {
        String login = loginClient.login("username", "password");
        Assertions.assertNotNull(login);
    }

}
