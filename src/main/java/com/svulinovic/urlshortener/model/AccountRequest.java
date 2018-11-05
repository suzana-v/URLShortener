package com.svulinovic.urlshortener.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AccountRequest {

    @NotNull
    @NotBlank
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
