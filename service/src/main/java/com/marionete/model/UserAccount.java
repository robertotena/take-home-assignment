package com.marionete.model;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor
@Value
public class UserAccount {

    private final User user;
    private final Account account;

}
