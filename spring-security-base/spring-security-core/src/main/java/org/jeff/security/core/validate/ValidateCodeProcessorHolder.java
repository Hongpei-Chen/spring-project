package org.jeff.security.core.validate;

import org.jeff.security.core.exception.ValidateCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 查询校验码处理器
 * @author admin
 * <p>Date: 2019-08-29 10:58:00</p>
 */
@Component
public class ValidateCodeProcessorHolder {

    /**
     * spring的依赖查找
     * spring 会自动注入所有相关ValidateCodeProcessor实现到Map,key为实现Bean的名称
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }
}
