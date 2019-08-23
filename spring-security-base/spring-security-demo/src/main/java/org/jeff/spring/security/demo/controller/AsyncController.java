package org.jeff.spring.security.demo.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;


@RestController
//@RequestMapping("/order")
public class AsyncController {

    private Logger logger = LoggerFactory.getLogger(getClass());

//    @GetMapping
    public Callable<String> order() throws Exception {
        logger.info("主线程开启");
        Callable<String> result = new Callable<String>() {
            @Override
            public String call() throws Exception {
                logger.info("副线程开启");
                Thread.sleep(2000);
                logger.info("副线程关闭");
                return "success";
            }
        };
        logger.info("主线程关闭");
        return result;
    }

}
