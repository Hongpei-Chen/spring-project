package org.jeff.security.core.properties;

import lombok.Data;

/**
 * 验证码属性配置
 * @author admin
 * <p>Date: 2019-08-27 10:44:00</p>
 */
@Data
public class SmsCodeProperties {

    private int length = 6;
    /**
     * 过期时间，默认60秒
     */
    private int expireIn = 60;

    /**
     * 可配置接口(用‘,’间隔)
     */
    private String url;
}
