package org.jeff.security.core.social.weixin.connect;

import org.jeff.security.core.social.weixin.api.Weixin;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @author admin
 * <p>Date: 2019-09-02 11:21:00</p>
 */
public class WeixinConnectionFactory extends OAuth2ConnectionFactory<Weixin> {

    public WeixinConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeixinServiceProvider(appId, appSecret), new WeixinAdapter());
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以这里直接根据accessToken设置providerUserI即可
     * @param accessGrant
     * @return
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if (accessGrant instanceof WeixinAccessGrant) {
            return ((WeixinAccessGrant) accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<Weixin> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<>(getProviderId(),
                extractProviderUserId(accessGrant),
                accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(),
                accessGrant.getExpireTime(),
                getOAuth2ServiceProvider(),
                getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<Weixin> createConnection(ConnectionData data) {
        return new OAuth2Connection<>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<Weixin> getApiAdapter(String providerUserId) {
        return new WeixinAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<Weixin> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<Weixin>) getServiceProvider();
    }
}
