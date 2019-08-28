package org.jeff.security.core.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码需实现基本接口
 * @author admin
 * <p>Date: 2019-08-27 17:02:00</p>
 */
public interface ValidateCodeGenerator {

    ValidateCode generate(ServletWebRequest request);

}
