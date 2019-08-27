package org.jeff.security.demo.config;

import org.jeff.security.demo.filter.TimeFilter;
import org.jeff.security.demo.interceptor.TimeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private TimeInterceptor timeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(timeInterceptor);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new TimeFilter());

        List<String> urls = new ArrayList<>();
        urls.add("/*");
        registrationBean.setUrlPatterns(urls);

        return registrationBean;
    }

    /**
     * 配置异步支持
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        //配置对应的异步请求拦截器
        //异步请求配置1、副线程就开在主线程里面
//        configurer.registerCallableInterceptors(timeInterceptor);

        //配置对应的异步请求拦截器：2、使用DeferredResult设置消息
//        configurer.registerDeferredResultInterceptors(timeInterceptor);

        //异步请求默认的超出时间
        configurer.setDefaultTimeout(5000);
        //设置可重用的线程池，来替代spring默认的不重用的线程池，我们上面的Callable看的线程是每次运行的时候都是开的一个新的线程。
        // 这种就是不重用的线程池。
        configurer.setTaskExecutor(null);

    }

}
