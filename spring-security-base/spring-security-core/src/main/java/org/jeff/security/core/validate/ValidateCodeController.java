package org.jeff.security.core.validate;

import org.jeff.security.core.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author admin
 * <p>Date: 2019-08-27 11:28:00</p>
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 创建验证码，根据验证码类型不同，调用不同的{@link ValidateCodeProcessor} 接口实现
     * @param request
     * @param response
     * @param type
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable String type) throws Exception {
        validateCodeProcessorHolder.findValidateCodeProcessor(type).create(new ServletWebRequest(request, response));
    }

}
