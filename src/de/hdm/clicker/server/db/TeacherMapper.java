package de.hdm.clicker.server.db;


import java.sql.*;
import java.util.Vector;

import de.hdm.clicker.shared.bo.*;

/**
 * Klasse, die Funktionen enth�lt um die gesamten Instanzen der Business Objekte 
 * in relationale Datenb�nke abzubilden. Hierbei k�nnen diese Abbildungen in der Datenbank
 * als Datens�tze angelegt, ver�ndert, gel�scht oder auch als Informationen ausgelesen werden. 
 * 
 * 
 * @see QuizMapper
 * @see QuizCacheMapper
 * @see CategoryMapper
 * @see QuestionMapper
 * @see ResultMapper
 * 
 * @author Moser, Sonntag
 * @version 1.0
 */
public class TeacherMapper {
	
	/**
	 * Die Klasse LecturerMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 */
	private static TeacherMapper lecturerMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit new neue
	 * Instanzen dieser Klasse zu erzeugen.
	 * 
	 */
	protected TeacherMapper(){
		
	}
	
	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>LecturerMapper.lecturerMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>LecturerMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> LecturerMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return DAS <code>LecturerMapper</code>-Objekt.
	 */
	public static TeacherMapper lecturerMapper() {
	    if (lecturerMapper == null) {
	      lecturerMapper = new TeacherMapper();
	    }

	    return lecturerMapper;
	   }
	
	/**
	 * Methode um eine beliebige Anzahl an Lecturer anhand Ihrerer ID's aus der
	 * DB auszulesen
	 * 
	 * @param	keys - Primärschlüsselattribut(e) (->DB)
	 * @return	Vector mit Lecturer, die den Primärschlüsselattributen entsprechen
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Vector<Teacher> findByKey(Vector<Integer> keys) throws RuntimeException {
		StringBuffer ids = new StringBuffer();
		
		//Erstellung des dynamischen Teils des SQL-Querys	
		if (keys.size() > 1) {
			for (int i = 0; i < keys.size()-1; i++) {
			ids.append(keys.elementAt(i));	
			ids.append(",");
			}		
		}
			
		ids.append(keys.elementAt(keys.size()-1));			
			
		//Einholen einer DB-Verbindung		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Teacher> lecturers = new Vector<Teacher>();
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Lecturer WHERE id IN (" + ids.toString() + ") ORDER BY name";
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Lecturer-Vectors"
			while(rs.next()){
				Teacher lecturer = new Teacher();
				lecturer.setId(rs.getInt("id"));
				lecturer.setUser(rs.getString("user"));
				lecturer.setPassword(rs.getString("password"));
				lecturer.setFirstname(rs.getString("firstname"));
				lecturer.getLastname(rs.getString("name"));
				lecturers.add(lecturer);  
	         }
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - dm fbk: " + e1.getMessage());				
		}
		
		return lecturers;
	}
	
	/**
	 * Methode um einen Lecturer anhand eines Logins aus der DB auszulesen
	 * 
	 * @param	user 
	 * 			password
	 * @return	Lecturer-Objekt, des Lecturer, welcher sich angemeldet hat
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Teacher findByLogin(String user, String password) throws RuntimeException {
					
		//Einholen einer DB-Verbindung		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Teacher lecturer = null;
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Lecturer WHERE user = '"+user+"' and password = '"+password+"'";
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Lecturer-Vectors"
			while(rs.next()){
				lecturer = new Teacher();
				lecturer.setId(rs.getInt("id"));
				lecturer.setUser(rs.getString("user"));
				lecturer.setPassword(rs.getString("password"));
				lecturer.setFirstname(rs.getString("firstname"));
				lecturer.getLastname(rs.getString("name")); 
	          }
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - dm fbk: " + e1.getMessage());				
		}
		
		return lecturer;
	}
	
	/**
	 * Methode um einen Lecturer in der DB zu aktualisieren
	 * 
	 * @param	lecturer - Objekt welches aktualisiert werden soll 			
	 * @return	Lecturer-Objekt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Teacher update(Teacher lecturer) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Dozent-Entität in der DB		
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "UPDATE Lecturer SET user='"+lecturer.getUser()+"', password='"+lecturer.getPassword()+"', firstname='"+lecturer.getFirstname()+"', name='"+lecturer.getLastname()+"' WHERE id="+lecturer.getId()+";";
			stmt.executeUpdate(sql);
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - lm.update: " + e1.getMessage());
		}
		
		return lecturer;
	}
	
	/**
	 * Methode um einen neuen Lecturer in die DB zu schreiben
	 * 
	 * @param	dozent - Objekt welcher neu hinzukommt			
	 * @return	Dozent-Objekt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Teacher insertIntoDB(Teacher lecturer) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
						
		try{
			// Ausführung des SQL-Querys	
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO Lecturer (`user`, `password`, `firstname`, `name`) VALUES ('"+lecturer.getUser()+"', '"+lecturer.getPassword()+"', '"+lecturer.getFirstname()+"', '"+lecturer.getLastname()+"');";
			stmt.executeUpdate(sql);
			
			// Auslesen der nach einfügen eines neuen Lecturers in DB entstandenen "größten" ID
			sql = "SELECT MAX(ID) AS maxid FROM Lecturer;";
			rs = stmt.executeQuery(sql);
			
			// Setzen der ID dem hier aktuellen Semesterverband-Objekt
			while(rs.next()){
				lecturer.setId(rs.getInt("maxid"));
			}
					
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem: " + e1.getMessage());
		}
		
		return lecturer;
	}
	
	/**
	 * Methode um alle Lecturer aus der DB auszulesen
	 * 
	 * @return	Vector mit Lecturer
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */	
	public Vector<Teacher> findAll() throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		Vector<Teacher> lecturers = new Vector<Teacher>();
		
