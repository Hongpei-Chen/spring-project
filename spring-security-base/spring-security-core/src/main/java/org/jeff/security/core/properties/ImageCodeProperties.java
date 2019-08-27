package org.jeff.security.core.properties;

import lombok.Data;

/**
 * 验证码属性配置
 * @author admin
 * <p>Date: 2019-08-27 10:44:00</p>
 */
@Data
public class ImageCodeProperties {

    /**
     * 宽度
     */
    private int width = 67;
    /**
     * 高度
     */
    private int height = 23;
    /**
     * 随机数个数
     */
    private int length = 4;
    /**
     * 过期时间，默认60秒
     */
    private int expireIn = 60;

    /**
     * 可配置接口(用‘,’间隔)
     */
    private String url;
}
