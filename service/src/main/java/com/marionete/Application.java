package com.marionete;

import com.marionete.backends.AccountInfoMock;
import com.marionete.backends.UserInfoMock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        AccountInfoMock.start();
        UserInfoMock.start();
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        log.info("{} started",
                context .getEnvironment().getProperty("spring.application.name","marionete-service"));
    }
}
