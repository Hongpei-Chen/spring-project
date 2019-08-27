package org.jeff.security.demo.code;

import org.jeff.security.core.validate.ImageCode;
import org.jeff.security.core.validate.ValidateCodeGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author admin
 * <p>Date: 2019-08-27 17:12:00</p>
 */
@Component("imageCodeGenerator")
public class DemoImageCodeGenerator implements ValidateCodeGenerator {
    @Override
    public ImageCode generate(ServletWebRequest request) {
        System.out.println("demo的图形验证代码");
        return null;
    }
}
