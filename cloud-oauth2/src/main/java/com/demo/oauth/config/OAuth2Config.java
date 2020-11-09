package com.demo.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Yingjie Zheng at 2020/11/2 20:26
 */
@Configuration
//开启授权服务
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    @Resource
    private DataSource dataSource;

    /**
     * redis工厂，默认使用lettue
     */
    @Autowired
    public RedisConnectionFactory redisConnectionFactory;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;



    @Bean
    public TokenStore tokenStore() {
        //使用redis存储token
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        //设置redis token存储中的前缀
        redisTokenStore.setPrefix("auth-token:");
        return redisTokenStore;
    }

    @Bean
    public DefaultTokenServices tokenService() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //配置token存储
        tokenServices.setTokenStore(tokenStore());
        // token有效期，设置12小时
        tokenServices.setAccessTokenValiditySeconds(300);
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setRefreshTokenValiditySeconds(600);
        tokenServices.setReuseRefreshToken(false);
        return tokenServices;
    }

    //这个是定义授权的请求的路径的Bean
    @Bean
    public ClientDetailsService clientDetails() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        // jdbcClientDetailsService.setSelectClientDetailsSql();
        // jdbcClientDetailsService.setPasswordEncoder(passwordEncoder());
        return jdbcClientDetailsService;
    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    //添加商户信息
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.withClientDetails(clientDetails());
    }

    //定义授权和令牌端点和令牌服务
    public void configure(AuthorizationServerEndpointsConfigurer endpointsConfigurer){

        //刷新令牌时需要的认证管理和用户信息来源
        endpointsConfigurer.allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                //配置token存储的服务与位置
                .tokenServices(tokenService())
                .authorizationCodeServices(authorizationCodeServices())
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userApprovalHandler(new DefaultUserApprovalHandler())
                .tokenGranter(new CompositeTokenGranter(getCustomizedTokenGranters()));
    }

    private List<TokenGranter> getCustomizedTokenGranters() {
        AuthorizationServerTokenServices tokenServices = tokenService();
        ClientDetailsService clientDetails = clientDetails();
        AuthorizationCodeServices authorizationCodeServices = authorizationCodeServices();
        OAuth2RequestFactory requestFactory = new DefaultOAuth2RequestFactory(clientDetails);

        AuthorizationCodeTokenGranter authorizationCodeTokenGranter = new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetails, requestFactory);
        RefreshTokenGranter refreshTokenGranter = new RefreshTokenGranter(tokenServices, clientDetails, requestFactory);
        ImplicitTokenGranter implicit = new ImplicitTokenGranter(tokenServices, clientDetails, requestFactory);
        ClientCredentialsTokenGranter clientCredentialsTokenGranter = new ClientCredentialsTokenGranter(tokenServices, clientDetails, requestFactory);
        // 设置返回refresh code
        clientCredentialsTokenGranter.setAllowRefresh(true);

        List<TokenGranter> tokenGranters = new ArrayList<>();
        tokenGranters.add(authorizationCodeTokenGranter);
        tokenGranters.add(refreshTokenGranter);
        tokenGranters.add(implicit);
        tokenGranters.add(clientCredentialsTokenGranter);

        if (authenticationManager != null) {
            tokenGranters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetails, requestFactory));
        }

        return tokenGranters;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {

        //允许表单认证
        oauthServer.allowFormAuthenticationForClients();

        //允许 check_token 访问
        oauthServer.checkTokenAccess("permitAll()");
    }




    @Bean
    PasswordEncoder passwordEncoder() {
        // 加密方式
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }



}
