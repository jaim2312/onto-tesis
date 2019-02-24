 package com.upn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.springframework.core.io.ClassPathResource;

import com.upn.entidades.JsTree;
import com.upn.entidades.JsTreeAttr;
import com.upn.entidades.JsTreeState;
import com.upn.entidades.OntoJsTree;
import com.upn.entidades.clasificador.ModelOnto;
import com.upn.model.Conocimiento;

public class Util {
	
	public static ModelOnto getOntology(/*ServletContext context*/) throws IOException{
		//String onto_path = context.getRealPath("/resources/TI.rdf");				
		ModelOnto modelOnto = new ModelOnto();
		File file_onto = new ClassPathResource("/static/TI.rdf").getFile();
		//File file_onto = new File(onto_path);
		
		FileInputStream reader = new FileInputStream(file_onto);
		//OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		
		model.read(reader,null);
	    //model.write(System.out,"RDF/XML");
		
		Ontology onto = null;
		Iterator iter = model.listOntologies();
		    
		if(iter.hasNext()){
		    onto = (Ontology) iter.next();
		}
		
		modelOnto.setModelo(model);
		modelOnto.setOntologia(onto);
		
		return modelOnto;
	}
	
	public static ArrayList<OntoJsTree> listadoOntoNodo(Ontology onto, boolean onlyClass) {
		//Este arreglo guardará los nodos de la ontología
		ArrayList<OntoJsTree> listOntoNodo = new ArrayList<OntoJsTree>();
		
		// Recorremos la ontologia y guardamos cada nodo en una lista
		for (Iterator<OntClass> i = onto.getOntModel().listClasses();i.hasNext();){
			OntClass cls = i.next();
			OntClass clsParent = cls.getSuperClass();
			
			listOntoNodo.add(new OntoJsTree(cls, clsParent));
			
			/*System.out.println(cls.getLocalName());
			System.out.println(cls.getNameSpace());
			System.out.println(cls.getURI());*/
			
			Resource findNode = onto.getOntModel().getResource(cls.getURI());
			
			//System.out.println("Será-->" + findNode.getLocalName());
			
			if(!onlyClass) {			
				for(Iterator it = cls.listInstances(true);it.hasNext();){
					Individual ind = (Individual) it.next();
					if(ind.isIndividual()){
						listOntoNodo.add(new OntoJsTree(ind, cls));
					}
				}			
			}
		}
		
		return listOntoNodo;
		
	}
	
	public static ArrayList<OntClass> listSubClass(OntClass clase, ArrayList<OntoJsTree> listado){
		
		ArrayList<OntClass> lstSubClases = new ArrayList<OntClass>();
		lstSubClases.add(clase);
		
		
		OntoJsTree ontoNodo = new OntoJsTree(clase,null);
			
        if (ontoTieneHijo(ontoNodo.getNodeChild().getLocalName(), listado) == true){
        	System.out.println(clase.getLocalName() + " tiene hijos!");
        	
        	lstSubClases = ConstruirNodoHijo(listado, ontoNodo, lstSubClases);

            //if (resultado != null) jsNodo.setChildren(resultado);
        }
			
		return lstSubClases;
	}
	
	public static Conocimiento bEncNodo(OntoJsTree nodo_recorrido_actual, ArrayList<Conocimiento> lstSelect) {
        //boolean bEncontrado = false;
		Conocimiento bEncontrado = null;
        for(Conocimiento cno : lstSelect){
        	if(cno.getsURI().equals(nodo_recorrido_actual.getNodeChild().getURI())){
        		//bEncontrado = true;
        		bEncontrado = cno;
        		break;
        	}
        }
        
        return bEncontrado;
	}
	
	public static Boolean bEncNodo(Individual individual, ArrayList<Conocimiento> lstSelect) {
        //boolean bEncontrado = false;
		Boolean bEncontrado = false;
        for(Conocimiento cno : lstSelect){
        	if(cno.getsURI().equals(individual.getURI())){
        		bEncontrado = true;
        		//bEncontrado = cno;
        		break;
        	}
        }
        
        return bEncontrado;
	}	
	
    protected static boolean ontoTieneHijo(String idpadre, ArrayList<OntoJsTree> listado)
    {

        boolean tieneHijo = false;
        for (OntoJsTree node : listado) {
            if(node.getNodeParent() != null ){
	            if ( node.getNodeParent().getLocalName().equals(idpadre) ) {
	                tieneHijo = true;
	                break;
	            }
            }
        }
        return tieneHijo;
    }
    
	protected static ArrayList<OntClass> ConstruirNodoHijo(ArrayList<OntoJsTree> listado, OntoJsTree nodo, 
			ArrayList<OntClass> listadoHijos)
	{
	    if (nodo.getNodeChild() != null) {

	        for(OntoJsTree nodo_recorrido_actual : listado) {
	        	if(nodo_recorrido_actual.getNodeParent() != null ){
		            if ( nodo_recorrido_actual.getNodeParent().getLocalName().equals(nodo.getNodeChild().getLocalName()) ) {	                	
		                
		            	listadoHijos.add(nodo_recorrido_actual.getNodeChild().asClass());
		            	
		                if (ontoTieneHijo(nodo_recorrido_actual.getNodeChild().getLocalName(), listado)) {
		                	ConstruirNodoHijo(listado, nodo_recorrido_actual, listadoHijos);
		                }
		            }
	        	}
	        }
	    }

	    return listadoHijos;
	}    
}
