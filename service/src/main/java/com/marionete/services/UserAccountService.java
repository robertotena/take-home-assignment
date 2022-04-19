package com.marionete.services;

import com.marionete.model.Account;
import com.marionete.model.User;
import com.marionete.model.UserAccount;
import com.marionete.model.UserCredentials;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class UserAccountService {

    LoginService loginService;
    UserService userService;
    AccountService accountService;

    public Mono<UserAccount> getUserAccount(UserCredentials userCredentials) {
        final String token = loginService.login(userCredentials);

        final Mono<User> userMono = userService.getUser(token);
        final Mono<Account> accountMono = accountService.getAccount(token);

        return userMono.zipWith(accountMono, (user, account) -> new UserAccount(user, account));
    }
}
