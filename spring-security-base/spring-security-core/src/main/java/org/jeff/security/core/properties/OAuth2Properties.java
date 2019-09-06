package org.jeff.security.core.properties;

import lombok.Data;

/**
 * @author admin
 * <p>Date: 2019-09-05 11:28:00</p>
 */
@Data
public class OAuth2Properties {

    private String jwtSigningKey = "org.jeff";

    private OAuth2ClientProperties[] clients = {};
}
