package org.jeff.security.core.validate.sms;

/**
 * @author admin
 * <p>Date: 2019-08-28 11:41:00</p>
 */
public interface SmsCodeSender {

    void send(String mobile, String code);
}
