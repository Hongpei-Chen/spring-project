package org.jeff.spring.security.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class TimeInterceptor  implements HandlerInterceptor {

    /**
     * 请求到Controller方法之前拦截
     * @param request
     * @param response
     * @param handler {@link HandlerMethod} 处理请求的方法
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("preHandle");

//        System.out.println(((HandlerMethod)handler).getBean().getClass().getName());
//        System.out.println(((HandlerMethod)handler).getMethod().getName());

        request.setAttribute("startTime", new Date().getTime());

//        System.out.println("id: " + request.getParameter("id"));
        return true;
    }

    /**
     * 方法正常执行完成后执行
     * @param request
     * @param response
     * @param handler {@link HandlerMethod} 处理请求的方法
     * @param modelAndView
     * @return
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
//        System.out.println("postHandle");
        Long start = (Long) request.getAttribute("startTime");
//        System.out.println("time interceptor 耗时:"+ (new Date().getTime() - start));
    }

    /**
     * 方法异常执行，若被之前的拦截器拦截了异常，那将不会执行该方法
     * @param request
     * @param response
     * @param handler {@link HandlerMethod} 处理请求的方法
     * @param ex
     * @return
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        System.out.println("afterCompletion");
        Long start = (Long) request.getAttribute("startTime");
//        System.out.println("time interceptor 耗时:"+ (new Date().getTime() - start));
//        System.out.println("ex is "+ex);
    }
}
