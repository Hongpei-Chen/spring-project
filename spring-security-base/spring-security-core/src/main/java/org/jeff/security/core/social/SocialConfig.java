package org.jeff.security.core.social;

import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.social.repository.MysqlUserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

/**
 * QQAutoConfig，WeixinAutoConfiguration，SocialConfig 这3个都是 SocialConfigurerAdapter 的子类，
 * 但是只有 SocialConfig 覆盖了 SocialConfigurerAdapter  的  getUsersConnectionRepository 方法
 * 如果SocialConfig 先加载 QQAutoConfig 或 WeixinAutoConfiguration 后加载，由于后加载的配置没有重写 getUsersConnectionRepository 方法，
 * 所以最终会用 SocialConfigurerAdapter 里的默认配置。在 SocialConfig 加了 @Order(10) 以后，
 * 确保了 SocialConfig 会被最后加载，所以 UsersConnectionRepository 会用最后加载的 SocialConfig 里的配置。
 * @author admin
 * <p>Date: 2019-08-30 10:39:00</p>
 */
@Order(10)
@Configuration
@EnableSocial
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    /**
     * 数据库配置
     *
     * 需要放置继承SocialAutoConfigurerAdapter的配置类中，该配置才有效果
     * @param connectionFactoryLocator
     * @return
     */
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        MysqlUserConnectionRepository jdbcRepository = new MysqlUserConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        //表添加前缀配置
//        jdbcRepository.setTablePrefix("t_");
        if (connectionSignUp != null) {
            jdbcRepository.setConnectionSignUp(connectionSignUp);
        }
        return jdbcRepository;
    }

    /**
     * 第三方登录拦截路径配置
     * @return
     */
    @Bean
    public SpringSocialConfigurer springSocialConfigurer() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
        MySpringSocialConfigurer configurer = new MySpringSocialConfigurer(filterProcessesUrl);
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }

    /**
     * 配置用户的第三方登录，跳转到注册或绑定页面是获取Social的用户信息，
     * 并绑定qq用户和业务用户的关系
     * @param connectionFactoryLocator
     * @return
     */
    @Bean
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator)) {
        };
    }
}
