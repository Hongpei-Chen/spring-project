package org.jeff.security.core.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author admin
 * <p>Date: 2019-08-30 11:10:00</p>
 */
@Data
public class QQProperties  extends SocialProperties {

    private String providerId = "qq";
}
