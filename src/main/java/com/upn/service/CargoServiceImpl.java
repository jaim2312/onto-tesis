package com.upn.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.upn.model.Cargo;
import com.upn.model.Conocimiento;

@Repository
public class CargoServiceImpl implements CargoService {

	private DataSource dataSource;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public boolean insCargo(Cargo obj) {
		String query = "insert into cargo (sNombre) values (?)";
		Connection con = null;
		PreparedStatement ps = null;
		
		boolean exito = false;
		
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, obj.getsNombre());
			int out = ps.executeUpdate();
			
			exito = (out != 0);
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return exito;
	}
	
	public boolean updCargo(Cargo obj) {
		String query = "update cargo SET sNombre = ? WHERE nId = ?";
		Connection con = null;
		PreparedStatement ps = null;
		
		boolean exito = false;
		
		try{
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setString(1, obj.getsNombre());
			ps.setInt(2, obj.getnId());
			int out = ps.executeUpdate();
			
			exito = (out != 0);
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				ps.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return exito;		
	}
	
	public List<Cargo> queCargo(int length,int start){
		List<Cargo> listado = new ArrayList<Cargo>();
		Connection con = null;
		//PreparedStatement ps = null;
		CallableStatement cStmt = null;
		ResultSet rs = null;
		try{
			con = dataSource.getConnection();
			cStmt = con.prepareCall("{call pa_QueCargo(?, ?)}");
			cStmt.setInt(1, length);
			cStmt.setInt(2, start);
			rs = cStmt.executeQuery();
			while(rs.next()){
				Cargo obj = new Cargo();
				obj.setnId(rs.getInt("nId"));
				obj.setsNombre(rs.getString("sNombre"));
				listado.add(obj);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				cStmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return listado;		
	}
	
	public int total_QueCargo(){
		Connection con = null;
		CallableStatement cStmt = null;
		ResultSet rs = null;
		int total = 0;
		try{
			con = dataSource.getConnection();
			cStmt = con.prepareCall("{call pa_Total_QueCargo()}");
			rs = cStmt.executeQuery();
			if(rs.next()){
				total = rs.getInt("Total");
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				cStmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return total;		
	}

	public Cargo getCargo(int id){
		// TODO Auto-generated method stub
		String query = "SELECT nId,sNombre FROM cargo WHERE nId = ?";
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Cargo cargo = null;
		
		try{
			cargo = new Cargo();
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			ps.setInt(1, id );
						
			rs = ps.executeQuery();
			
			if (rs.next())	{
				cargo.setnId(rs.getInt("nId"));
				cargo.setsNombre(rs.getString("sNombre"));
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
		
		return cargo;		
	}
}
