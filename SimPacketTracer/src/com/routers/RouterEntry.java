package com.routers;

public class RouterEntry {
	private String dest;
	private int cost;// min cost to rich destination
	private String nextRouterName;// next router name
	private int nextPort;// the port of connection to the next router

	public RouterEntry() {
	}

	public RouterEntry(String dest, int cost, String nextRouterName, int nextPort) {
		super();
		this.dest = dest;
		this.cost = cost;
		this.nextRouterName = nextRouterName;
		this.nextPort = nextPort;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getNextRouterName() {
		return nextRouterName;
	}

	public void setNextRouterName(String nextRouterName) {
		this.nextRouterName = nextRouterName;
	}

	public int getNextPort() {
		return nextPort;
	}

	public void setNextPort(int nextPort) {
		this.nextPort = nextPort;
	}

}
