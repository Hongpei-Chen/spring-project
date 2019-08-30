package org.jeff.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author admin
 * <p>Date: 2019-08-26 10:36:00</p>
 */
@Data
@ConfigurationProperties(prefix = "jeff.security")
public class SecurityProperties {

    private BrowserProperties browser = new BrowserProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    private SocialProperties social = new SocialProperties();

}
