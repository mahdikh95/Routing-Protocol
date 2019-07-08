package com.adminRouter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.routers.HostHandler;
import com.routers.NeighborRouter;
import com.routers.Router;
import com.routers.RouterEntry;

public class AdminImp extends UnicastRemoteObject implements IAdminRouter {

	private static int i = 1;

	public AdminImp() throws RemoteException {

	}

	private static List<Router> routers = new ArrayList<>();


	public void newRouter(Router router) {
		routers.add(router);
	}

	@Override
	public void newHost(int port, String newDest) {
		Router router = hoHasThisPort(port);
		router.routingTable.add(new RouterEntry(newDest,0, "directly", -1));
		updateRT(router, newDest);
		i = 1;
	}

	// recursive function to get all routers in the list
	public void updateRT(Router routerGetConnection, String dest) {

		List<Router> neighbors = findNeighbor(routerGetConnection, routers);
		for (Router router : neighbors) {
			router.routingTable.add(new RouterEntry(dest, i, routerGetConnection.name, routerGetConnection.port));
			i++;
			updateRT(router, dest);
		}

	}

	// search for neighbors of router that get a new connection
	public List<Router> findNeighbor(Router Router, List<Router> routers) {

		List<Router> NeighborRouter = new ArrayList<>();

		Iterator<NeighborRouter> iterator = (Iterator<NeighborRouter>) Router.connectedRouters;

		for (Router router2 : routers) {
			while (iterator.hasNext()) {
				NeighborRouter nr = iterator.next();
				if (nr.getName().equals(router2.name)) {
					NeighborRouter.add(router2);
				}
			}
		}
		return NeighborRouter;
	}

	public Router hoHasThisPort(int port) {
		for (Router router : routers) {
			if (router.port == port)
				return router;
		}
		return null;
	}

	public static void main(String[] args) {
		IAdminRouter adminRouter;
		try {
			adminRouter = new AdminImp();
			Naming.rebind("rmi://localhost:5000/admin", adminRouter);
		} catch (RemoteException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
