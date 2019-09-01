package org.jeff.security.core.social.qq.connect;

import org.jeff.security.core.social.qq.api.QQ;
import org.jeff.security.core.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * @author admin
 * <p>Date: 2019-08-30 10:19:00</p>
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 认证服务器地址
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 申请令牌的地址
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    public QQServiceProvider(String appId, String appSecret) {
        //OAuth2Template 是spring 默认的OAuth2Operations实现
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        //QQImpl 需要多实例，每个用户的accessToken不同
        return new QQImpl(accessToken, appId);
    }
}
