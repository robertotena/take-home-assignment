package com.marionete.grpc;

import io.grpc.ManagedChannelBuilder;
import services.LoginRequest;
import services.LoginResponse;
import services.LoginServiceGrpc;
import services.LoginServiceGrpc.LoginServiceBlockingStub;

public class LoginClient {

    private final LoginServiceBlockingStub blockingStub;

    public LoginClient(String host, int port) {
        this(LoginServiceGrpc.newBlockingStub(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()));
    }
    public LoginClient(LoginServiceBlockingStub blockingStub) {
        this.blockingStub = blockingStub;
    }

    public String login(final String username, final String password) {
        final LoginRequest request = LoginRequest.newBuilder().setUsername(username).setPassword(password).build();
        final LoginResponse response = blockingStub.login(request);
        return response.getToken();
    }

}
