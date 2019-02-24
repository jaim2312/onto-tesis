package com.upn.controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.upn.model.Cargo;
import com.upn.service.CargoService;

@Controller
public class WelcomeController {

	@Autowired
    private ServletContext servletContext;	
	
	@Autowired
	private CargoService cargoService;
	
	@RequestMapping("/hello")
	public String sayHi(
	@RequestParam(name = "name", required = false, defaultValue = "") 
	String name, Model model) {
		
		System.out.println("version: " + SpringVersion.getVersion());
		System.out.println(servletContext.getContextPath());
		
		Cargo c = cargoService.getCargo(7);
		
		System.out.println(c.getsNombre());
		
        model.addAttribute("message", name);

        return "welcome"; //view
        
	}
}