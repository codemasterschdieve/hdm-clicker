package hdm.wi.clicker.server.db;


import java.sql.*;
import java.util.Vector;

import hdm.wi.clicker.shared.bo.*;

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
 * @see CategoryMapper
 * 
 * @author Moser, Sonntag
 * @version 1.0
 */
public class ResultMapper {
	
	/**
	 * Die Klasse ResultsMapper wird nur einmal instantiiert. Man spricht hierbei
	 * von einem sogenannten <b>Singleton</b>.
	 * <p>
	 * Diese Variable ist durch den Bezeichner <code>static</code> nur einmal für
	 * sämtliche eventuellen Instanzen dieser Klasse vorhanden. Sie speichert die
	 * einzige Instanz dieser Klasse.
	 */
	private static ResultMapper ResultsMapper = null;
	
	/**
	 * Geschützter Konstruktor - verhindert die Möglichkeit, mit new neue
	 * Instanzen dieser Klasse zu erzeugen.
	 * 
	 */
	protected ResultMapper(){
		
	}
	
	/**
	 * Diese statische Methode kann aufgrufen werden durch
	 * <code>ResultsMapper.resultsMapper()</code>. Sie stellt die
	 * Singleton-Eigenschaft sicher, indem Sie dafür sorgt, dass nur eine einzige
	 * Instanz von <code>QuestionMapper</code> existiert.
	 * <p>
	 * 
	 * <b>Fazit:</b> ResultsMapper sollte nicht mittels <code>new</code>
	 * instantiiert werden, sondern stets durch Aufruf dieser statischen Methode.
	 * 
	 * @return DAS <code>ResultsMapper</code>-Objekt.
	 */
	public static ResultMapper resultsMapper() {
	    if (ResultsMapper == null) {
	    	ResultsMapper = new ResultMapper();
	    }

	    return ResultsMapper;
	   }
	
