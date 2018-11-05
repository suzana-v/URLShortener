package com.svulinovic.urlshortener.controller;

import com.svulinovic.urlshortener.exception.BadRequestException;
import com.svulinovic.urlshortener.model.AccountRequest;
import com.svulinovic.urlshortener.model.AccountResponse;
import com.svulinovic.urlshortener.model.RegisterRequest;
import com.svulinovic.urlshortener.model.RegisterResponse;
import com.svulinovic.urlshortener.service.ConfigurationService;
import com.svulinovic.urlshortener.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @PostMapping("/account")
    public AccountResponse createAccount(@Valid @RequestBody AccountRequest request, BindingResult result) {

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            throw new BadRequestException(Helper.errorsToString(errors));
        }

        return configurationService.createAccount(request);
    }

    @PostMapping("/register")
    public RegisterResponse registerURL(@Valid @RequestBody RegisterRequest request, BindingResult result) {

        if (result.hasErrors()) {
            List<FieldError> errors = result.getFieldErrors();
            throw new BadRequestException(Helper.errorsToString(errors));
        }

        return configurationService.registerURL(request);
    }

    @GetMapping("/statistic/{accountId}")
    public Map<String, Integer> getStatistics(@PathVariable("accountId") String accountId) {
        return configurationService.getStatistics();
    }

}
