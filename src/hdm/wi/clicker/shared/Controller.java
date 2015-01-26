package hdm.wi.clicker.shared;


import hdm.wi.clicker.shared.bo.Category;
import hdm.wi.clicker.shared.bo.InfoGraph;
import hdm.wi.clicker.shared.bo.Question;
import hdm.wi.clicker.shared.bo.Quiz;
import hdm.wi.clicker.shared.bo.Result;
import hdm.wi.clicker.shared.bo.Teacher;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


/**
 * 
 * <p>
 * Synchrones Interface, welches von RemoteService erbt und somit RPC Funktionen von der Impl-Klasse ermöglicht.
 * <p>
 * 
 * @author Moser, Sonntag
 * @version 1
 */

@RemoteServiceRelativePath("controller")
public interface Controller extends RemoteService {
		
	
	
	/**
	 * Open DBConnection
	 * @throws RuntimeException
	 */
	public void openDBConnection() throws RuntimeException;
	
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte und Daten auslesen
	 * ***********************************************************************************************
	 */
	
	public Vector<Teacher> getTeacher(Teacher teacher) throws RuntimeException;
	
	public Vector<Teacher> getAllTeachers() throws RuntimeException;
	
	public Teacher getloggedinTeacher() throws RuntimeException;
	
	public Vector<Category> getCategory(Category category) throws RuntimeException;
	
	public Vector<Category> getAllCategoriesByTeacher() throws RuntimeException;
	
	public Vector<Question> getQuestion(Question question) throws RuntimeException;
	
	public Vector<Question> getAllQuestionsByCategoryAndDifficulty(Category category, int difficulty) throws RuntimeException;
	
	public Vector<Question> getAllQuestionsByCategory(Category categorie) throws RuntimeException;
	
	public Vector<Question> getAllQuestionsByQuiz(Quiz quiz) throws RuntimeException;
	
	public Vector<Quiz> getQuiz(Quiz quiz) throws RuntimeException;
	
	public Vector<Quiz> getAllQuizByTeacher() throws RuntimeException;
	
	public Vector<Quiz> getAllQuizByTeacherAndActive() throws RuntimeException;
	
	public Vector<Quiz> getAllActiveQuiz() throws RuntimeException;
	
	public Vector<Integer> getQuizDatasByTeacher() throws RuntimeException;
	
	public Vector<Quiz> getAllQuizzByTeacherAndStartingdate(int startingdate) throws RuntimeException;
	
	public Vector<InfoGraph> getInfographByQuiz(Quiz quiz) throws RuntimeException;
	
	public boolean getResults(Result result) throws RuntimeException;
	
	public Vector<Boolean> getMissingResults(Vector<Result> resultsvector) throws RuntimeException;
	
	public String getCSVDataByQuiz(Quiz quiz) throws RuntimeException;
	
	
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte anlegen
	 * ***********************************************************************************************
	 */
	
	public Teacher createTeacher(String username, String password, String firstName, String lastname) throws RuntimeException;
	
	public Category createCategory(String description) throws RuntimeException;
	
	public Question createQuestion(String questiontxt, String answer1txt, String answer2txt, String answer3txt, String answer4txt, int difficulty, int categoryId) throws RuntimeException;
	
	public Quiz createQuiz(String passwort, int buttonDuration, String description, int questionDuration, 
			int startingdate, int startingtime, boolean active, boolean automatic, boolean randomorder, Vector<Question> questionvector) throws RuntimeException;
	
	public Quiz createNewQuizVersion(Quiz quiz) throws RuntimeException;
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte verändern
	 * ***********************************************************************************************
	 */
	
	public Teacher editTeacher(Teacher teacher) throws RuntimeException;
	
	public Category editCategory(Category category) throws RuntimeException;
	
	public Question editQuestion(Question question) throws RuntimeException;
	
	public Quiz editQuiz(Quiz quiz, Vector<Question> questionvector) throws RuntimeException;
	

	/*
	 * ***********************************************************************************************
	 * BusinessObjekte löschen
	 * ***********************************************************************************************
	 */
	
	public void deleteTeacher(Teacher teacher) throws RuntimeException;
	
	public void deleteCategory(Category category) throws RuntimeException;
	
	public void deleteQuestion(Question question) throws RuntimeException;
	
	public void deleteQuiz(Quiz quiz) throws RuntimeException;
	
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte laden
	 * ***********************************************************************************************
	 */
	
	public Quiz startQuiz(Quiz quiz) throws RuntimeException;
	
	public void getQuizCache(Quiz quiz) throws RuntimeException;
	
	public void checkAllQuiz() throws RuntimeException;
	
	public Vector<Question> loadQuestions(Quiz quiz) throws RuntimeException;
	
	/*
	 * ***********************************************************************************************
	 * Authentifizierung
	 * ***********************************************************************************************
	 */
	
	public boolean authenticateAdministrator(String password) throws RuntimeException;
	
	public boolean authenticateTeacher(String username, String password) throws RuntimeException;
	
	public void loginStudent(String student) throws RuntimeException;
	
	
}
