package org.jeff.security.core.properties;

import lombok.Data;

/**
 * 验证码属性配置
 * @author admin
 * <p>Date: 2019-08-27 10:44:00</p>
 */
@Data
public class ImageCodeProperties extends SmsCodeProperties{

    public ImageCodeProperties() {
        setLength(4);
    }

    /**
     * 宽度
     */
    private int width = 67;
    /**
     * 高度
     */
    private int height = 23;
}
