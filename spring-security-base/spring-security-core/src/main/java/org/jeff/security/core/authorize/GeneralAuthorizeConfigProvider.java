package org.jeff.security.core.authorize;

import org.jeff.security.core.constants.SecurityConstants;
import org.jeff.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 *
 * @author admin
 * <p>Date: 2019-09-06 15:09:00</p>
 */
@Component
@Order(Integer.MIN_VALUE)
public class GeneralAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                securityProperties.getBrowser().getLoginPage(),
                SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                securityProperties.getBrowser().getSignUpUrl(),
                "/session/invaild")
                .permitAll();

        return false;
    }
}
