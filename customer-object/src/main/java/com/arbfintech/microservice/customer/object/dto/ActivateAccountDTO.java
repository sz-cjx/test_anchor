package com.arbfintech.microservice.customer.object.dto;

import javax.validation.constraints.NotNull;

public class ActivateAccountDTO {

    @NotNull(message = "email can not be null.")
    private String email;

    @NotNull(message = "password can not be null.")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
