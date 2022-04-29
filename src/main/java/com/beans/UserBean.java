package com.beans;

public class UserBean {

	// attributes
	private int id;
	private String pseudo;
	private String surname;
	private String forename;
	private String club;
	private String email;
	private boolean isRegistered;
	
	// constructors
	public UserBean(int id, String pseudo, String surname, String forename, String email, String club) {
		this.id=id;
		this.pseudo=pseudo;
		this.surname=surname;
		this.forename=forename;
		this.email=email;
		this.club=club;
		this.isRegistered=false;
	}

	// getters
	public int getId() {
		return id;
	}
	public String getPseudo() {
		return pseudo;
	}
	public String getSurname() {
		return surname;
	}
	public String getForename() {
		return forename;
	}
	public String getClub() {
		return club;
	}
	public String getEmail() {
		return email;
	}
	public boolean getRegistered() {
		return isRegistered;
	}

	
	// setters
	public void setId(int id) {
		this.id = id;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public void setForename(String forename) {
		this.forename = forename;
	}
	public void setClub(String club) {
		this.club = club;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	
	// methods
	public void register() throws Exception {
		if(!this.getRegistered()) {
			this.setRegistered(true);
		} else {
			throw new Exception("The team ["+this.getId()+"] is already registered!");
		}
	}
	public String toString() {
		return "pseudo: "+this.getPseudo()+"\nclub: "+this.getClub();
	}
}
