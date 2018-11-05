package com.svulinovic.urlshortener.controller;

import com.svulinovic.urlshortener.exception.NotFoundException;
import com.svulinovic.urlshortener.model.RedirectUrl;
import com.svulinovic.urlshortener.service.ConfigurationService;
import com.svulinovic.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigurationService configurationService;

    @GetMapping("/short/{urlCode}")
    public String redirect(@PathVariable("urlCode") String urlCode, HttpServletRequest request) {

        RedirectUrl redirectUrl = userService.getRedirectUrl(urlCode);
        if (redirectUrl == null) {
            throw new NotFoundException();
        }

        userService.updateRedirectStatistics(redirectUrl.getUrlId());

        if (redirectUrl.getRedirectType() == 301) {
            request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.MOVED_PERMANENTLY);
        } else {
            request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.FOUND);
        }

        return "redirect:" + redirectUrl.getUrl();
    }

    @GetMapping("/showStatistics")
    public String showStatistics(Model model) {
        model.addAttribute("statisticsMap", configurationService.getStatistics());
        return "statistics";
    }

}
