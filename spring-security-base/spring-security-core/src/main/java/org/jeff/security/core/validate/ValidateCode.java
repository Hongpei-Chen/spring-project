package org.jeff.security.core.validate;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author admin
 * <p>Date: 2019-08-28 11:22:00</p>
 */
@Data
public class ValidateCode implements Serializable{

    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    /**
     *
     * @param code
     * @param expireIn 过期时间秒
     */
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}
