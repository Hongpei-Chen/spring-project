package org.jeff.security.core.social.weixin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author admin
 * <p>Date: 2019-09-02 10:35:00</p>
 */
public class WeixinImpl extends AbstractOAuth2ApiBinding implements Weixin {

    /**
     * 获取用户信息的url
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    private ObjectMapper objectMapper = new ObjectMapper();

    public WeixinImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     */
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String url = URL_GET_USER_INFO + openId;
        String response = getRestTemplate().getForObject(url, String.class);
        if (StringUtils.contains(response, "errcode")) {
            return null;
        }
        WeixinUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(response, WeixinUserInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

}
