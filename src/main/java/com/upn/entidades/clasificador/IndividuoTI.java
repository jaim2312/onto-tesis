package com.upn.entidades.clasificador;

import org.apache.jena.ontology.Individual;

public class IndividuoTI {
	private Individual individuo;
	private String descripcion;
	private int longitud;
	private boolean bEncontrado;
	
	public boolean isbEncontrado() {
		return bEncontrado;
	}
	public void setbEncontrado(boolean bEncontrado) {
		this.bEncontrado = bEncontrado;
	}
	public Individual getIndividuo() {
		return individuo;
	}
	public void setIndividuo(Individual individuo) {
		this.individuo = individuo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getLongitud() {
		return longitud;
	}

	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}
}
