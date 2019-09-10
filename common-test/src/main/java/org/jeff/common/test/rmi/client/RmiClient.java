package org.jeff.common.test.rmi.client;

import org.jeff.common.test.rmi.IUserService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author admin
 * <p>Date: 2019-09-10 17:42:00</p>
 */
public class RmiClient {

    public static void main(String[] args) {
        try {
            IUserService userService = (IUserService) Naming.lookup("rmi://localhost:8888/userService");

            System.out.println(userService.getUser("0001"));
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
