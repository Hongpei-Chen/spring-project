package org.jeff.security.core.properties;

import lombok.Data;

/**
 * @author admin
 * <p>Date: 2019-09-05 11:29:00</p>
 */
@Data
public class OAuth2ClientProperties {
    /**
     * 第三方应用appId
     */
    private String clientId;
    /**
     * 第三方应用appSecret
     */
    private String clientSecret;
    /**
     * 针对此应用发出的token的有效时间
     */
    private int accessTokenValidateSeconds = 7200;
}
