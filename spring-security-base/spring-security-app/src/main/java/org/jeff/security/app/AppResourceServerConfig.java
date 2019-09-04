package org.jeff.security.app;

import org.jeff.security.core.authentication.mobile.SmsCodeAuthenticationConfig;
import org.jeff.security.core.constants.SecurityConstants;
import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.validate.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 资源服务器
 * @author admin
 * <p>Date: 2019-09-04 11:10:00</p>
 */
@Configuration
@EnableResourceServer
public class AppResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;

    @Autowired
    private SpringSocialConfigurer springSocialConfigurer;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //密码登录配置
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(successHandler)
                .failureHandler(failureHandler);

        //校验码相关验证配置(图形验证码或短信验证码是否匹配)
        http.apply(validateCodeSecurityConfig)
                .and()
                //短信校验配置
                .apply(smsCodeAuthenticationConfig)
                .and()
                //添加social认证配置
                .apply(springSocialConfigurer)
                .and()
                .authorizeRequests()
                //匹配不需要认证的请求
                .antMatchers(
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        securityProperties.getBrowser().getLoginPage(),
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        securityProperties.getBrowser().getSignUpUrl(),
                        "/user/regist",
                        "/session/invaild")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //跨站伪造攻击配置
                .csrf().disable();
    }
}
