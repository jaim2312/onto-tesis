package com.upn.model;

public class Conocimiento {
	private int nId;
	private String sNombre;
	private String sURI;
	private Cargo cargo;
	
	public Cargo getCargo() {
		return cargo;
	}
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	public int getnId() {
		return nId;
	}
	public void setnId(int nId) {
		this.nId = nId;
	}
	public String getsNombre() {
		return sNombre;
	}
	public void setsNombre(String sNombre) {
		this.sNombre = sNombre;
	}
	public String getsURI() {
		return sURI;
	}
	public void setsURI(String sURI) {
		this.sURI = sURI;
	}
}
