package org.jeff.security.core.authentication.mobile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author jeff
 * <p>Date 2019/8/28</p>
 */
@Configuration
public class SmsCodeAuthenticationConfig extends
        SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        SmsCodeAuthenticationFilter smsFilter = new SmsCodeAuthenticationFilter();
        smsFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsFilter.setAuthenticationSuccessHandler(successHandler);
        smsFilter.setAuthenticationFailureHandler(failureHandler);

        SmsCodeAuthenticationProvider smsProvider = new SmsCodeAuthenticationProvider();
        smsProvider.setUserDetailsService(userDetailsService);

        http.authenticationProvider(smsProvider)
                .addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
