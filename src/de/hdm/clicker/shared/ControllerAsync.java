package de.hdm.clicker.shared;


import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.hdm.clicker.shared.bo.Category;
import de.hdm.clicker.shared.bo.InfoGraph;
import de.hdm.clicker.shared.bo.Teacher;
import de.hdm.clicker.shared.bo.Question;
import de.hdm.clicker.shared.bo.Quiz;
import de.hdm.clicker.shared.bo.Result;

/**
 * Asynchrones Interface 
 * 
 * @author Thies, Moser, Sonntag
 * @version 1
 */
public interface ControllerAsync {
	
	
	/**
	 * Open DBConnection
	 * @throws RuntimeException
	 */
	void openDBConnection(AsyncCallback<Void> callback) throws RuntimeException;
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte und Daten auslesen
	 * ***********************************************************************************************
	 */
	
	void getTeacher(Teacher teacher, AsyncCallback<Vector<Teacher>> callback) throws RuntimeException;
	
	void getAllTeachers(AsyncCallback<Vector<Teacher>> callback) throws RuntimeException;
	
	void getloggedinTeacher(AsyncCallback<Teacher> callback) throws RuntimeException;
	
	void getCategory(Category category, AsyncCallback<Vector<Category>> callback) throws RuntimeException;	

	void getAllCategoriesByTeacher(AsyncCallback<Vector<Category>> callback) throws RuntimeException;
	
	void getQuestion(Question question, AsyncCallback<Vector<Question>> callback) throws RuntimeException;
	
	void getAllQuestionsByCategoryAndDifficulty(Category category, int difficulty, AsyncCallback<Vector<Question>> callback) throws RuntimeException;
	
	void getAllQuestionsByCategory(Category category, AsyncCallback<Vector<Question>> callback) throws RuntimeException;
	
	void getAllQuestionsByQuiz(Quiz quiz, AsyncCallback<Vector<Question>> callback) throws RuntimeException;
	
	void getQuiz(Quiz quiz, AsyncCallback<Vector<Quiz>> callback) throws RuntimeException;
	
	void getAllQuizByTeacher(AsyncCallback<Vector<Quiz>> callback) throws RuntimeException;
	
	void getAllQuizByTeacherAndActive(AsyncCallback<Vector<Quiz>> callback) throws RuntimeException;
	
	void getQuizDatasByTeacher(AsyncCallback<Vector<Integer>> callback) throws RuntimeException;
	
	void getAllQuizzByTeacherAndStartingdate(int startingdate, AsyncCallback<Vector<Quiz>> callback) throws RuntimeException;
	
	void getInfographByQuiz(Quiz quiz, AsyncCallback<Vector<InfoGraph>> callback) throws RuntimeException;
	
	void getCSVDataByQuiz(Quiz quiz, AsyncCallback<String> callback) throws RuntimeException;
	
	void getAllActiveQuiz(AsyncCallback<Vector<Quiz>> callback) throws RuntimeException;
	
	void getResults(Result result, AsyncCallback<Boolean> callback) throws RuntimeException;
	
	void getMissingResults(Vector<Result> resultsvector, AsyncCallback<Vector<Boolean>> callback) throws RuntimeException;
	
	
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte anlegen
	 * ***********************************************************************************************
	 */
	void createTeacher(String username, String password, String firstName, String name, AsyncCallback<Teacher> callback) throws RuntimeException;

	void createCategory(String description, AsyncCallback<Category> callback) throws RuntimeException;
	
	void createQuestion(String questiontxt, String answer1txt, String answer2txt, String answer3txt, String answer4txt, int difficulty, int categoryId, AsyncCallback<Question> callback) throws RuntimeException;
	
	void createQuiz(String passwort, int buttonDuration, String description, int questionDuration, 
			int startingdate, int startingtime, boolean active, boolean automatic, boolean randomorder, Vector<Question> questionvector, AsyncCallback<Quiz> callback) throws RuntimeException;
	
	void createNewQuizVersion(Quiz quiz, AsyncCallback<Quiz> callback) throws RuntimeException;
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte verändern
	 * ***********************************************************************************************
	 */
	
	void editCategory(Category category, AsyncCallback<Category> callback) throws RuntimeException;
	
	void editTeacher(Teacher teacher, AsyncCallback<Teacher> callback) throws RuntimeException;
	
	void editQuestion(Question question, AsyncCallback<Question> callback) throws RuntimeException;
	
	void editQuiz(Quiz quiz, Vector<Question> questionvector, AsyncCallback<Quiz> callback) throws RuntimeException;
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte löschen
	 * ***********************************************************************************************
	 */
	void deleteTeacher(Teacher teacher, AsyncCallback<Void> callback) throws RuntimeException;
	
	void deleteCategory(Category category, AsyncCallback<Void> callback) throws RuntimeException;
	
	void deleteQuestion(Question question, AsyncCallback<Void> callback) throws RuntimeException;
	
	void deleteQuiz(Quiz quiz,  AsyncCallback<Void> callback) throws RuntimeException;
	
	
	/*
	 * ***********************************************************************************************
	 * BusinessObjekte laden
	 * ***********************************************************************************************
	 */
	void getQuizCache(Quiz quiz, AsyncCallback<Void> callback) throws RuntimeException;
	
	void loadQuestions(Quiz quiz, AsyncCallback<Vector<Question>> callback) throws RuntimeException;
	
	void checkAllQuiz(AsyncCallback<Void> callback) throws RuntimeException;
	
	void startQuiz(Quiz quiz,  AsyncCallback<Quiz> callback) throws RuntimeException;
		
	/*
	 * ***********************************************************************************************
	 * Authentifizierung
	 * ***********************************************************************************************
	 */
	
	void authenticateTeacher(String username, String password, AsyncCallback<Boolean> callback) throws RuntimeException;
	
	void authenticateAdministrator(String password, AsyncCallback<Boolean> callback) throws RuntimeException;	

	void loginStudent(String student, AsyncCallback<Void> callback) throws RuntimeException;
		
}
