package org.jeff.security.core.social.qq.config;

import org.jeff.security.core.properties.QQProperties;
import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @author admin
 * <p>Date: 2019-08-30 11:14:00</p>
 */
@Configuration
//有相应的properties配置时，该类才启用
@ConditionalOnProperty(prefix = "jeff.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties properties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qq = properties.getSocial().getQq();
        return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
    }
}
