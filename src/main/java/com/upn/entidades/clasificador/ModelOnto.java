package com.upn.entidades.clasificador;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;

public class ModelOnto {
	OntModel modelo;
	Ontology ontologia;
	
	public OntModel getModelo() {
		return modelo;
	}
	public void setModelo(OntModel modelo) {
		this.modelo = modelo;
	}
	public Ontology getOntologia() {
		return ontologia;
	}
	public void setOntologia(Ontology ontologia) {
		this.ontologia = ontologia;
	}
}
