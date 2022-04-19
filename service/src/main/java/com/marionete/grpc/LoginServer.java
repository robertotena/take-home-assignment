package com.marionete.grpc;

import com.marionete.services.LoginServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginServer {

    private final int port;
    private final Server server;

    public LoginServer(final int port){
        this(port, ServerBuilder.forPort(port));
    }

    public LoginServer(final int port, ServerBuilder<?> serverBuilder){
        this.port = port;
        server = serverBuilder.addService(new LoginServiceGrpc()).build();
    }

    public void start() throws IOException {
        server.start();
        log.info("Login GRPC server started, listening on port {}", port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LoginServer.this.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
        }));
    }

    public void shutdown() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(10, TimeUnit.SECONDS);
        }
    }

}
