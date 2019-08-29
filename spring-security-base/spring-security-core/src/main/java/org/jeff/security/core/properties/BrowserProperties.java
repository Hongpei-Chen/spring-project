package org.jeff.security.core.properties;

import lombok.Data;
import org.jeff.security.core.constants.LoginType;
import org.jeff.security.core.constants.SecurityConstants;

/**
 * @author admin
 * <p>Date: 2019-08-26 10:36:00</p>
 */
@Data
public class BrowserProperties {

    private String loginPage = SecurityConstants.DEFAULT_LOGIN_PAGE_URL;

    private LoginType loginType = LoginType.JSON;

    /**
     * token 的过期时间
     */
    private int rememberMeSeconds = 3600;

}
