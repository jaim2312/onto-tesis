package com.upn.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.upn.entidades.OntoJsTree;
import com.upn.entidades.clasificador.ModelOnto;
import com.upn.model.Cargo;
import com.upn.model.Conocimiento;
import com.upn.service.ConocimientoService;
import com.upn.util.Util;

@Controller
@RequestMapping("/conocimiento")
public class ConocimientoController {
	
	@Autowired
    ServletContext context;
	@Autowired
	private ConocimientoService conocimientoService;
	
	@RequestMapping(value = "guardar", method = RequestMethod.POST)
	@ResponseBody
	public String guardarConocimiento(
		@RequestParam String nombre, @RequestParam String URI,
		@RequestParam int cargoId
	) throws Exception{
				
		ModelOnto mOnto = Util.getOntology();
		ArrayList<OntoJsTree> listOntoNodoClass = Util.listadoOntoNodo(mOnto.getOntologia(),true);
		
		OntResource resBuscado = mOnto.getOntologia().getOntModel().getOntResource(URI);
		
		System.out.println("Guardar - "+cargoId);
		
		if(resBuscado.isClass()) {
			System.out.println("Es clase");
			
			OntClass cls = resBuscado.asClass();
			
			ArrayList<OntClass> lstSubClases = Util.listSubClass(cls,listOntoNodoClass);
			
			System.out.println(lstSubClases);
			
			for(OntClass c : lstSubClases){
				Iterator it = c.listInstances(false);
				
				/*Conocimiento cClass = new Conocimiento();
				cClass.setsNombre(c.getLocalName());
				cClass.setsURI(c.getURI());

				Cargo cargClass = new Cargo();
				cargClass.setnId(cargoId);
				cClass.setCargo(cargClass);*/
				
				//conocimientoDAOImpl.insConocimiento(cClass);
				//System.out.println(c.getLocalName());
				
				while (it.hasNext()) {															
					Individual ind = (Individual) it.next();
					
					Conocimiento cInd = new Conocimiento();
					cInd.setsNombre(ind.getLocalName());
					cInd.setsURI(ind.getURI());
					
					Cargo cargInd = new Cargo();
					cargInd.setnId(cargoId);
					cInd.setCargo(cargInd);
					
					System.out.println(ind.getLocalName());
					conocimientoService.insConocimiento(cInd);
										
				}
			}		
		}else {
			Conocimiento cInd = new Conocimiento();
			cInd.setsNombre(resBuscado.getLocalName());
			cInd.setsURI(resBuscado.getURI());
			
			Cargo cargInd = new Cargo();
			cargInd.setnId(cargoId);
			cInd.setCargo(cargInd);
			
			System.out.println(URI);
			
			conocimientoService.insConocimiento(cInd);		
		}
		
		return "";
	}

	@RequestMapping(value = "eliminar", method = RequestMethod.POST)
	@ResponseBody
	public String eliminarConocimiento(		
		@RequestParam String URI,
		@RequestParam int cargoId
	) throws Exception{
		
		ModelOnto mOnto = Util.getOntology();
		ArrayList<OntoJsTree> listOntoNodoClass = Util.listadoOntoNodo(mOnto.getOntologia(),true);
		
		OntResource resBuscado = mOnto.getOntologia().getOntModel().getOntResource(URI);
		System.out.println("Eliminar - "+cargoId);
		
		if(resBuscado.isClass()) {
			System.out.println("Es clase");
			
			OntClass cls = resBuscado.asClass();
			
			ArrayList<OntClass> lstSubClases = Util.listSubClass(cls,listOntoNodoClass);
			
			System.out.println(lstSubClases);
			
			for(OntClass c : lstSubClases){
				Iterator it = c.listInstances(false);
				
				while (it.hasNext()) {															
					Individual ind = (Individual) it.next();				
					
					System.out.println(ind.getURI());
					
					Conocimiento obj = new Conocimiento();
					obj.setsURI(ind.getURI());
					Cargo cargo = new Cargo();
					cargo.setnId(cargoId);
					obj.setCargo(cargo);
					
					conocimientoService.delConocimiento(obj);
										
				}
			}		
		}else {
			
			System.out.println(URI);
			
			Conocimiento obj = new Conocimiento();
			obj.setsURI(URI);
			Cargo cargo = new Cargo();
			cargo.setnId(cargoId);
			obj.setCargo(cargo);
			
			conocimientoService.delConocimiento(obj);
		}
		
		return "";
	}	
	
}
