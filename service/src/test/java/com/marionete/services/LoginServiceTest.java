package com.marionete.services;

import com.marionete.grpc.LoginClient;
import com.marionete.model.UserCredentials;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    @Test
    public void testLogin() {
        UserCredentials credentials = new UserCredentials("user", "pass");
        LoginClient loginClient = mock(LoginClient.class);
        LoginService service = new LoginService(loginClient);
        String expectedToken = "token";

        when(loginClient.login(credentials.getUsername(), credentials.getPassword())).thenReturn(expectedToken);

        String token = service.login(credentials);

        assertEquals(expectedToken, token);

        verify(loginClient, times(1)).login(credentials.getUsername(), credentials.getPassword());
    }

}
