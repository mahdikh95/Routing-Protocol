package com.adminRouter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Handler extends Thread {

	private Socket socket;
	private List<Handler> handlers = new ArrayList<>();
	private ObjectInputStream in;
	private ObjectOutputStream out;

	public Handler(Socket socket, List<Handler> handlers) throws IOException {
		this.handlers = handlers;
		this.socket = socket;
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());
	}

	@Override
	public void run() {

		while (true) {

		}

	}
	
	
	public void send() {
		
	}
	
	public void closeConnecion() {
		
	}
}
