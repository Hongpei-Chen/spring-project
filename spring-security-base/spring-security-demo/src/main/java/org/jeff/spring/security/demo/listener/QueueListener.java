package org.jeff.spring.security.demo.listener;

import org.apache.commons.lang.StringUtils;
import org.jeff.spring.security.demo.dto.DeferredResultHolder;
import org.jeff.spring.security.demo.dto.OrderQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 监听虚拟订单消息队列
 * @author admin
 * <p>Date: 2019-08-23 11:08:00</p>
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private final OrderQueue orderQueue;
    private final DeferredResultHolder deferredResultHolder;

    public QueueListener(OrderQueue orderQueue, DeferredResultHolder deferredResultHolder) {
        this.orderQueue = orderQueue;
        this.deferredResultHolder = deferredResultHolder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(() -> {
            while (true) {
                //消息队列不为空
                if(StringUtils.isNotBlank(orderQueue.getCompleteOrder())) {
                    String orderNumber = orderQueue.getCompleteOrder();
                    logger.info("返回订单处理结果：" +orderNumber);
                    deferredResultHolder.getMap().get(orderNumber).setResult("place order success");
                    //还原消息队列为空
                    orderQueue.setCompleteOrder(null);
                }else{
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
