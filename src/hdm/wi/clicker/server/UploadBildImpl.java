package hdm.wi.clicker.server;


import hdm.wi.clicker.server.db.DBConnection;
import hdm.wi.clicker.server.db.QuestionMapper;
import hdm.wi.clicker.shared.bo.Question;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class UploadBildImpl extends HttpServlet {

	public QuestionMapper questionMapper = QuestionMapper.questionMapper();
	
	byte[] buffer;
	int bildid;
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();
		try {
			FileItemIterator iter = upload.getItemIterator(request);

				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					InputStream stream = item.openStream();	
					
					if (item.isFormField()) {
						byte[] str = new byte[stream.available()];
			            stream.read(str);
			            String pFieldValue = new String(str,"UTF8");
			            bildid = new Integer(pFieldValue);
					}
					else {
						byte[] bb = IOUtils.toByteArray(stream);						
						buffer = bb;
					}
					
				}
				
				insertIntoDB();
				
			} catch (Exception e) {
				throw new RuntimeException(e);
		}

	}
	
	public void insertIntoDB () throws RuntimeException {
		
		//erst wird das alte Bild gelöscht damit keine fehler entstehen kann
		Vector<Integer> vi = new Vector<Integer>();
		vi.add(bildid);
		Question ques2Update = questionMapper.findByKey(vi).elementAt(0);
		questionMapper.deleteImage(ques2Update);
		
		Connection con = DBConnection.connection();
		
		try{
			// SQL-Statements Ausführen
			//Statement stmt = con.createStatement();
			String sql = "INSERT INTO Images (id, image) VALUES (?, ?);";
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, bildid);
			pstmt.setBytes(2, buffer);
			pstmt.executeUpdate();
			
			//Aktualisieren der Fragen
			ques2Update.setImage(true);
			questionMapper.update(ques2Update);
							
		}		
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem: " + e1.getMessage());
		}
						
	}
	
}
