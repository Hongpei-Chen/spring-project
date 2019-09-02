package org.jeff.security.core.social.weixin.api;

/**
 * @author admin
 * <p>Date: 2019-09-02 10:31:00</p>
 */
public interface Weixin {

    WeixinUserInfo getUserInfo(String openId);
}
