package com.routers;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.sound.midi.Soundbank;

import com.adminRouter.AdminImp;
import com.adminRouter.IAdminRouter;

public class Router {
	public int port;
	public String name;
	private static final String ip = "localhost";
	public ServerSocket serverSocket;

	public List<NeighborRouter> connectedRouters = new ArrayList<>();

	public List<String> connectedHost = new ArrayList<>();

	public List<HostHandler> nodes = new ArrayList<>();

	public List<RouterEntry> routingTable = new ArrayList<>();

	private Scanner sc;

	public Router() {
		sc = new Scanner(System.in);
	}

	public void registration() {

		try {
			System.out.println("<<Router registration>>\n enter a port number for connection :");
			port = sc.nextInt();
			System.out.println("enter your router name :");
			name = sc.next();
		
			serverSocket = new ServerSocket(port);
			 Registry registry = LocateRegistry.getRegistry("127.0.0.1", 18080);
			 IAdminRouter adminRouter = (IAdminRouter) registry.lookup("adminRouter");
			adminRouter.newRouter(this);
			while (true) {
				Socket socket;
				try {
					System.out.println("your router(" + name + ") listene to port :" + port);
					System.out.println("wait new connection...");
					socket = serverSocket.accept();
					System.out.println("connection accepted...");
					HostHandler hosthandler = new HostHandler(this, socket, nodes, routingTable);
					nodes.add(hosthandler);
					hosthandler.start();
				} catch (SocketException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("enter a valid port number to listene...");
		}
	}

	public void addNeighbors() {
		String resp;
		int nbr;
		String name;
		int port;
		System.out.println("exists a direct neighbor router  to this router ?(y/yes)");
		Scanner sc = new Scanner(System.in);
		resp = sc.nextLine();
		if (resp.equals("y") || resp.equals("yes")) {
			System.out.println("enter the number of neighbor :");
			nbr = sc.nextInt();
			for (int i = 1; i <= nbr; i++) {
				System.out.println("enter the Name and port (IP) of router neighbor :" + i);
				name = sc.next();
				port = sc.nextInt();
				connectedRouters.add(new NeighborRouter(name, port));

			}

		} 
	}

	public void generateRoutingTable() {
		// set the dataEntry of routing table(dest,cost,nextrouterName,nextrouterport)
	}

	public String selectNext(Packet packet, RouterEntry entry) {
		// return the next hope to rich dest
		return null;
	}

	public static void main(String[] args) {
		Router router = new Router();
		try {
			router.addNeighbors();
			router.registration();
		
		} catch (Exception e) {
			e.printStackTrace();
		}

		

	}
}
