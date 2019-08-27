package org.jeff.security.core.validate;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码
 * @author admin
 * <p>Date: 2019-08-27 10:26:00</p>
 */
@Data
public class ImageCode {

    /**
     * 图形验证码
     */
    private BufferedImage image;
    /**
     * 随机数
     */
    private String code;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     *
     * @param image
     * @param code
     * @param expireIn 过期时间秒
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
