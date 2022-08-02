package com.rezilux.dinngdonngecommerceapi.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PersonneBadRequestException extends RuntimeException {
    private Boolean statut;

    public PersonneBadRequestException(String s, Boolean statut) {
        super(s);
        this.statut = statut;
    }

    public Boolean getStatut() {
        return statut;
    }

    public void setStatut(Boolean statut) {
        this.statut = statut;
    }
}
