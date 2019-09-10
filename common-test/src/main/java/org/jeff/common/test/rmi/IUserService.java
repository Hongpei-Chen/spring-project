package org.jeff.common.test.rmi;

import org.jeff.common.test.bean.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author admin
 * <p>Date: 2019-09-10 17:34:00</p>
 */
public interface IUserService extends Remote {

    User getUser(String id) throws RemoteException;
}
