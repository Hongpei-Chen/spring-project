package org.jeff.security.core.validate.image;

import lombok.Data;
import org.jeff.security.core.validate.ValidateCode;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码
 * @author admin
 * <p>Date: 2019-08-27 10:26:00</p>
 */
@Data
public class ImageCode extends ValidateCode {

    /**
     * 图形验证码
     */
    private BufferedImage image;


    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

    /**
     *
     * @param image
     * @param code
     * @param expireIn 过期时间秒
     */
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

}
