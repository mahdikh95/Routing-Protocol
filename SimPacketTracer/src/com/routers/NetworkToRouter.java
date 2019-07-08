package com.routers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class NetworkToRouter extends Thread {

	private Socket s;
	private ObjectInputStream in;

	public NetworkToRouter(Socket s) throws IOException {
		this.s = s;
		in = new ObjectInputStream(s.getInputStream());

	}

	@Override
	public void run() {

		while (true) {
			try {

				Object object = receive();
				if (object instanceof Packet) {
					
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Object receive() throws ClassNotFoundException, IOException {
		return in.readObject();
	}
}
