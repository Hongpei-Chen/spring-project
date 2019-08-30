package org.jeff.security.core.social.qq.connect;

import org.jeff.security.core.social.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @author admin
 * <p>Date: 2019-08-30 10:35:00</p>
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {



    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
