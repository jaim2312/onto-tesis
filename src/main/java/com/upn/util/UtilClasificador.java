package com.upn.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.upn.entidades.clasificador.Clasificador;
import com.upn.entidades.clasificador.IndividuoTI;
import com.upn.entidades.clasificador.ModelOnto;
import com.upn.entidades.clasificador.PorcSim;
import com.upn.model.Conocimiento;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class UtilClasificador {
	
	private static double porc_error = 0.9;
	private static double porc_error_menor = 0.85;
	
	public static String /*ArrayList<Integer>*/ stringToASCII(String text) {
		
		// converting String to ASCII value in Java		
		try { 
			
			String cad_limpia = "";
			
			int[] ascii_no_autorizados = new int[]{63,40,41};			
			byte[] bytes = text.getBytes("US-ASCII");
						
			ArrayList<Byte> bytesToArray = asciiBytesToArray(bytes);			
			ArrayList<Integer> positions = new ArrayList<Integer>();
			
			for(int i = 0 ; i < ascii_no_autorizados.length; i++) {				
				// Identificar en que posiciones se encuentra el caracter no autorizado
				for(int j = 0; j < bytes.length; j++){						
					if( bytes[j] == ascii_no_autorizados[i] ){
						positions.add(j);
					}
				}				
			}
									
			// Crear cadena sin caracteres no permitidos
			for (int x = 0; x < text.length(); x++) {
				
				if( !positions.contains(x) ){
					cad_limpia = cad_limpia + text.charAt(x);
				}
			}
				   
			// return positions;
			return cad_limpia.trim();
			
		} catch (java.io.UnsupportedEncodingException e) { 
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String removeCharAt(String s, int pos) {
		return s.substring(0, pos) + s.substring(pos + 1);
	}
	
	public static ArrayList<Byte> asciiBytesToArray( byte[] bytes ) {
		
		if ( (bytes == null) || (bytes.length == 0) ) return new ArrayList<Byte>();
	  	  	  
		ArrayList<Byte> listado = new ArrayList<Byte>();  
		
		for ( int i = 0; i < bytes.length; i++ ) {
			listado.add(new Byte(bytes[i]));
		}
	  
		return listado;
	}
	
	public static String stripAccents(String cad)
	{
	    cad = Normalizer.normalize(cad, Normalizer.Form.NFD);
	    cad = cad.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	    return cad;
	}
	
	public static String removeFinalDot(String cad) {
		if(cad.endsWith(".")) {
			int cantidad = 1; /* Total de elementos a Eliminar*/  
			/* Total de elementos a Mostrar*/      
			int m = Math.max(0, cad.length() - cantidad);
			StringBuilder sb = new StringBuilder(cad);
			sb.setLength(m);		
			
			cad = sb.toString();
		}
		return cad;
	}
	
	public static String moreSpaceToOne(String cad){
		return cad.replaceAll("\\s+", " ");
	}
	
	public static String[] arrWords(String cad){
		return cad.split("\\s+|\n");
	}
	
	public static String stripUnderscore(String cad) {
		return cad.replace("_"," ");
	}
	
	public static ArrayList<IndividuoTI> getIndividuoTIByModel(OntModel model,ArrayList<Conocimiento> listSelecConoc){
		
		ArrayList<IndividuoTI> individuosTI = new ArrayList<IndividuoTI>();
		
		Iterator lstIndividuos = model.listIndividuals();
		while(lstIndividuos.hasNext()){
			Individual individuo = (Individual) lstIndividuos.next();
			
			Boolean bEncontrado = Util.bEncNodo(individuo, listSelecConoc);
			
			if(bEncontrado){
			
				IndividuoTI IndTmp = new IndividuoTI();
				
				// Listo para realizar la comparación de cadenas
				String descripcion_tmp = stripUnderscore(individuo.getLocalName()).toLowerCase();
				
				if(!descripcion_tmp.trim().equals("")){			
					IndTmp.setIndividuo(individuo);
					
					IndTmp.setDescripcion(descripcion_tmp);
					IndTmp.setLongitud(descripcion_tmp.length());
					
					individuosTI.add(IndTmp);
				}
			}
		}
		
		return individuosTI;
	}
	
	public static String getNumWordByN(String[] arr,int desde, int n){
		//String[] arr = arrWords(cad);
		String result = null;
		
		if((desde+n-1) <= arr.length){
			result = "";
			for(int i=0;i<n;i++){
				result = result + arr[i+desde-1] + " ";
			}
			result = result.trim();
		}
		
		return result; 
	}

	public static ArrayList<Path> obtenerRutasCV(String sRuta){
		// Empezar a leer archivos PDFs
		ArrayList<Path> fileProcess = fileProcess = new ArrayList<Path>();
		
		try {			
			Files.walk(Paths.get(sRuta)).forEach(ruta-> {
			    if (Files.isRegularFile(ruta)) {
			    	//Filtrar archivo por extensión
			    	Pattern patron = Pattern.compile("^.*(pdf|PDF)$");
			        Matcher match = patron.matcher(ruta.toString());
			        
			        if(match.matches()) fileProcess.add(ruta);
			    }
			});
		} catch (IOException ex) {
			System.out.println("Error leyendo cvs:"+ex.getMessage());
		} finally{
			return fileProcess;
		}		
	}
	
	public static ArrayList<Clasificador> clasificar_cv(ArrayList<Path> fileProcess, ModelOnto mOnto, ArrayList<Conocimiento> listSelecConoc){
		
		ArrayList<Clasificador> lstClasifidosCV = new ArrayList<Clasificador>();		
		
		for(Path cvActual : fileProcess){
			try {
				File file = cvActual.toFile();
				
		        PDDocument document = PDDocument.load(file);
		        PDFTextStripper pdfStripper = new PDFTextStripper();	
		        String salto = System.getProperty("line.separator");		        
		        String text = pdfStripper.getText(document);
		        document.close();
		        
		        String[] lines = text.split(salto);
		        
		        //Listar de individuos a evaluar
		        ArrayList<IndividuoTI> individuos = UtilClasificador.getIndividuoTIByModel(mOnto.getModelo(),listSelecConoc);
	
		        ArrayList<PorcSim> arrIndividuoEncont = new ArrayList<PorcSim>();
		        
		        int numLine = 0;
		        for(String line : lines){
		        	if(line.trim().length() > 0) {
		        		numLine++;
		        		String linePrepocesada = removeFinalDot(stripAccents(line.trim().toLowerCase()));
		        		String lineClean = UtilClasificador.moreSpaceToOne( UtilClasificador.stringToASCII(linePrepocesada) );		        		
		        		
		        		for(IndividuoTI item : individuos){
		        			if(item.isbEncontrado() != true){			        				
			        			// Recorriendo todos los individuos de la Ontología
			        			java.util.List<String> ind_sinonimos = new ArrayList<String>();
			        			
			        			// Obteniendo los sinonimos del individuo
			        			StmtIterator props = item.getIndividuo().listProperties();
			        			while (props.hasNext()) {
			        				Statement s = props.next();
	
			                        if (s.getObject().isLiteral() && s.getPredicate().getLocalName().equals("sinonimo") ) {
			                        	// Sinónimos listos para comparar
			                        	String[] sinonimos = s.getLiteral().getLexicalForm().toLowerCase().split(",");
			                        	ind_sinonimos.addAll(Arrays.asList(sinonimos));
			                        	break;
			                        }   				
			        			}
			        			
			        			ind_sinonimos.add(item.getDescripcion());
			        			
			        			searchOntoLoop:
			        			// Recorro al individuo con todo sus sinonimos
			        			for(String ind_sin : ind_sinonimos){
			        				// Verifico cuantas palabras tiene el sinónimos
			        				String[] sinoni_palab = UtilClasificador.arrWords(ind_sin);
			        				
			        				if(sinoni_palab.length > 1){
			        					
			        					//System.out.println(ind_sin);

			        					String[] line_palab = UtilClasificador.arrWords(lineClean);
			        					
			        					for(int i=0;i<line_palab.length-sinoni_palab.length+1;i++){
			        						
			        						String subcadena = UtilClasificador.getNumWordByN(line_palab,i+1,sinoni_palab.length);
			        						
			        						JaroWinkler jw = new JaroWinkler();
											double porcSim = jw.similarity(ind_sin, subcadena);								
											
											if( porcSim >= porc_error ){
					    										    			
								    			PorcSim sim = new PorcSim();
								    			//sim.setIndividuo(item);
								    			sim.setNumLinea(numLine);
								    			sim.setDescLinea(lineClean);
								    			sim.setSubCadena(subcadena);
								    			sim.setDescOnto(ind_sin);
								    			sim.setPorcentaje(porcSim);
								    			
								    			//Individuo encontrado
								    			item.setbEncontrado(true);
								    			
								    			arrIndividuoEncont.add(sim);
								    			break searchOntoLoop;
											}
			        						
			        					}				        					
			        					
			        				}else{
			        					// Obtener todas las palabras de la cadena y compararlas con el sinonimo
			        					String[] line_palab = UtilClasificador.arrWords(lineClean);				
			        					
			        					for(String palabra : line_palab){				        						
			        						
			        						JaroWinkler jw = new JaroWinkler();									
											double porcSim = jw.similarity(ind_sin, palabra);
											
											//System.out.println(ind_sin + " - " + palabra + " - " + porcSim);
											
											if(	(palabra.length() > 7 && porcSim >= porc_error) ||
												(palabra.length() <= 7 && porcSim >= porc_error_menor) ){
												
												//System.out.println("Onto: "+ ind_sin + " - Cadena Enc.: " + palabra + "PS: " + porcSim);
					    										    			
								    			PorcSim sim = new PorcSim();
								    			sim.setNumLinea(numLine);
								    			sim.setDescLinea(lineClean);
								    			sim.setSubCadena(palabra);
								    			sim.setDescOnto(ind_sin);
								    			sim.setPorcentaje(porcSim);
								    			
								    			//Individuo encontrado
								    			item.setbEncontrado(true);
								    			
								    			arrIndividuoEncont.add(sim);
								    			break searchOntoLoop;
											}			        						
			        					}
			        				}				        								        				
				        			
			        			}
							}
		        		}
		        	}
		        }
		        
		        Clasificador clasificado = new Clasificador();
		        clasificado.setFileNameCV(cvActual.getFileName().toString());
		        clasificado.setArrIndividuoEnc(arrIndividuoEncont);
		        
		        for(PorcSim obj : arrIndividuoEncont){
		        	System.out.println(cvActual.getFileName().toString() + " | " + obj.getDescOnto() + " | " + obj.getSubCadena() + " | " + obj.getPorcentaje() + " | " + obj.getNumLinea());
		        }
		        
		        lstClasifidosCV.add(clasificado);
			} catch (Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
		
		return lstClasifidosCV;
	}
	
	public static ArrayList<Clasificador> calificar_cv(ArrayList<Clasificador> arrClasificador){		
		Collections.sort(arrClasificador, Clasificador.CalificadorSizeIndEnc);
		
		for (Clasificador clasificado : arrClasificador) {
			System.out.println("-----------------------------");
		    //System.out.println(cla.getCv());
		    System.out.println(clasificado.getArrIndividuoEnc().size());
		}
		
		return arrClasificador;
	}
}
