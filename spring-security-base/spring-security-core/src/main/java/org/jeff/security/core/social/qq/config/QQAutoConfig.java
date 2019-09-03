package org.jeff.security.core.social.qq.config;

import org.jeff.security.core.properties.QQProperties;
import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.social.qq.connect.QQConnectionFactory;
import org.jeff.security.core.social.repository.MysqlUserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import javax.sql.DataSource;

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

//    @Autowired(required = false)
//    private ConnectionSignUp connectionSignUp;
//
//    @Autowired
//    private DataSource dataSource;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        QQProperties qq = properties.getSocial().getQq();
        return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
    }
//
//    @Override
//    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//        MysqlUserConnectionRepository jdbcRepository = new MysqlUserConnectionRepository(dataSource,
//                connectionFactoryLocator, Encryptors.noOpText());
//        //表添加前缀配置
////        jdbcRepository.setTablePrefix("t_");
//        if (connectionSignUp != null) {
//            jdbcRepository.setConnectionSignUp(connectionSignUp);
//        }
//        return jdbcRepository;
//    }
}
