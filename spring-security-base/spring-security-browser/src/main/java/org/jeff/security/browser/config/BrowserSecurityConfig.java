package org.jeff.security.browser.config;

import org.jeff.security.core.authentication.AbstractChannelSecurityConfig;
import org.jeff.security.core.authentication.mobile.SmsCodeAuthenticationConfig;
import org.jeff.security.core.authorize.AuthorizeConfigManager;
import org.jeff.security.core.constants.SecurityConstants;
import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.validate.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * @author admin
 * <p>Date: 2019-08-23 15:48:00</p>
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //密码登录配置
        applyPasswordAuthenticationConfig(http);

        //校验码相关验证配置(图形验证码或短信验证码是否匹配)
        http.apply(validateCodeSecurityConfig)
                .and()
                //短信校验配置
                .apply(smsCodeAuthenticationConfig)
                .and()
                //添加social认证配置
                .apply(springSocialConfigurer)
                .and()
                //"记住我"的配置
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(userDetailsService)
                .and()
                //session 过期配置
                .sessionManagement()
                //失效的跳转地址
                .invalidSessionUrl("/session/invaild")
                .and()
                //跨站伪造攻击配置
                .csrf().disable();

        authorizeConfigManager.config(http.authorizeRequests());
    }

    /**
     * 记住我功能的数据库配置
     * @return
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        tokenRepository.setCreateTableOnStartup(false);
        return tokenRepository;
    }

}
