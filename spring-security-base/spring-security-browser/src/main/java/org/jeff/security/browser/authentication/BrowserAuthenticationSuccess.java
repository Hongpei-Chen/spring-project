package org.jeff.security.browser.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeff.security.core.constants.LoginType;
import org.jeff.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功后的处理
 * 1. 实现接口AuthenticationSuccessHandler
 * 2. 继承spring框架默认登录成功后处理
 * @author admin
 * <p>Date: 2019-08-26 11:36:00</p>
 */
@Component("browserAuthenticationSuccess")
public class BrowserAuthenticationSuccess extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        logger.info("登录成功");

        //判断登录成功后是否返回JSON数据还是跳转页面
        if (LoginType.JSON.equals(securityProperties.getBrowser().getLoginType())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(authentication));
        }else {
            //spring 默认的登录成功处理
            super.onAuthenticationSuccess(request, response, authentication);
        }

    }
}
