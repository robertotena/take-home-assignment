package com.marionete.controller;

import com.marionete.model.UserAccount;
import com.marionete.model.UserCredentials;
import com.marionete.services.UserAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class UserAccountController {

    private final UserAccountService userAccountService;

    @RequestMapping(value = "/marionete/useraccount",
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST)
    public Mono<UserAccount> getUserAccount(@RequestBody UserCredentials userCredentials) {
        return userAccountService.getUserAccount(userCredentials);
    }

}
