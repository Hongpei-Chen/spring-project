package org.jeff.common.test.rmi.server;

import org.jeff.common.test.bean.User;
import org.jeff.common.test.rmi.IUserService;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @author admin
 * <p>Date: 2019-09-10 17:38:00</p>
 */
public class RmiServer {

    public static void main(String[] args) {
        try {
            //创建服务
            IUserService userService = new UserServiceImpl();

            //注册
            LocateRegistry.createRegistry(8888);

            Naming.bind("rmi://localhost:8888/userService", userService);

            System.out.println("server start success");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }


    }
}
