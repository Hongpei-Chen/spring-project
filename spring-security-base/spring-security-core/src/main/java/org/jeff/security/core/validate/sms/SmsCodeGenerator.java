package org.jeff.security.core.validate.sms;

import org.apache.commons.lang.RandomStringUtils;
import org.jeff.security.core.properties.SecurityProperties;
import org.jeff.security.core.validate.ValidateCode;
import org.jeff.security.core.validate.ValidateCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author admin
 * <p>Date: 2019-08-28 14:40:00</p>
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public ValidateCode generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }
}
