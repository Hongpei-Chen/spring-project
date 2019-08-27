package org.jeff.security.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码验证异常
 * @author admin
 * <p>Date: 2019-08-27 11:08:00</p>
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
