package com.marionete.services;

import io.grpc.stub.StreamObserver;
import services.LoginRequest;
import services.LoginResponse;

import java.util.UUID;

public class LoginServiceGrpc extends services.LoginServiceGrpc.LoginServiceImplBase {

    @Override
    public void login(LoginRequest request, StreamObserver<LoginResponse> responseObserver) {
        responseObserver.onNext(LoginResponse.newBuilder().setToken(UUID.randomUUID().toString()).build());
        responseObserver.onCompleted();
    }
}
