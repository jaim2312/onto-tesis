package com.upn.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.upn.model.Cargo;
import com.upn.model.Conocimiento;

@Repository
public class ConocimientoServiceImpl implements ConocimientoService {
	
	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}	

	@Override
	public boolean insConocimiento(Conocimiento obj) {
		// TODO Auto-generated method stub
		String query = "INSERT INTO conocimiento(sNombre, sURI, cargo_id) values (?,?,?)";
		Connection con = null;
		PreparedStatement ps = null;
		boolean bExito = false;
		
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, obj.getsNombre() );
			ps.setString(2, obj.getsURI() );
			ps.setInt(3, obj.getCargo().getnId() );
			
			bExito = ( ps.executeUpdate() != 0);			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return bExito;
	}
	
	@Override
	public boolean delConocimiento(Conocimiento obj) {
		// TODO Auto-generated method stub
		String query = "DELETE FROM conocimiento WHERE sURI = ? AND cargo_id = ?";
		Connection con = null;
		PreparedStatement ps = null;
		boolean bExito = false;
		
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1,obj.getsURI());
			ps.setInt(2,obj.getCargo().getnId());
			
			bExito = ( ps.executeUpdate() != 0);			
		}catch(SQLException e){
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return bExito;
	}	

	public ArrayList<Conocimiento> listarConocPorCargo(int cargoid) {
		// TODO Auto-generated method stub
		String query = "SELECT nId,sNombre,sURI,cargo_id FROM conocimiento WHERE cargo_id = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Conocimiento> listado = null;
		
		try{
			listado = new ArrayList<Conocimiento>();
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, cargoid );
						
			rs = ps.executeQuery();
			
			while (rs.next())	{
				Conocimiento c = new Conocimiento();
				c.setnId(rs.getInt("nId"));
				c.setsNombre(rs.getString("sNombre"));
				c.setsURI(rs.getString("sURI"));
				
				Cargo car = new Cargo();
				car.setnId(rs.getInt("cargo_id"));				
				c.setCargo(car);
				
				listado.add(c);
			}
						
		}catch(SQLException e){
			System.out.println(e.getMessage());
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return listado;
	}

}
