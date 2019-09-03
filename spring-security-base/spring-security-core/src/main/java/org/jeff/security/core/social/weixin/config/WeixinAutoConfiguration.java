/**
 * 
 */
package org.jeff.security.core.social.weixin.config;

import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.properties.WeixinProperties;
import org.jeff.security.core.social.repository.MysqlUserConnectionRepository;
import org.jeff.security.core.social.weixin.connect.WeixinConnectionFactory;
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
 * 微信登录配置
 * 
 * @author zhailiang
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "jeff.security.social.weixin", name = "app-id")
public class WeixinAutoConfiguration extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

//	@Autowired
//	private DataSource dataSource;
//
//	@Autowired(required = false)
//	private ConnectionSignUp connectionSignUp;




	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		WeixinProperties weixinConfig = securityProperties.getSocial().getWeixin();
		return new WeixinConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(),
				weixinConfig.getAppSecret());
	}

//	@Override
//	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
//		MysqlUserConnectionRepository jdbcRepository = new MysqlUserConnectionRepository(dataSource,
//				connectionFactoryLocator, Encryptors.noOpText());
//		//表添加前缀配置
////        jdbcRepository.setTablePrefix("t_");
//		if (connectionSignUp != null) {
//			jdbcRepository.setConnectionSignUp(connectionSignUp);
//		}
//		return jdbcRepository;
//	}
	
}
