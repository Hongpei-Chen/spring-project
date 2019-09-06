package org.jeff.security.app;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 * <p>Date: 2019-09-05 16:16:00</p>
 */
public class JwtTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> info = new HashMap<>();
        info.put("company", "org.jeff");

        DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;
        defaultAccessToken.setAdditionalInformation(info);

        return defaultAccessToken;
    }
}
