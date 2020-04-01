package com.demo.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Yingjie Zheng at 2020-03-27 15:25
 */
@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){

        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("path_route"
                //含义  当访问localhost:9527/guonei 时--->映射到 http://news.baidu.com/guonei
                , r->r.path("/guonei").uri("http://news.baidu.com/guonei"));
        return routes.build();
    }

}
