package com.upn.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.upn.entidades.Mensaje;
import com.upn.entidades.clasificador.Clasificador;
import com.upn.entidades.clasificador.IndividuoTI;
import com.upn.entidades.clasificador.ModelOnto;
import com.upn.entidades.clasificador.PorcSim;
import com.upn.model.Cargo;
import com.upn.model.Conocimiento;
import com.upn.service.CargoService;
import com.upn.service.ConocimientoService;
import com.upn.util.Util;
import com.upn.util.UtilClasificador;

import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/calificador")
public class CalificadorController {
	@Autowired
    ServletContext context;
	
	private static final Logger logger = LoggerFactory.getLogger(CalificadorController.class);
	
	@Autowired
	private CargoService cargoService;
	@Autowired
	private ConocimientoService conocimientoService;
	
	//private String path = "C:/Users/JONATHAN/Desktop/uploadCV/";
	private String path = "/Users/jonathan/Desktop/uploadCV/";

	@RequestMapping(value = "/{cargoId}", method = RequestMethod.GET)
	public String home(Locale locale, Model model,
			@PathVariable int cargoId) {
		logger.info("Welcome Calificador! The client locale is {}.", locale);
		
		Cargo cargo = cargoService.getCargo(cargoId);
		model.addAttribute("cargo", cargo);		
		
		return "calificador";
	}
	
	/**
	 * Upload multiple file using Spring Controller
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody
	String uploadMultipleFileHandler(MultipartHttpServletRequest request, HttpServletResponse response,
			@RequestParam int cargoId) {
		
		Iterator<String> itr =  request.getFileNames();
		
		System.out.println(cargoId);
		
		Cargo cargo = cargoService.getCargo(cargoId);
		
        while(itr.hasNext()){
        	MultipartFile mpf = request.getFile(itr.next());
        	
            try {
            	saveFilesToServer(mpf, String.valueOf(cargo.getnId()));
            }catch(Exception e) {
            	System.out.println(e.getMessage());
            }        	
        	//System.out.println(mpf.getOriginalFilename() +" uploaded!");
        }
		
		return "";
	}
	
	public void saveFilesToServer(MultipartFile mpf,String folder) throws IOException {
		String directory = path + folder+"/";
		File file = new File(directory);
		file.mkdirs();		
		file = new File(directory + mpf.getOriginalFilename());
		IOUtils.copy(mpf.getInputStream(), new FileOutputStream(file));
	}
	
	@RequestMapping(value = "/procesarcv", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	//@RequestMapping(value = "/procesarcv", method = RequestMethod.GET)
	public String procesarCV(
		@RequestParam int cargoid //, Model model
	) throws Exception{
		
		//int cargoid = 1;
		
		ModelOnto mOnto = Util.getOntology();
		
		ArrayList<Path> fileProcess = UtilClasificador.obtenerRutasCV(path + cargoid+"/");
		
		//Encontrar todo los conocimientos necesarios por cargo
		ArrayList<Conocimiento> listSelecConoc = conocimientoService.listarConocPorCargo(cargoid);
		
		System.out.println("Empezar a leer archivos PDFs - cargo: "+cargoid);
		System.out.println("-----------------------------------------------");
		
		ArrayList<Clasificador> arrClasificador = UtilClasificador.clasificar_cv(fileProcess, mOnto, listSelecConoc);
		
		arrClasificador = UtilClasificador.calificar_cv(arrClasificador);
		
		Map<String,Object> data= new HashMap<String,Object>();
		data.put("arrClasificador", arrClasificador);
		
		//msj.setMensaje("Bacanudo!!");
		
        Gson gson = new Gson();
        String json = gson.toJson(arrClasificador);
        
        System.out.println(json);
		
		//model.addAttribute("arrClasificador", arrClasificador);
		
		//return new ModelAndView("partial_view/exc_pv_calificador_cv.exc", "data", data);
		return json;
	}
}