	/**
	 * Methode um eine beliebige Anzahl an Results anhand Ihrerer ID's aus der
	 * DB auszulesen
	 * 
	 * @param	keys - Primärschlüsselattribut(e) (->DB)
	 * @return	Vector mit Results, die den Primärschlüsselattributen entsprechen
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Vector<Result> findByQuiz(int quizID, int quizVersion) throws RuntimeException {
					
		//Einholen einer DB-Verbindung		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<Result> results = new Vector<Result>();
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Results WHERE quizid="+quizID+" AND quizversion="+quizVersion+" ORDER BY hdmuser";
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Results-Vectors"
			while(rs.next()){
				Result result = new Result();
				result.setId(rs.getInt("id"));
				result.setAnswerNo(rs.getInt("answerno"));
				if (rs.getInt("answered") == 1) {
					result.setAnswered(true);
				} else {
					result.setAnswered(false);
				}
				result.setHdmUser(rs.getString("hdmuser"));
				result.setQuestionID(rs.getInt("questionid"));
				result.setQuizID(rs.getInt("quizid"));
				result.setQuizVersion(rs.getInt("quizversion"));
				if (rs.getInt("successed") == 1) {
					result.setSuccessed(true);;
				} else {
					result.setSuccessed(false);
				}
				results.add(result);
			}
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm fbk: " + e1.getMessage());				
		}
		
		return results;
	}
	
	/**
	 * Methode um Results anhand eines Quizzes aus der DB auszulesen
	 * 
	 * @param	Quiz -Objekt
	 * @return	Vector mit Results, die den Primärschlüsselattributen entsprechen
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Vector<InfoGraph> findByQuizReport(Quiz quiz) throws RuntimeException {
					
		//Einholen einer DB-Verbindung		
		Connection con = DBConnection.connection();
		ResultSet rs;
		Vector<InfoGraph> cis = new Vector<InfoGraph>();
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM `hdm-clicker`.Results AS results INNER JOIN `hdm-clicker`.Question AS ques ON results.questionid = ques.id WHERE results.quizid="+quiz.getId()+" AND results.quizversion="+quiz.getVersion()+" ORDER BY results.questionid;";
			rs = stmt.executeQuery(sql);
			
			InfoGraph ci = null;;
			boolean firstIt = true;
			
			int quesId = 0;
			
			int wrongs = 0;
			int corrects = 0;
			String quesBody = null;
			
			// Erstellung des "Results-Vectors"
			while(rs.next()){
				
				if (firstIt || rs.getInt("questionid") == quesId) {
					firstIt = false;
					if (rs.getInt("successed") == 1) {
						corrects++;
					} 
					else {
						wrongs++;
					}
					quesBody = rs.getString("questionbody");
				}
				else {
					ci = new InfoGraph();
					ci.setQuestionText(quesBody);
					ci.setCorrectAnswers(corrects);
					ci.setWrongAnswers(wrongs);
					cis.add(ci);
					
					corrects = 0;
					wrongs = 0;
					quesBody = null;
					
					if (rs.getInt("successed") == 1) {
						corrects++;
					} 
					else {
						wrongs++;
					}
					quesBody = rs.getString("questionbody");
					
				}
				
				quesId = rs.getInt("questionid");
				
			}
			ci = new InfoGraph();
			ci.setQuestionText(quesBody);
			ci.setCorrectAnswers(corrects);
			ci.setWrongAnswers(wrongs);
			cis.add(ci);
			
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm fbk: " + e1.getMessage());				
		}
		
		return cis;
	}
	
	/**
	 * Methode um die Anzahl der Results aus der DB zu lesen, die in relation
	 * zu der entsprechenden Question stehen
	 * 
	 * @param	question - Question-Objekt
	 * @return	int - Repräsentation der Anzahl
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public int countByQuestion(Question question) throws RuntimeException {
					
		//Einholen einer DB-Verbindung		
		Connection con = DBConnection.connection();
		ResultSet rs;
		int anzahl = 0;
		try{
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT count(*) as count FROM `hdm-clicker`.Results where questionid =" + question.getId();
			rs = stmt.executeQuery(sql);
			
			// Erstellung des "Results-Vectors"
			while(rs.next()){
				anzahl = rs.getInt("count");
			}
						
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm cbq: " + e1.getMessage());				
		}
		
		return anzahl;
	}
	
	/**
	 * Methode um ein neues Result in die DB zu schreiben
	 * 
	 * @param	results - Objekt welcher neu hinzukommt			
	 * @return	Results-Objekt
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Result insertIntoDB(Result result) throws RuntimeException {
		int answered;
		int successed;
		
		if (result.isAnswered()) {
			answered = 1;
		} else {
			answered = 0;
		}
		
		if (result.isSuccessed()) {
			successed = 1;
		} else {
			successed = 0;
		}
		
				
		Connection con = DBConnection.connection();
						
		try{
			// Ausführung des SQL-Querys	
			Statement stmt = con.createStatement();
			String sql = "INSERT INTO Results (`answerno`, `answered`, `hdmuser`, `questionid`, "
					+ "`quizid`, `quizversion`, `successed`) "
					+ "VALUES ('"+result.getAnswerNo()+"', '"+answered+"', '"+result.getHdmUser()+"', '"+result.getQuestionID()
					+"', '"+result.getQuizID()+"', '"+result.getQuizVersion()+"', '"+successed+"');";
			stmt.executeUpdate(sql);
			
					
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm.insert: " + e1.getMessage());
		}
		
		return result;
	}
	
	/**
	 * Methode um neue Results in die DB zu schreiben
	 * 		
	 * @return	Void
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public Vector<Boolean> insertMultipleIntoDB(Vector<Result> vr) throws RuntimeException {
		Vector<Boolean> vb = new Vector<Boolean>();
		Connection con = DBConnection.connection();
		String sql = null;
		
		int answered;
		int successed;
		
		if (vr.elementAt(0).isAnswered()) {
			answered = 1;
		} else {
			answered = 0;
		}
		
		if (vr.elementAt(0).isSuccessed()) {
			successed = 1;
		} else {
			successed = 0;
		}
		
		if (vr.elementAt(0).getAnswerNo() != 1) {
			vb.add(false);
		}
		else {
			vb.add(true);
		}
		
		// Aktualisierung der Dozent-Entität in der DB		
		try{			
			// Neu Aufsetzen der N:M-Beziehung
			if (vr != null && vr.size() > 0) {
				Statement stmt = con.createStatement();
				sql = "INSERT INTO Results (`answerno`, `answered`, `hdmuser`, `questionid`, "
						+ "`quizid`, `quizversion`, `successed`) "
						+ "VALUES ('"+vr.elementAt(0).getAnswerNo()+"', '"+answered+"', '"+vr.elementAt(0).getHdmUser()+"', '"+vr.elementAt(0).getQuestionID()
						+"', '"+vr.elementAt(0).getQuizID()+"', '"+vr.elementAt(0).getQuizVersion()+"', '"+successed+"')";
				
				for(int i = 1; i < vr.size(); i++) {
					if (vr.elementAt(i).isAnswered()) {
						answered = 1;
					} else {
						answered = 0;
					}
					
					if (vr.elementAt(i).isSuccessed()) {
						successed = 1;
					} else {
						successed = 0;
					}
					
					if (vr.elementAt(i).getAnswerNo() != 1) {
						vb.add(false);
					}
					else {
						vb.add(true);
					}
					
					sql = sql + " ,('"+vr.elementAt(i).getAnswerNo()+"', '"+answered+"', '"+vr.elementAt(i).getHdmUser()+"', '"+vr.elementAt(i).getQuestionID()
						+"', '"+vr.elementAt(i).getQuizID()+"', '"+vr.elementAt(i).getQuizVersion()+"', '"+successed+"')";
				}
				stmt.executeUpdate(sql);
			}
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - resultm.insertmultiple: " + e1.getMessage());
		}
		
		return vb;
	}
	
	/**
	 * Methode um alle Results aus der DB auszulesen
	 * 
	 * @return	Vector mit Results
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */	
	public Vector<Result> findAll() throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		Vector<Result> results = new Vector<Result>();
		
