package org.jeff.spring.security.demo.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Http请求和响应两个线程之间传递对象
 * 下单和完成订单共享对象
 * @author admin
 * <p>Date: 2019-08-23 11:02:00</p>
 */
@Component
public class DeferredResultHolder {

    /**
     *  map<key,value> 其中key相当于订单号，一个订单号对应一个订单的处理结果
     */
    private Map<String, DeferredResult<String>> map = new HashMap<>();

    public Map<String,DeferredResult<String>> getMap() {
        return map;
    }

    public void setMap(Map<String, DeferredResult<String>> map) {
        this.map = map;
    }

}
