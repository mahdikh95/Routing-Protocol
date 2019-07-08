package com.hosts;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class Host {

	private static Socket s;
	private static int port;
	private static ObjectOutputStream out;
	private Scanner keyb;
	private static ObjectInputStream in;
	private String name;
	private Packet packet;

	public Host() {
		packet = new Packet();
		keyb = new Scanner(System.in);
	}

	public void hostRegistre() throws UnknownHostException, IOException {
		System.out.println("enter your name : ");
		name = keyb.nextLine();
		System.out.println("enter a router IP(port) be connect to.. ");
		port = keyb.nextInt();

		s = new Socket("localhost", port);
		// in = new ObjectInputStream(s.getInputStream());
		out = new ObjectOutputStream(s.getOutputStream());

		System.out.println("socket is open on Router's port: " + port);
		packet.setSrcName(name);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void send(Packet packet) throws IOException {
		out.writeObject(packet);
	}

	public static void main(String[] args) {
		try {

			Host host = new Host();
			host.hostRegistre();
			NetworkToHost ntu = new NetworkToHost(s);
			ntu.start();

			try { // invoke rmi to know new connection host
				IAdminRouter adminRouter = (IAdminRouter) Naming.lookup("rmi://localhost:5000/admin");
				adminRouter.newHost(port, host.getName());
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String msg = "";
			String destName;
			Scanner sc = new Scanner(System.in);
			while (true) {
				System.out.println("enter your dest name ");
				destName = sc.nextLine();
				host.packet.setDestName(destName);
				System.out.println("enter ur message and (close) to finish: ");
				do {
					System.out.print("you :>>");
					msg = sc.nextLine();
					System.out.println();
					host.packet.setMsg(msg);
					host.send(host.packet);

				} while (!msg.equals("close"));
			}
		} catch (IOException ex) {
			System.err.println("please check the Router port is running");
		}
	}
}
