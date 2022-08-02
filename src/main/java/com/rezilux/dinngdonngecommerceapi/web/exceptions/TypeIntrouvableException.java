package com.rezilux.dinngdonngecommerceapi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TypeIntrouvableException extends RuntimeException {
    public TypeIntrouvableException(String s) {
        super(s);
    }
}
