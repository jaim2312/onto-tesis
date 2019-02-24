package com.upn.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.upn.entidades.JsTree;
import com.upn.entidades.JsTreeAttr;
import com.upn.entidades.JsTreeState;
import com.upn.entidades.OntoJsTree;
import com.upn.entidades.clasificador.ModelOnto;
import com.upn.model.Cargo;
import com.upn.model.Conocimiento;
import com.upn.service.CargoService;
import com.upn.service.ConocimientoService;
import com.upn.util.Util;
import com.upn.util.UtilClasificador;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired
    ServletContext context;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private CargoService cargoService;
	@Autowired
	private ConocimientoService conocimientoService;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 * @return 
	 * @throws FileNotFoundException 
	 */

	@RequestMapping(value = "/{cargoId}", method = RequestMethod.GET)
	public String home(Locale locale, Model model,
			@PathVariable int cargoId) {
		logger.info("Welcome home! The client locale is {}.", locale);		
		Cargo cargo = cargoService.getCargo(cargoId);		
		model.addAttribute("cargo", cargo);		
		System.out.println("Entre a home "+cargoId);		
		return "home";
	}
	
	@RequestMapping(value = "/getontojstree", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getOntoJsTree(Locale locale, Model model,
			@RequestParam(value = "cargo") int cargoId) throws Exception {
		logger.info("Recuperando onto como JsTree {}.", locale);
		
		ModelOnto mOnto = Util.getOntology();
		
		String ontologyURI = mOnto.getOntologia().getURI();
		String ontologyNS = mOnto.getOntologia().getNameSpace();
		
	    System.out.println("Controller getontojstree = "+cargoId);
	    	   
	    ArrayList<OntoJsTree> listOntoNodo = Util.listadoOntoNodo(mOnto.getOntologia(),false);		
		ArrayList<JsTree> nodesList = new ArrayList<JsTree>();
		
		ArrayList<Conocimiento> listSelecConoc = conocimientoService.listarConocPorCargo(cargoId);

        for (OntoJsTree ontoNodo : listOntoNodo){
        	
            if (ontoNodo.getNodeParent() == null){
            	
            	JsTree jsNodo = new JsTree();
            	jsNodo.setParent("#");
            	//jsNodo.setId( ontoNodo.getNodeChild().getLocalName() );
            	jsNodo.setText( UtilClasificador.stripUnderscore(ontoNodo.getNodeChild().getLocalName()) );            	
                //$node->a_attr = new StdClass();
            	JsTreeAttr a_attr = new JsTreeAttr();
            	JsTreeState jsEstado = new JsTreeState();
            	a_attr.setUnique( ontoNodo.getNodeChild().getURI() );
            	
            	Conocimiento bEncontrado = Util.bEncNodo(ontoNodo, listSelecConoc);
            	
                
                if (GrupoTieneHijos(ontoNodo.getNodeChild().getLocalName() , listOntoNodo) == true){
                                    	
                	jsEstado.setOpened(true);
                	if(bEncontrado != null) {
                		jsNodo.setId( String.valueOf(bEncontrado.getnId()) );
                		jsEstado.setSelected(true);
                	}
                	jsNodo.setState(jsEstado);
                	jsNodo.setIcon("fa fa-folder-open");

                	a_attr.setEs_clase(1);

                	ArrayList<JsTree> resultado = ConstruirNodoHijo(listOntoNodo, ontoNodo, listSelecConoc);

                    if (resultado != null) jsNodo.setChildren(resultado);
                }else{
                	
                	if(ontoNodo.getNodeChild().isClass()) {
                		jsNodo.setIcon("fa fa-folder");
                		a_attr.setEs_clase(1);
                	}else {
                		jsNodo.setIcon("fa fa-user");
                		a_attr.setEs_clase(0);
                	}
                	if(bEncontrado != null){
                		jsNodo.setId( String.valueOf(bEncontrado.getnId()) );
                		jsEstado.setSelected(true);
                	}
                	jsNodo.setState(jsEstado);
                }
                
                jsNodo.setA_attr(a_attr);
                nodesList.add(jsNodo);
            }
        }	
        
        Gson gson = new Gson();
        String json = gson.toJson(nodesList);
        
        System.out.println(json);
		
		return json;
	}
	
    protected boolean GrupoTieneHijos(String idpadre, ArrayList<OntoJsTree> listado)
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

	protected ArrayList<JsTree> ConstruirNodoHijo(ArrayList<OntoJsTree> listado, OntoJsTree nodo, ArrayList<Conocimiento> lstSelect)
	{
	   	ArrayList<JsTree> listadoHijos = new ArrayList<JsTree>();

	    if (nodo.getNodeChild() != null) {
	        int hijos = 0;

	        for(OntoJsTree nodo_recorrido_actual : listado) {
	        	if(nodo_recorrido_actual.getNodeParent() != null ){
		            if ( nodo_recorrido_actual.getNodeParent().getLocalName().equals(nodo.getNodeChild().getLocalName()) ) {
		                JsTree nodoTmp = new JsTree();
	                	
		                //nodoTmp.setId( nodo_recorrido_actual.getNodeChild().getLocalName() );
		                nodoTmp.setText( UtilClasificador.stripUnderscore(nodo_recorrido_actual.getNodeChild().getLocalName()) );	                   		                
		                JsTreeAttr a_attr = new JsTreeAttr();
		                JsTreeState jsEstado = new JsTreeState();
		                a_attr.setUnique( nodo_recorrido_actual.getNodeChild().getURI() );
		                
		                // Verificar si el nodo se encuentra entre los selecionados
		                Conocimiento bEncontrado = Util.bEncNodo(nodo_recorrido_actual, lstSelect);		                
		                
		                if (GrupoTieneHijos(nodo_recorrido_actual.getNodeChild().getLocalName(), listado))
		                {		                	
		                	jsEstado.setOpened(true);
		                	if(bEncontrado != null) {
		                		nodoTmp.setId( String.valueOf(bEncontrado.getnId()) );
		                		jsEstado.setSelected(true);
		                	}
		                	nodoTmp.setState(jsEstado);
		                	nodoTmp.setIcon("fa fa-folder-open");
		                				                
			                a_attr.setEs_clase(1);
	
	                        ArrayList<JsTree> resultado = ConstruirNodoHijo(listado, nodo_recorrido_actual, lstSelect);
	                        nodoTmp.setChildren(resultado);
		                }else{
		                	if(nodo_recorrido_actual.getNodeChild().isClass()) {
		                		nodoTmp.setIcon("fa fa-folder");
		                		a_attr.setEs_clase(1);		                		
		                	}else {
		                		nodoTmp.setIcon("fa fa-user");
		                		a_attr.setEs_clase(0);
		                	}
		                	
		                	if(bEncontrado != null) {
		                		nodoTmp.setId( String.valueOf(bEncontrado.getnId()) );
		                		jsEstado.setSelected(true);
		                	}
		                	nodoTmp.setState(jsEstado);
		                }
	
		                nodoTmp.setA_attr(a_attr);
		                listadoHijos.add(nodoTmp);
	
		            }
	        	}
	        }
	    }

	    return listadoHijos;
	}	
	
}
