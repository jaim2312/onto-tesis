package com.upn.service;

import java.util.ArrayList;

import com.upn.model.Cargo;
import com.upn.model.Conocimiento;

public interface ConocimientoService {
	public boolean insConocimiento(Conocimiento obj);
	public boolean delConocimiento(Conocimiento obj);
	public ArrayList<Conocimiento> listarConocPorCargo(int cargoid);
}
