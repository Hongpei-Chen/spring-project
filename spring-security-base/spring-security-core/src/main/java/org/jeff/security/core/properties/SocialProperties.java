package org.jeff.security.core.properties;

import lombok.Data;

/**
 * @author admin
 * <p>Date: 2019-08-30 11:11:00</p>
 */
@Data
public class SocialProperties {

    private String filterProcessesUrl = "/auth";

    private QQProperties qq = new QQProperties();
}
