package com.svulinovic.urlshortener.service;

import com.svulinovic.urlshortener.model.RedirectUrl;
import com.svulinovic.urlshortener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public RedirectUrl getRedirectUrl(String code) {
        return userRepository.getRedirectUrl(code);
    }

    public void updateRedirectStatistics(Integer urlId) {
        userRepository.updateRedirectStatistics(urlId);
    }

}
