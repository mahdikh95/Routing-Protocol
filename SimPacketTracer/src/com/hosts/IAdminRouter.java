package com.hosts;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.routers.Router;

public interface IAdminRouter extends Remote {

	public void newHost(int port, String name) throws RemoteException;
}
