package com.routers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;


public class RouterToNetwork extends Thread {

	private Socket socket;
	private ObjectOutputStream out;
	private Packet packet;

	public RouterToNetwork(Socket socket, Packet packet) throws IOException {
		this.socket = socket;
		this.packet = packet;
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {
		while (true) {
			sendPacket(packet);
		}

	}

	public void sendPacket(Packet packet) {
		try {
			out.writeObject(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendRouterEntry(RouterEntry entry) {
		try {
			out.writeObject(entry);
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
}
