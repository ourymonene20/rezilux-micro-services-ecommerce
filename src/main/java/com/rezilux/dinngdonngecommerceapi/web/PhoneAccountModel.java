package com.rezilux.dinngdonngecommerceapi.web;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PhoneAccountModel {

    Long id;

    @NotNull
    @Pattern(regexp="[\\d]+")
    private String phone;
    @NotNull
    @Size(min = 1, max = 4)
    private String indicative;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndicative() {
        return indicative;
    }

    public void setIndicative(String indicative) {
        this.indicative = indicative;
    }
}
