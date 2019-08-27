package org.jeff.security.demo.controller;

import org.apache.commons.lang.RandomStringUtils;
import org.jeff.security.demo.dto.DeferredResultHolder;
import org.jeff.security.demo.dto.OrderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 订单处理
 * @author admin
 * <p>Date: 2019-08-23 11:03:00</p>
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderQueue orderQueue;
    private final DeferredResultHolder deferredResultHolder;

    public OrderController(OrderQueue orderQueue, DeferredResultHolder deferredResultHolder) {
        this.orderQueue = orderQueue;
        this.deferredResultHolder = deferredResultHolder;
    }

    @GetMapping
    public DeferredResult<String> order() throws Exception {
        logger.info("主线程开启");
        //生产8位随机订单号
        String oderNumber = RandomStringUtils.randomNumeric(8);
        orderQueue.setPlaceOrder(oderNumber);
        //响应结果
        DeferredResult<String> result = new DeferredResult<>();
        //注入
        deferredResultHolder.getMap().put(oderNumber, result);
        logger.info("主线程关闭");
        return result;
    }
}
