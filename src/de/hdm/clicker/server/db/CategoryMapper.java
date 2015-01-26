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
 * @see TeacherMapper
 * @see QuestionMapper
 * @see ResultMapper
 * 
 * @author Moser, Sonntag
 * @version 1.0
 */
public class CategoryMapper {
	
	/**
	 * Da die Klasse CategoryMapper nur einmal instanziiert werden soll und dann 
	 * dieselbe Instanz immer wieder verwendet werden soll wird hier ein Singleton verwendet.
	 * 
	 * 
	 * Die Klasse LecturerMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 */
	private static CategoryMapper categoryMapper = null;
	
	/**
	 * 
	 * Der Modifier protected erlaubt nur der Klasse selber den Konstruktor zu nutzen um Insatnzen zu erzeugen. 
	 * Dadurch wird sichergestellt das nur durch den Aufruf der Klassenmethode <code>categoryMapper </code> eine 
	 * Instanz dieser Klasse erzeugt werden kann
	 */
	protected CategoryMapper(){
		
	}
	
	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>CategoryMapper.categoryMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>LecturerMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> categoryMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return DAS <code>CategoryMapper</code>-Objekt.
	 */
	public static CategoryMapper categoryMapper() {
	    if (categoryMapper == null) {
	    	categoryMapper = new CategoryMapper();
	    }

	    return categoryMapper;
	   }
	
	/**
	 * Methode um eine beliebige Anzahl an Categories anhand Ihrerer ID's aus der
	 * DB auszulesen
	 * 
	 * @param	keys - Primärschlüsselattribut(e) (->DB)
	 * @return	Vector mit Categories, die den Primärschlüsselattributen entsprechen
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Vector<Category> findByKey(Vector<Integer> keys) throws RuntimeException {
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
		Connection connection = DBConnection.connection();
		ResultSet resultset;
		Vector<Category> categories = new Vector<Category>();
		try{
			// Ausführung des SQL-Querys
			Statement statement = connection.createStatement();
			String sql = "SELECT * FROM Category WHERE ID IN (" + ids.toString() + ") ORDER BY description";
			resultset = statement.executeQuery(sql);
			
			// Erstellung des "Lecturer-Vectors"
			while(resultset.next()){
				Category category = new Category();
				category.setId(resultset.getInt("id"));
				category.setDescription(resultset.getString("description"));
				category.setTeacherid(resultset.getInt("lecturerid"));
			}
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm fbk: " + e1.getMessage());				
		}
		
		return categories;
	}
	
	/**
	 * Methode um eine Category in der DB zu aktualisieren
	 * 
	 * @param	category - Objekt welches aktualisiert werden soll 			
	 * @return	Category-Objekt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Category update(Category category) throws RuntimeException {
		Connection con = DBConnection.connection();
		
		// Aktualisierung der Dozent-Entität in der DB		
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "UPDATE Category SET description='"+category.getDescription()+"', lecturerid='"+category.getTeacherid()+"' WHERE id="+category.getId()+";";
			stmt.executeUpdate(sql);
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm.update: " + e1.getMessage());
		}
		
		return category;
	}
	
	/**
	 * Methode um eine neue Category in die DB zu schreiben
	 * 
	 * @param	category - Objekt welcher neu hinzukommt			
	 * @return	Category-Objekt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Category insertIntoDB(Category category) throws RuntimeException {
		Connection con = DBConnection.connection();
		ResultSet rs;
						
		try{
			// Ausführung des SQL-Querys	
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO Category (`description`, `lecturerid`) VALUES ('"+category.getDescription()+"', '"+category.getTeacherid()+"');";
			stmt.executeUpdate(sql);
			
			// Auslesen der nach einfügen einer neuen Category in DB entstandenen "größten" ID
			sql = "SELECT MAX(ID) AS maxid FROM Category;";
			rs = stmt.executeQuery(sql);
			
			// Setzen der ID dem hier aktuellen Category-Objekt
			while(rs.next()){
				category.setId(rs.getInt("maxid"));
			}
					
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm.insert: " + e1.getMessage());
		}
		
		return category;
	}
	
	/**
	 * Methode um alle Categories aus der DB auszulesen
	 * 
	 * @return	Vector mit Categories
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */	
	public Vector<Category> findAll() throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		Vector<Category> categories = new Vector<Category>();
		
		try{		
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Category ORDER BY description;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setDescription(rs.getString("description"));
				category.setTeacherid(rs.getInt("lecturerid"));
				categories.add(category);
			}
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm fa: " + e1.getMessage());		
		}
		
				
		return categories;
			
	}
	
	/**
	 * Methode um alle Categories anhand eines Lecturers aus der DB auszulesen
	 * 
	 * @param	lecturer - Objekt aufgrund dessen ausgeleesnern werden soll
	 * @return	Vector mit Categories
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */	
	public Vector<Category> findAllByLecturer(Teacher lecturer) throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		Vector<Category> categories = new Vector<Category>();
		
		try{		
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Category Where lecturerid = "+lecturer.getId()+" ORDER BY description;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				Category category = new Category();
				category.setId(rs.getInt("id"));
				category.setDescription(rs.getString("description"));
				category.setTeacherid(rs.getInt("lecturerid"));
				categories.add(category);
			}
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm fa: " + e1.getMessage());		
		}
		
				
		return categories;
			
	}
	
	/**
	 * Methode um eine Category aus der DB zu löschen
	 * 
	 * @param	category - Objekt welches gelöscht werden soll
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public void delete(Category category) throws RuntimeException {
		
		/* Das Löschen einer Category hat das Löschen aller Questions
		 * zur Folge, welche dieser Category zugeordnet sind
		 */
		
		
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			
			// Löschen der Questions
			
			// Löschen der Category-Entität
			String sql = "DELETE FROM Category WHERE id = '"+category.getId()+"';";
			stmt.executeUpdate(sql);
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm.delete: " + e1.getMessage());
		}
		
	}
	
	/**
	 * Methode um Categories anhand eines Lecturers zu löschen
	 * 
	 * @param	lecturer - Objekt welches die Grundlage darstellt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public void deleteByLecturer(Category category) throws RuntimeException {
		
		/* Das Löschen einer Category hat das Löschen aller Questions
		 * zur Folge, welche dieser Category zugeordnet sind
		 */
		
		
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
			
			// Löschen der Questions
			
			// Löschen der Category-Entität
			String sql = "DELETE FROM Category WHERE id = '"+category.getId()+"';";
			stmt.executeUpdate(sql);
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - cm.delete: " + e1.getMessage());
		}
		
	}

}