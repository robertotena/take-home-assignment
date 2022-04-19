package com.marionete.services;

import com.marionete.model.Account;
import com.marionete.model.User;
import com.marionete.model.UserAccount;
import com.marionete.model.UserCredentials;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserAccountServiceTest {

    @Test
    public void testUserAccountService() {
        LoginService loginService = mock(LoginService.class);
        UserService userService = mock(UserService.class);
        AccountService accountService = mock(AccountService.class);
        UserCredentials credentials = new UserCredentials("user", "pass");
        String token = "token";
        User user = new User("name", "surname", "sex", 1);
        Account account = new Account("account");
        UserAccount expectedResult = new UserAccount(user, account);

        when(loginService.login(credentials)).thenReturn(token);
        when(userService.getUser(token)).thenReturn(Mono.just(user));
        when(accountService.getAccount(token)).thenReturn(Mono.just(account));

        UserAccountService userAccountService = new UserAccountService(loginService, userService, accountService);

        Mono<UserAccount> result = userAccountService.getUserAccount(credentials);

        assertEquals(expectedResult, result.block());
        verify(loginService, times(1)).login(credentials);
        verify(userService, times(1)).getUser(token);
        verify(accountService, times(1)).getAccount(token);
    }

}
