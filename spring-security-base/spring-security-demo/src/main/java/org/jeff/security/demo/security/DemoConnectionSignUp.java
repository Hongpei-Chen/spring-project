package org.jeff.security.demo.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 实现第三方登录(QQ)，业务系统没有注册用户时进行自动注册的动作
 * @author jeff
 * <p>Date 2019/9/1</p>
 */
@Component
public class DemoConnectionSignUp implements ConnectionSignUp{

    @Override
    public String execute(Connection<?> connection) {
        //根据社交用户信息默认创建用户并返回用户唯一标识

        //TODO 业务注册用户的逻辑
        //返回用户唯一标识，这里假设唯一标识是connection.getDisplayName();
        return connection.getDisplayName();
    }
}
