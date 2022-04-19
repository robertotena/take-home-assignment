package com.marionete.controller;

import com.marionete.model.Account;
import com.marionete.model.User;
import com.marionete.model.UserAccount;
import com.marionete.model.UserCredentials;
import com.marionete.services.UserAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserAccountControllerTest {

    @Test
    public void testControllerDelegatesOnService(){
        UserAccountService service = mock(UserAccountService.class);
        UserCredentials credentials = new UserCredentials("user", "pass");

        Mono<UserAccount> expectedResult = Mono.just(new UserAccount(new User("name", "surname", "sex", 1), new Account("account")));

        when(service.getUserAccount(credentials)).thenReturn(expectedResult);
        UserAccountController controller = new UserAccountController(service);

        Mono<UserAccount> result = controller.getUserAccount(credentials);
        assertEquals(expectedResult, result);
        verify(service, times(1)).getUserAccount(credentials);
    }


}
