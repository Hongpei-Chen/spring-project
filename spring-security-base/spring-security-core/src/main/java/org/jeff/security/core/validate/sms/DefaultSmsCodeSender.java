package org.jeff.security.core.validate.sms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author admin
 * <p>Date: 2019-08-28 11:42:00</p>
 */
public class DefaultSmsCodeSender implements SmsCodeSender {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void send(String mobile, String code) {
        logger.info("向手机【{}】发送验证码【{}】", mobile, code);
    }
}
