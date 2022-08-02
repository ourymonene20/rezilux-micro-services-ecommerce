package com.rezilux.dinngdonngecommerceapi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserEmailAlreadyExist extends RuntimeException {
    public UserEmailAlreadyExist(String s) {
        super(s);
    }
}
