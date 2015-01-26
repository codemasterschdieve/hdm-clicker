package hdm.wi.clicker.server;

import hdm.wi.clicker.server.db.DBConnection;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DownloadBildImpl extends HttpServlet{
	
	public static byte[] bbb = null;
	
	Blob bildblob = null;
	
	String bildid = null;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
		
		
		bildid =  request.getParameter("textBoxFormElement");
        bildblob = selectBlob();
                
        
        
        byte[] imageData = null;
		try {
			imageData = bildblob.getBytes(1, (int)bildblob.length());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			
		}
		response.setContentType("image/png");
		
		
         try {
			response.getOutputStream().write(imageData,0,imageData.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
	}
	
	public Blob selectBlob() {
		Connection con = DBConnection.connection();
		ResultSet rs = null;
		
		
		try{
			// Ausf�hren des SQL-Statements
			Statement stm = con.createStatement();
			String sql = "Select Image from `hdm-clicker`.Images Where id = " + bildid;
			rs = stm.executeQuery(sql);
			
			if (rs.next()) {
				bildblob = rs.getBlob("Image");
			}
							
		}		
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem: " + e1.getMessage());
		}
		return bildblob;
	}
}
