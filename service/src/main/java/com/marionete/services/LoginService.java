package com.marionete.services;

import com.marionete.grpc.LoginClient;
import com.marionete.model.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class LoginService {

    private final LoginClient loginClient;

    public String login(final UserCredentials userCredentials) {
        return loginClient.login(userCredentials.getUsername(), userCredentials.getPassword());
    }

}
