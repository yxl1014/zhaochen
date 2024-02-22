package org.simple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author yxl17
 * @Package : org.simple.config
 * @Create on : 2024/2/22 19:21
 **/
@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration config=new CorsConfiguration();
        //config.addAllowedOrigin("*"); -过时的
        //允许哪些域名访问
        config.addAllowedOriginPattern("*");
        //是否允许前端传递验证信息   cookie
        config.setAllowCredentials(true);
        //允许所有的方法  POST GET ...
        config.addAllowedMethod("*");
        //允许哪些请求头
        config.addAllowedHeader("*");
        //暴露哪些头部信息
        config.addExposedHeader("*");
        //设置允许哪些接口可以被跨域访问
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }
}
