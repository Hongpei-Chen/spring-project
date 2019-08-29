package org.jeff.security.browser.controller;

import org.apache.commons.lang.StringUtils;
import org.jeff.security.browser.support.SimpleResponse;
import org.jeff.security.core.constants.SecurityConstants;
import org.jeff.security.core.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author admin
 * <p>Date: 2019-08-26 10:40:00</p>
 */
@RestController
public class BrowserSecurityController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //spring 提供的地址请求工具
    private RequestCache requestCache = new HttpSessionRequestCache();
    //spring 提供的地址跳转工具
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 当需要身份认证时，跳转到这里
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            logger.info("引发跳转的请求是:" + targetUrl);
            if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
                //如果是html请求，跳转到指定的的登录页
//                redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
            redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
        }
        //rest请求返回未授权信息
        return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页");
    }

}
