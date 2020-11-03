package com.demo.springcloud.zuul.filter;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * created by Yingjie Zheng at 2020/4/14 11:14
 */
public class InnocentRequestFilter implements Filter {



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        System.out.println(uri);
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        System.out.println(path);
        // request.getRequestDispatcher(path).forward(request, response);
        filterChain.doFilter(request, response);

    }



    /**
     * 判断是否是不需要过滤的接口路径
     *
     * @param notNeedFilterUrl
     * @return
     */
    public static boolean checkUrlNotNeedFilter(String notNeedFilterUrl, String location) {
        // 路径中包含open，则认为是不需要走filter的接口
        if (location.contains("/open/")) {
            return true;
        }

        if (notNeedFilterUrl != null && !notNeedFilterUrl.isEmpty()) {
            String[] urls = StringUtils.tokenizeToStringArray(notNeedFilterUrl,
                    ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            for (String url : urls) {
                if (url.endsWith("/*")) {
                    // 路径匹配
                    url = url.substring(0, url.lastIndexOf("/*"));
                } else if (!url.startsWith("*.") && url.contains("*.")) {
                    // 因网关同时接入多个项目，相比单个项目的后缀匹配，这里要求带路径，不然容易误判
                    String suffix = url.substring(url.lastIndexOf("."));
                    // 后缀匹配
                    if (!location.endsWith(suffix)) {
                        continue;
                    }

                    url = url.substring(0, url.lastIndexOf("*."));
                }

                // 精确匹配或路径匹配
                if (location.startsWith(url)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean checkUrlNotNeedFilter(String notNeedFilterUrl, String zuulPrefix,
                                                HttpServletRequest request) {
        String location = request.getRequestURI();
        // 路径中包含open，则认为是不需要走filter的接口
        if (location.contains("/open/")) {
            return true;
        }

        if (notNeedFilterUrl != null && !notNeedFilterUrl.isEmpty()) {
            if (!zuulPrefix.endsWith("/")) {
                zuulPrefix += "/";
            }

            if (location.startsWith(zuulPrefix) && !notNeedFilterUrl.startsWith(zuulPrefix)) {
                // 截取zuul前缀
                location = location.substring(zuulPrefix.length());
                if (!location.startsWith("/")) {
                    location = "/" + location;
                }
            }
            return checkUrlNotNeedFilter(notNeedFilterUrl, location);
        }

        return false;
    }
}
