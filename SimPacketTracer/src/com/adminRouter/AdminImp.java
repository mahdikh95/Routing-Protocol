package com.adminRouter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.routers.NeighborRouter;
import com.routers.Router;
import com.routers.RouterEntry;

public class AdminImp extends UnicastRemoteObject implements IAdminRouter {

	private static final long serialVersionUID = 1L;



	private static int i = 1;

	

	private  List<Router> routers = new ArrayList<>();


	public AdminImp() throws RemoteException {
		 super();
	}
	
	@Override
	public void newRouter(Router router)  throws RemoteException {
		routers.add(router);
	}

	
	@Override
	public void newHost(int port, String newDest) {
		Router router = howHasThisPort(port);
		router.routingTable.add(new RouterEntry(newDest,0, "directly", -1));
		updateRT(router, newDest);
		i = 1;
	}

	// recursive function to get all routers in the list
	private void updateRT(Router routerGetConnection, String dest) {

		List<Router> neighbors = findNeighbor(routerGetConnection, routers);
		for (Router router : neighbors) {
			router.routingTable.add(new RouterEntry(dest, i, routerGetConnection.name, routerGetConnection.port));
			i++;
			updateRT(router, dest);
		}

	}

	// search for neighbors of router that get a new connection
	private List<Router> findNeighbor(Router Router, List<Router> routers) {

		List<Router> neighborRouter = new ArrayList<>();

		Iterator<NeighborRouter> iterator = (Iterator<NeighborRouter>) Router.connectedRouters;

		for (Router router2 : routers) {
			while (iterator.hasNext()) {
				NeighborRouter nr = iterator.next();
				if (nr.getName().equals(router2.name)) {
					neighborRouter.add(router2);
				}
			}
		}
		System.out.println("");
		System.out.println("\"|\" + router name + \" | \" + port " );
		for(Router r : neighborRouter)
		{
			System.out.println("|" + r.name + " | " + r.port );
		}
		
		return neighborRouter;
	}

	private Router howHasThisPort(int port) {
		for (Router router : routers) {
			if (router.port == port)
				return router;
		}
		return null;
	}
	
	private static Registry registry;

	public static void main(String[] args) {
	
		try {
			IAdminRouter  adminRouter = new AdminImp();
			System.setProperty("java.rmi.server.hostname","192.168.137.6");
			 LocateRegistry.createRegistry(1888).rebind("rmi://192.168.137.6:18080/admin", adminRouter);
			 System.out.println("admin has been initilized");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
