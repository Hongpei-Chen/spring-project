package org.jeff.common.test.rmi.server;

import cn.hutool.core.util.RandomUtil;
import org.jeff.common.test.bean.User;
import org.jeff.common.test.rmi.IUserService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author admin
 * <p>Date: 2019-09-10 17:36:00</p>
 */
public class UserServiceImpl extends UnicastRemoteObject implements IUserService {

    public UserServiceImpl() throws RemoteException {

    }

    @Override
    public User getUser(String id) throws RemoteException{
        if ("0001".equals(id)) {
            return initUser();
        }
        return null;
    }

    private static User initUser() {
        User user = new User();
        user.setId("0001");
        user.setUsername("陈飞");
        user.setAccount("chenfei");
        user.setAge(20);
        user.setPassword("qwq1#ewer$%sasd");
        user.setMobile("18621209491");
        user.setEmail("1209491@wer.com");

        return user;
    }
}
