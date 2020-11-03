package com.demo.oauth.controller;

import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Resource
    private DefaultTokenServices defaultTokenServices;

    @GetMapping("index")
    @ResponseBody
    public String index(HttpServletRequest httpServletRequest) {
        String access_token = httpServletRequest.getParameter("access_token");
        System.out.println(access_token);
        String clientId = defaultTokenServices.getClientId(access_token);

        return clientId;
    }
}
