package org.jeff.security.browser.config;

import org.jeff.security.core.filter.ValidateCodeFilter;
import org.jeff.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author admin
 * <p>Date: 2019-08-23 15:48:00</p>
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler browserAuthenticationSuccess;

    @Autowired
    private AuthenticationFailureHandler browserAuthenticationFailure;

    /**
     * 密码加密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
        validateCodeFilter.setAuthenticationFailureHandler(browserAuthenticationFailure);
        validateCodeFilter.setSecurityProperties(securityProperties);
        validateCodeFilter.afterPropertiesSet();

        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                //表单验证
                .formLogin()
                //跳转到指定的controller处理认证逻辑
                .loginPage("/authentication/require")
                //通知UsernamePasswordAuthenticationFilter处理登录验证的路径
                .loginProcessingUrl("/authentication/form")
                .successHandler(browserAuthenticationSuccess)
                .failureHandler(browserAuthenticationFailure)
                .and()
                .authorizeRequests()
                //匹配可放行页面
                .antMatchers("/authentication/require",
                        securityProperties.getBrowser().getLoginPage(),
                        "/code/image").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //跨站伪造攻击配置
                .csrf().disable();
    }
}
