package com.rezilux.dinngdonngecommerceapi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserPhoneAlreadyExist extends RuntimeException {
    public UserPhoneAlreadyExist(String s) {
        super(s);
    }
}
