package com.upn.entidades.clasificador;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

public class Clasificador {
	private String fileNameCV;
	private ArrayList<PorcSim> arrIndividuoEnc;
	
	public String getFileNameCV() {
		return fileNameCV;
	}
	public void setFileNameCV(String fileNameCV) {
		this.fileNameCV = fileNameCV;
	}
	public ArrayList<PorcSim> getArrIndividuoEnc() {
		return arrIndividuoEnc;
	}
	public void setArrIndividuoEnc(ArrayList<PorcSim> arrIndividuoEnc) {
		this.arrIndividuoEnc = arrIndividuoEnc;
	}
	
    /*Comparator for sorting the list by roll no*/
    public static Comparator<Clasificador> CalificadorSizeIndEnc = new Comparator<Clasificador>() {

	public int compare(Clasificador a, Clasificador b) {

	   int sizeIndEnc1 = a.getArrIndividuoEnc().size();
	   int sizeIndEnc2 = b.getArrIndividuoEnc().size();

	   /*For ascending order*/
	   return sizeIndEnc2 - sizeIndEnc1;

	   /*For descending order*/
	   //rollno2-rollno1;
   }};	
}