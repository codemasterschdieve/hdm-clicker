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
public class DownloadBildPlayQuizImpl extends HttpServlet{
	
	boolean flag = false;
	public static byte[] bbb = null;
	Blob blob = null;
	
	TempDatenbank tempDB = null;
	byte[] imageData = null;
	
	String bildid = null;
	int quizid;
	int fragenid;
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		bildid =  request.getParameter("textBoxFormElement");
        
        if (bildid != null) {
        	quizid = Integer.valueOf(bildid.substring(0,bildid.lastIndexOf(",")));
			fragenid = Integer.valueOf(bildid.substring(bildid.lastIndexOf(",") + 1));
			tempDB = TempDatenbank.get();
			for (QuizPackage qp : TempDatenbank.getQpv()) {
				if (qp.getId() == quizid) {
					blob = qp.getImages().get(fragenid);
					flag = true;
					break;
				}
			}
        }
        
        if (!flag) {
			blob = selectBlob();
			flag = false;
		}
		byte[] imageData = null;
		try {
			imageData = blob.getBytes(1, (int)blob.length());
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
			// SQL-Statements Ausführung
			Statement stmt = con.createStatement();
			String sql = "Select Image from `hdm-clicker`.Images Where id = " + fragenid;
			rs = stmt.executeQuery(sql);
			
			if (rs.next()) {
             blob = rs.getBlob("Image");
			}
							
		}		
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem: " + e1.getMessage());
		}
		return blob;
	}
}
