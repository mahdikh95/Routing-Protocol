package com.routers;

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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

	public void Registration() {

		try {
			System.out.println("<<Router registration>>\n enter a port number for conx..");
			port = sc.nextInt();
			System.out.println("enter your router name");
			name = sc.next();
			System.out.println("your router(" + name + ") listene to port :" + port);
			serverSocket = new ServerSocket(port);
			while (true) {
				Socket socket;
				try {
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
			System.err.println("enter a valid port number to listene...");
		}
	}

	public void addNeighbors() {
		String resp;
		int nbr;
		String name;
		int port;
		System.out.println("existe a neighbor router run to this router ?");
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

		} else {
			System.out.println("seccess...");
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
			AdminImp adminRouter = new AdminImp();
			adminRouter.newRouter(router);
		} catch (Exception e) {
			e.printStackTrace();
		}

		router.addNeighbors();
		router.Registration();

	}
}
