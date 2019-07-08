package com.hosts;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;


public class NetworkToHost extends Thread {

	private Socket s;
	private ObjectInputStream in;

	public NetworkToHost(Socket s) throws IOException {
		this.s = s;
		in = new ObjectInputStream(s.getInputStream());
	}

	Packet receive = null;

	@Override
	public void run() {

		while (true) {
			try {
				receive = (Packet) in.readObject();
				System.out.println("<<<<<<<recieved :" + receive.getMsg()+">>>>>>>>>");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
