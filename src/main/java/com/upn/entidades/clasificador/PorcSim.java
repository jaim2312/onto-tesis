package com.upn.entidades.clasificador;

public class PorcSim {
	//private IndividuoTI individuo;
	private int numLinea;
	private String descLinea;
	private String subCadena;
	private String descOnto;
	private double porcentaje;
	
	/*public IndividuoTI getIndividuo() {
		return individuo;
	}
	public void setIndividuo(IndividuoTI individuo) {
		this.individuo = individuo;
	}*/
		
	public int getNumLinea() {
		return numLinea;
	}
	public String getDescOnto() {
		return descOnto;
	}
	public void setDescOnto(String descOnto) {
		this.descOnto = descOnto;
	}
	public void setNumLinea(int numLinea) {
		this.numLinea = numLinea;
	}
	public String getDescLinea() {
		return descLinea;
	}
	public void setDescLinea(String descLinea) {
		this.descLinea = descLinea;
	}
	public String getSubCadena() {
		return subCadena;
	}
	public void setSubCadena(String subCadena) {
		this.subCadena = subCadena;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
}
