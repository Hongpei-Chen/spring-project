package org.jeff.security.demo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

//@Component
public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("time filter destroy");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
//        System.out.println("time filter start");
//        long start = new Date().getTime();
        filterChain.doFilter(request, response);
//        System.out.println("time filter 耗时:"+ (new Date().getTime() - start));
//        System.out.println("time filter finish");
    }

    @Override
    public void destroy() {
//        System.out.println("time filter init");
    }
}
