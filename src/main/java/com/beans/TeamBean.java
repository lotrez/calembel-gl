package com.beans;

import java.sql.Timestamp;

public class TeamBean {
	
	// attributes
	private int id;
	private String name;
	private int nbPlayers;
	
	// constructors
	public TeamBean(int id, String name) {
		this.id=id;
		this.name=name;
		this.nbPlayers=0;
	}
	
	// getters
	public int getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public int getNbPlayers() {
		return this.nbPlayers;
	}
	
	// setters
	public void setName(String name) {
		this.name=name;
	}
	private void addPlayer() {
		this.nbPlayers+=1;
	}
	
	// methods
	public String toString() {
		return "id: "+this.getId()+"\nname: "+this.getName()+"\nnbPlayers: "+this.getNbPlayers();
	}
}
