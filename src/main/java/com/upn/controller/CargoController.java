package com.upn.controller;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.upn.entidades.DtTable;
import com.upn.model.Cargo;
import com.upn.service.CargoService;

@Controller
@RequestMapping("/cargo")
public class CargoController {
	
	/*@Autowired
	private Environment env;*/
	@Value("${app.baseURL}")
    private String baseURL;
	
	@Autowired
    private ServletContext servletContext;	
	
	@Autowired
	private CargoService cargoService;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String cargo(Locale locale, Model model, HttpServletRequest request) {
		logger.info("Welcome cargo! The client locale is {}.", locale);
		
		System.out.println("version: " + SpringVersion.getVersion());
		System.out.println(servletContext.getContextPath());
		
		/*CargoDAO cargoDao = new CargoDAOImpl();
		System.out.println( cargoDao.getListCargo() );*/
		
		/*List<Cargo> listado =  cargoDAOImpl.getListCargo();
		
		if(listado.size() > 0){
			for(Cargo obj : listado){
				System.out.println(obj.getsNombre());
			}
		}*/
		
	    //Try this:
	    System.out.println(baseURL); 
	    // or this
	    //System.out.println(request.getR );
		
		return "cargo";
	}		
	
	@RequestMapping(value = "guardar", method = RequestMethod.POST)
	@ResponseBody
	public String guardarCargo(
		@RequestParam int id, @RequestParam String nombre
	){
		
		Cargo obj = new Cargo();
		obj.setnId(id);		
		obj.setsNombre(nombre);
		
		if(obj.getnId() == 0){			
			cargoService.insCargo(obj);
		}else{
			cargoService.updCargo(obj);
		}
		
		return "";
	}
	
	@RequestMapping(value = "dt_QueCargo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dt_QueCargo(
		@RequestParam int draw, @RequestParam int start,
		@RequestParam int length
	){		
		
		List<Cargo> listado = cargoService.queCargo(length, start);
		int total = cargoService.total_QueCargo();
		
		DtTable dtTable = new DtTable();
		List<Object> data = new ArrayList<Object>();
		
		for(Cargo item : listado){
			List<String> row = new ArrayList<String>(); 
			row.add(item.getsNombre());
			row.add("<div class='card-block icon-btn button-page '>"					
					+ "<div>"
                    + "<a href='javascript:Cargo.edit_cargo("+item.getnId()+");' class='btn btn-primary btn-icon'><i class='icofont icofont-ui-edit'></i></a>"
                    + "<a href='/"+item.getnId()+"' target='_blank' class='btn btn-warning btn-icon'><i class='icofont icofont-warning-alt'></i></a>"
                    + "<a href='/calificador/"+item.getnId()+"' target='_blank' class='btn btn-inverse btn-icon'><i class='icofont icofont-exchange'></i></a>"
                    + "</div>"
                    + "</div>");
					
					//+ "<div><a href='javascript:Cargo.edit_cargo("+item.getnId()+");' class='btn btn-primary btn-icon'><i class='icofont icofont-user-alt-3'></i></a>"
					//+ "<a href='"+baseURL+item.getnId()+"' target='_blank' class='btn btn-warning btn-icon'><i class='icofont icofont-warning-alt'></i></a></div>"
					//+ "</div>");
			data.add(row);
		}
		
		dtTable.setDraw(draw);
		dtTable.setRecordsTotal(total);
		dtTable.setRecordsFiltered(total);
		dtTable.setData(data);
		
        Gson gson = new Gson();
        String json = gson.toJson(dtTable);
        
        System.out.println(json);
		
		return json;
	}
	
	@RequestMapping(value = "getCargo", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String getCargo(
		@RequestParam int id
	){		
		
		Cargo cargo = cargoService.getCargo(id);
		
        Gson gson = new Gson();
        String json = gson.toJson(cargo);
        
        System.out.println(json);
		
		return json;
	}	
	
}
