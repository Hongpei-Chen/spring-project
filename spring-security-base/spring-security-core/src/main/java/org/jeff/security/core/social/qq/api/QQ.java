package org.jeff.security.core.social.qq.api;

/**
 * @author admin
 * <p>Date: 2019-08-30 09:55:00</p>
 */
public interface QQ {

    /**
     * 获取QQ返回的用户信息
     * @return {@link QQUserInfo}
     */
    QQUserInfo getUserInfo();
}
