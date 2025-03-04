package org.jeff.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeff.security.core.support.SimpleResponse;
import org.jeff.security.core.constants.LoginType;
import org.jeff.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理
 * @author admin
 * <p>Date: 2019-08-26 14:51:00</p>
 */
@Component("browserAuthenticationFailure")
public class BrowserAuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        logger.info("登录失败");
        //判断登录失败是否返回JSON数据还是跳转页面
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(new SimpleResponse(exception.getMessage())));
        }else {
            //spring 默认的登录失败处理
            super.onAuthenticationFailure(request, response, exception);
        }


    }
}
