package com.marionete.services;

import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import services.LoginRequest;
import services.LoginResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LoginServiceGrpcTest {

    @Test
    public void testLogin() {
        LoginRequest request = LoginRequest.newBuilder().setUsername("user").setPassword("password").build();
        StreamObserver<LoginResponse> responseObserver = mock(StreamObserver.class);
        ArgumentCaptor<LoginResponse> captor = ArgumentCaptor.forClass(LoginResponse.class);

        LoginServiceGrpc service = new LoginServiceGrpc();

        service.login(request, responseObserver);

        verify(responseObserver, times(1)).onNext(captor.capture());
        verify(responseObserver, times(1)).onCompleted();
        LoginResponse response = captor.getValue();
        assertNotNull(response);
        assertNotNull(response.getToken());
    }

}
