package com.routers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.adminRouter.IAdminRouter;

public class HostHandler extends Thread {
	private String name;
	private Socket socket;
	private List<HostHandler> clients;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static Packet packet;
	private List<RouterEntry> entries;
	private RouterToNetwork toNetwork;
	private NetworkToRouter fromNetwork;
	private Router thisRouter;
	private IAdminRouter adminRouter;

	public HostHandler(Router router, Socket socket, List<HostHandler> clients, List<RouterEntry> entries)
			throws IOException {
		this.thisRouter = router;
		this.socket = socket;
		this.clients = clients;
		this.entries = entries;
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		packet = new Packet();
		try {
			adminRouter = (IAdminRouter) Naming.lookup("rmi://localhost:5000/admin");
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
				HostHandler host;
				RouterEntry entry;
				packet = (Packet) in.readObject();
				while (packet != null && !packet.getMsg().equals("close")) {
					this.name = packet.getSrcName();
					String msg = " " + packet.getMsg();
					host = findDirectlyDest(clients, packet);// if directly in same router
					if (host != null) {
						host.senMessage(packet);
					} else {// search in routing table
						entry = foundingInRT(packet, entries);
						if (entry != null) {
							Socket socket = new Socket("localhost", entry.getNextPort());
							toNetwork = new RouterToNetwork(socket, packet);
							toNetwork.start();

							// fromNetwork = new NetworkToRouter(socket);
							// fromNetwork.start();

						} else {
							// not existe at this system
							packet.setMsg("client not found in routing table");
							this.senMessage(packet);
						}
					}

					packet = (Packet) in.readObject();

				}
				this.clients.remove(this);
				this.closeConnection();
//			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public HostHandler findDirectlyDest(List<HostHandler> clients, Packet packet) {
		for (HostHandler host : clients) {
			if (packet.getDestName().equals(host.name)) {
				return host;
			}
		}
		return null;
	}

	public void senMessage(Packet packet) {
		try {
			out.writeObject(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void goTonNextHope() {

	}

	public RouterEntry foundingInRT(Packet packet, List<RouterEntry> entries) {

		Iterator<RouterEntry> iterator = entries.iterator();
		while (iterator.hasNext()) {
			RouterEntry re = iterator.next();
			if (packet.getDestName().equals(re.getDest())) {
				return re;
			}
		}
		return null;
	}
}
