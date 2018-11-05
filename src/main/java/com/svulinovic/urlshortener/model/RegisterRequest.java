package com.svulinovic.urlshortener.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RegisterRequest {

    @NotNull
    @NotBlank
    private String url;

    private Integer redirectType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(Integer redirectType) {
        this.redirectType = redirectType;
    }
}
