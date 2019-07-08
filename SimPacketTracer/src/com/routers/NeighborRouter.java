package com.routers;

public class NeighborRouter {
	private String name;
	private int port;

	public NeighborRouter() {
		// TODO Auto-generated constructor stub
	}

	public NeighborRouter(String name, int port) {
		this.name = name;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
