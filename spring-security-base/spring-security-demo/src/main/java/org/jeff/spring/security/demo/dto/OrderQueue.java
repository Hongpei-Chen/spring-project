package org.jeff.spring.security.demo.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 模拟消息队列的对象
 * @author admin
 * <p>Date: 2019年08月23日 10:56:00</p>
 */
@Component
public class OrderQueue {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 下单的消息
     */
    private String placeOrder;

    /**
     * 订单完成的消息
     */
    private String completeOrder;


    public String getPlaceOrder() {
        return placeOrder;
    }

    public void setPlaceOrder(String placeOrder) throws Exception {
        //新开线程处理下单请求
        new Thread(() -> {
            logger.info("接到下单请求，" + placeOrder);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.completeOrder = placeOrder;
            logger.info("下单请求处理完毕，" +  placeOrder);
        }).start();
    }

    public String getCompleteOrder() {
        return completeOrder;
    }

    public void setCompleteOrder(String completeOrder) {
        this.completeOrder = completeOrder;
    }
}
