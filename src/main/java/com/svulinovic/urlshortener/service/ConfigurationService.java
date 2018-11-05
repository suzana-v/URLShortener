package com.svulinovic.urlshortener.service;

import com.svulinovic.urlshortener.model.AccountRequest;
import com.svulinovic.urlshortener.model.AccountResponse;
import com.svulinovic.urlshortener.model.RegisterRequest;
import com.svulinovic.urlshortener.model.RegisterResponse;
import com.svulinovic.urlshortener.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

@Service
public class ConfigurationService {

    @Autowired
    public ConfigurationRepository configurationRepository;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Value("${shortUrlPrefix}")
    private String shortUrlPrefix;

    public AccountResponse createAccount(AccountRequest request) {
        AccountResponse response = new AccountResponse();

        try {
            //check if account exists
            if (!configurationRepository.isAccountIdUnique(request.getAccountId())) {
                response.setSuccess(false);
                response.setDescription("Account with that ID already exists");
                response.setPassword(null);
                return response;
            }

            //generate password
            String password = generateAlphanumericString(8);

            //save account to database
            configurationRepository.saveAccount(request.getAccountId(), PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(password));

            response.setSuccess(true);
            response.setDescription("Your account is open");
            response.setPassword(password);

        } catch (Exception e) {
            e.printStackTrace();

            response.setSuccess(false);
            response.setDescription("General error - Failed to create account");
            response.setPassword(null);
        }

        return response;
    }

    private String generateAlphanumericString(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return builder.toString();
    }

    public RegisterResponse registerURL(RegisterRequest request) {
        RegisterResponse response = new RegisterResponse();

        try {
            //if not selected, set 302 as redirectType
            if (301 != request.getRedirectType() && 302 != request.getRedirectType()) {
                request.setRedirectType(302);
            }

            //if it exists, get urlCode for url and redirectType from db
            String urlCode = configurationRepository.getUrlCode(request.getUrl(), request.getRedirectType());

            if (urlCode == null || "".equals(urlCode)) {
                //generate unique code for url and redirectType
                String uniqueCode = getUniqueCode(request.getUrl() + request.getRedirectType());

                //if it is unique, use first 6 chars of generated code
                if (configurationRepository.isUrlCodeUnique(uniqueCode.substring(0, 6))) {
                    urlCode = uniqueCode.substring(0, 6);
                } else { //else use other 6 chars
                    urlCode = uniqueCode.substring(6, 12);
                }

                //save url, redirectType and code to database
                configurationRepository.saveUrlRedirectSettings(request.getUrl(), request.getRedirectType(), urlCode);
            }

            response.setShortUrl(shortUrlPrefix + urlCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getUniqueCode(String message) {
        String code = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(message.getBytes("UTF-8"));
            code = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public Map<String, Integer> getStatistics() {
        return configurationRepository.getStatistics();
    }

}
