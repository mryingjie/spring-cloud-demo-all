package com.demo.springcloud.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * created by Yingjie Zheng at 2020/4/14 14:04
 */
@Component
public class CasFilter extends ZuulFilter {

    @Value("${robot.cas.innocent.path:/order}")
    private String innocentPath;

    @Value("${zuul.prefix:/}")
    private String zuulPrefix;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        try {
            RequestContext.getCurrentContext().addZuulRequestHeader("waliyun-account", URLEncoder.encode("account-info", "UTF-8"));
            HttpServletResponse response = RequestContext.getCurrentContext().getResponse();
            HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

            RequestContext.getCurrentContext().addZuulRequestHeader("cookie", request.getHeader("cookie") + "; test_cookie=11111");

            Cookie[] cookies1 = request.getCookies();
            Enumeration<String> cookieEnumeration = request.getHeaders("cookie");

            response.addCookie(new Cookie("test_cookie", "test_cookie1"));

            System.out.println(Arrays.toString(cookies1));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;

    }


}
