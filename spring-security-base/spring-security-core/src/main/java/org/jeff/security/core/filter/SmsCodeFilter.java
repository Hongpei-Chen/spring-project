//package org.jeff.security.core.filter;
//
//import org.apache.commons.lang.StringUtils;
//import org.jeff.security.core.exception.ValidateCodeException;
//import org.jeff.security.core.properties.SecurityProperties;
//import org.jeff.security.core.validate.ValidateCode;
//import org.jeff.security.core.validate.ValidateCodeProcessor;
//import org.jeff.security.core.validate.image.ImageCode;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.social.connect.web.HttpSessionSessionStrategy;
//import org.springframework.social.connect.web.SessionStrategy;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.ServletRequestUtils;
//import org.springframework.web.context.request.ServletWebRequest;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @author admin
// * <p>Date: 2019-08-27 15:36:00</p>
// */
//public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean {
//
//    private AuthenticationFailureHandler authenticationFailureHandler;
//
//    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
//
//    private Set<String> urls = new HashSet<>();
//
//    private SecurityProperties securityProperties;
//
//    private AntPathMatcher antPathMatcher = new AntPathMatcher();
//
//    /**
//     * 所有参数完成初始化后，初始化url
//     * @throws ServletException
//     */
//    @Override
//    public void afterPropertiesSet() throws ServletException {
//        super.afterPropertiesSet();
//        if (StringUtils.isNotBlank(securityProperties.getCode().getSms().getUrl())){
//            String[] configUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(
//                    securityProperties.getCode().getSms().getUrl(), ",");
//            for (String configUrl : configUrls) {
//                urls.add(configUrl);
//            }
//        }
//        urls.add("/authentication/mobile");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        boolean action = false;
//        for (String url: urls) {
//            if (antPathMatcher.match(url, request.getRequestURI())){
//                action = true;
//            }
//        }
//        if (action) {
//            try {
//                validate(new ServletWebRequest(request));
//            }catch (ValidateCodeException e){
//                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
//                return;
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//    private void validate(ServletWebRequest request) throws ServletRequestBindingException {
//        ValidateCode smsCode = (ValidateCode) sessionStrategy.getAttribute(request,
//                ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
//
//        //获取提交的验证码
//        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");
//        if (StringUtils.isBlank(codeInRequest)){
//            throw new ValidateCodeException("验证码不能为空");
//        }
//
//        if (smsCode == null){
//            throw new ValidateCodeException("验证码不存在");
//        }
//
//        if (smsCode.isExpried()) {
//            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
//            throw new ValidateCodeException("验证码已过期");
//        }
//
//        if (!StringUtils.equalsIgnoreCase(smsCode.getCode(), codeInRequest)){
//            throw new ValidateCodeException("验证码不匹配");
//        }
//        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX + "SMS");
//    }
//
//    public AuthenticationFailureHandler getAuthenticationFailureHandler() {
//        return authenticationFailureHandler;
//    }
//
//    public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
//        this.authenticationFailureHandler = authenticationFailureHandler;
//    }
//
//    public SecurityProperties getSecurityProperties() {
//        return securityProperties;
//    }
//
//    public void setSecurityProperties(SecurityProperties securityProperties) {
//        this.securityProperties = securityProperties;
//    }
//}
