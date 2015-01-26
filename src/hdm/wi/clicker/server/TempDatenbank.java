package hdm.wi.clicker.server;

import hdm.wi.clicker.shared.bo.Quiz;

import java.util.Vector;




public class TempDatenbank {
	
	/**
	 * Diese Klasse ist nur einmal instanziierbar und dient der Performancesteigerung,
	 * da der Datenbankzugriff reduziert wird.
	 * 
	 * @author Moser, Sonntag
	 * @version 1.0
	 * 
	 */
	private static TempDatenbank td = null;
	
	private static Vector<Quiz> activeQuizVector = new Vector<Quiz>();
	private static Vector<QuizPackage> qpv = new Vector<QuizPackage>();
	
	private TempDatenbank() throws RuntimeException {
	}
	
	public static TempDatenbank get() throws RuntimeException {
		if (td == null) {
			td = new TempDatenbank();
		}
		return td;
	}

	public Vector<Quiz> getActiveQuizVector() {
		return activeQuizVector;
	}

	public void setActiveQuizVector(Vector<Quiz> activeQuizVector) {
		TempDatenbank.activeQuizVector = activeQuizVector;
	}

	public static Vector<QuizPackage> getQpv() {
		return qpv;
	}

	public static void setQpv(Vector<QuizPackage> qpv) {
		TempDatenbank.qpv = qpv;
	}
	
	
	
	
	
}
