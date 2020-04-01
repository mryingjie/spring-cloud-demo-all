package order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * created by Yingjie Zheng at 2020-03-26 11:43
 */
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }

}
