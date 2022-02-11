package com.support.oauth2postservice.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @GetMapping("/hi")
    @ResponseBody
    public String hi(OAuth2AuthenticationToken token) {
        return token.toString();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
