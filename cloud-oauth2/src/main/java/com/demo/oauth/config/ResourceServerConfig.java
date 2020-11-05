package com.demo.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * created by Yingjie Zheng at 2020/11/3 19:23
 */
@Configuration
@EnableResourceServer //这个类表明了此应用是OAuth2 的资源服务器，此处主要指定了受资源服务器保护的资源链接
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {



    @Override
    public void configure(HttpSecurity http) throws Exception {


        http.csrf().disable()//禁用了 csrf 功能
                .authorizeRequests()//限定签名成功的请求
                // .antMatchers("/decision/**","/govern/**").hasAnyRole("USER","ADMIN")
//                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/index").authenticated()//签名成功后可访问，不受role限制
//                .antMatchers("/admin/login","/oauth/**").permitAll()
                .anyRequest().permitAll()//其他没有限定的请求，允许访问
                .and().httpBasic();//启用http 基础验证


    }



}