		try{		
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Lecturer ORDER BY name;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Teacher lecturer = new Teacher();
				lecturer.setId(rs.getInt("id"));
				lecturer.setUser(rs.getString("user"));
				lecturer.setPassword(rs.getString("password"));
				lecturer.setFirstname(rs.getString("firstname"));
				lecturer.getLastname(rs.getString("name"));
				lecturers.add(lecturer);   
			}
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - lm fa: " + e1.getMessage());		
		}
		
				
		return lecturers;
			
	}
	
	/**
	 * Methode um einen Lecturer aus der DB zu löschen
	 * 
	 * @param	lecturer - Objekt welches gelöscht werden soll
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public void delete(Teacher lecturer) throws RuntimeException {
		
		/* 
		 * Das Löschen eines Lecturers hat das Löschen aller mit ihm
		 * in verbindungstehenden Entitäten anderer Typen zur Folge
		 */
		
		
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			
			// Löschen der Results auf Grundlage der Quizzes des Lecturers			
			String sql = "Delete From `hdm-clicker`.Results where quizid in "
					+ "(Select tmp.quizid from (Select res.quizid from `hdm-clicker`.Results as res "
					+ "inner join `hdm-clicker`.Quiz as qui on res.quizid = qui.id and "
					+ "res.quizversion = qui.version "
					+ "where qui.lecturerid = "+lecturer.getId()+") as tmp)";
			stmt.executeUpdate(sql);
			
			// Löschen Verbindungen zwischen den Questions des Lecturers und den Quizzes
			sql = "Delete From `hdm-clicker`.NMTable_QQ where questionid "
					+ "in (Select q.id from `hdm-clicker`.Question as q "
					+ "inner join `hdm-clicker`.Category as c "
					+ "on c.id = q.categoryid where c.lecturerid = "+lecturer.getId()+");";
			stmt.executeUpdate(sql);
			
			// Löschen der Images der Questions des Lecturers
			sql = "Delete From `hdm-clicker`.Images where id "
					+ "in (Select q.id from `hdm-clicker`.Question as q "
					+ "inner join `hdm-clicker`.Category as c "
					+ "on c.id = q.categoryid where c.lecturerid = "+lecturer.getId()+");";
			stmt.executeUpdate(sql);
			
			// Löschen der Questions des Lecturers
			sql = "Delete From `hdm-clicker`.Question where id in "
					+ "(Select tmp.id from (Select q.id from "
					+ "`hdm-clicker`.Category as cat inner join `hdm-clicker`.Question as q "
					+ "on cat.id = q.categoryid where cat.lecturerid = "+lecturer.getId()+") as tmp)";
			stmt.executeUpdate(sql);
			
			// Löschen der Categories
			sql = "Delete From `hdm-clicker`.Category where lecturerid = "+lecturer.getId()+";";
			stmt.executeUpdate(sql);
			
			// Löschen der Quizze
			sql = "Delete From `hdm-clicker`.Quiz where lecturerid = "+lecturer.getId()+";";
			stmt.executeUpdate(sql);
			
			// Löschen Löschen der Lecturer-Entität			
			sql = "DELETE FROM Lecturer WHERE id = '"+lecturer.getId()+"';";
			stmt.executeUpdate(sql);
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - lm.delete: " + e1.getMessage());
		}
		
	}
	

}