		try{		
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM Results ORDER BY quizid, quizversion;";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()){
				Result result = new Result();
				result.setId(rs.getInt("id"));
				result.setAnswerNo(rs.getInt("answerno"));
				if (rs.getInt("answered") == 1) {
					result.setAnswered(true);
				} else {
					result.setAnswered(false);
				}
				result.setHdmUser(rs.getString("hdmuser"));
				result.setQuestionID(rs.getInt("questionid"));
				result.setQuizID(rs.getInt("quizid"));
				result.setQuizVersion(rs.getInt("quizversion"));
				if (rs.getInt("successed") == 1) {
					result.setSuccessed(true);;
				} else {
					result.setSuccessed(false);
				}
				results.add(result);
			}
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm fa: " + e1.getMessage());		
		}
		
				
		return results;
			
	}
	
	/**
	 * Methode um alle Results zu einem Quiz in String-Form aus der DB auszulesen
	 * 
	 * @param	quiz -Objekt
	 * @return	Vector mit Results
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */	
	public String getCSVStringByQuiz(Quiz quiz) throws RuntimeException {
				
		Connection con = DBConnection.connection();
		
		StringBuffer sb = new StringBuffer();
		
		try{		
			// Ausführung des SQL-Querys
			Statement stmt = con.createStatement();
			String sql = "SELECT * FROM `hdm-clicker`.Results AS results INNER JOIN `hdm-clicker`.Participants AS ptc ON results.hdmuser = ptc.kuerzel WHERE results.quizid="+quiz.getId()+" AND results.quizversion="+quiz.getVersion()+" ORDER BY results.hdmuser";
			ResultSet rs = stmt.executeQuery(sql);
			
			sb.append("Antwort-Nr." + ",");
			sb.append("Teilnehmer-Antwort" + ",");
			sb.append("Question-ID" + ",");
			sb.append("Quiz-ID" + ",");
			sb.append("Quiz-Version" + ",");
			sb.append("Richtig beantwortet" + ",");
			sb.append("HdM-Kürzel" + ",");
			sb.append("Vorname" + ",");
			sb.append("Nachname" + ",");
			
			while(rs.next()){
				sb.append("\n");
				sb.append(rs.getInt(1) + ",");
				sb.append(rs.getInt(2) + ",");
				sb.append(rs.getInt(4) + ",");
				sb.append(rs.getInt(5) + ",");
				sb.append(rs.getInt(6) + ",");
				sb.append(rs.getInt(7) + ",");
				sb.append(rs.getString(8) + ",");
				sb.append(rs.getString(9) + ",");
				sb.append(rs.getString(10) + ",");
			}
			
			
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm fa: " + e1.getMessage());		
		}
		
				
		return sb.toString();
			
	}
	
	/**
	 * Methode um eine Results aus der DB zu löschen
	 * 
	 * @param	result - Objekt welches gelöscht werden soll
	 * @throws	Bei der Kommunikation mit der DB kann es zu Komplikationen kommen,
	 * 			die entstandene Exception wird an die aufrufende Methode weitergereicht
	 */
	public void delete(Result result) throws RuntimeException {
		
		/*
		 * Das löschen von Results ist kritisch, da Sie Bewegeungsdaten sind
		 * und zur Nachprüfung vorgehalten werden sollten
		 */
		
		Connection con = DBConnection.connection();
		try {
			Statement stmt = con.createStatement();
									
			// Löschen der Results-Entität
			String sql = "DELETE FROM Results WHERE id = '"+result.getId()+"';";
			stmt.executeUpdate(sql);
		}
		catch (SQLException e1) {
			throw new RuntimeException("Datenbankbankproblem - rm.delete: " + e1.getMessage());
		}
		
	}
	

}