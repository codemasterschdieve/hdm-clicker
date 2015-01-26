package hdm.wi.clicker.client;

import hdm.wi.clicker.shared.ControllerAsync;
import hdm.wi.clicker.shared.bo.Question;
import hdm.wi.clicker.shared.bo.Quiz;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.dialog.AlertDialog;
import com.googlecode.mgwt.ui.client.widget.input.MPasswordTextBox;
import com.googlecode.mgwt.ui.client.widget.input.MTextBox;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexPanel;

public class QuizPasswordFormMobile extends FlexPanel{

	/**
	 * Referenz auf das Proxy-Objekt um mit dem Server kommunizieren zu können
	 */
	ControllerAsync verwaltung = null;
	
	/**
	 * Referenz auf die Clicker-Start-Seite
	 */
	Clicker clicker = null;
	
	MTextBox pwLBL = null;
	MPasswordTextBox pwTB = null;
	Button pwButton = null;
	
	Button zurueckButton = null;
	
	AlertDialog ad = null;
	
	ChooseQuizFormMobile cqfm = null;
	Quiz quiz = null;
	
	public QuizPasswordFormMobile (ControllerAsync verwaltungA) {
		
		this.verwaltung = verwaltungA;
		
		
		
		pwLBL = new MTextBox();
		pwLBL.setText("Password:");
		pwLBL.setEnabled(false);
		pwTB =  new MPasswordTextBox();
		pwButton = new Button("OK");
		
		zurueckButton = new Button("Zurück");
		
		this.add(pwLBL);
		this.add(pwTB);
		this.add(pwButton);
		this.add(zurueckButton);
		
		pwButton.addTapHandler(new TapHandler() {

			
			
			@Override
			public void onTap(TapEvent event) {
				
				if (!quiz.getPassword().equals(pwTB.getText())) {
					ad = new AlertDialog("Passwort nicht korrekt!","");
					ad.addTapHandler(new TapHandler() {

					@Override
					public void onTap(TapEvent event) {
						ad.hide();
						clicker.setChooseQuizFormMobileToMain();
						}
						
					});
					ad.show();
				}
				else {
					verwaltung.getQuizCache(quiz, new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							ad = new AlertDialog("Fehler",caught.getMessage());
							ad.addTapHandler(new TapHandler() {

							@Override
							public void onTap(TapEvent event) {
								ad.hide();
								clicker.setChooseQuizFormMobileToMain();
								}
								
							});
							ad.show();
							
						}

						@Override
						public void onSuccess(Void result) {
							verwaltung.loadQuestions(quiz, new AsyncCallback< Vector<Question>>() {

								@Override
								public void onFailure(Throwable caught) {
									ad = new AlertDialog("Fehler",caught.getMessage());
									ad.addTapHandler(new TapHandler() {

									@Override
									public void onTap(TapEvent event) {
										ad.hide();
										clicker.setChooseQuizFormMobileToMain();
										}
										
									});
									ad.show();
									
									
								}
	
								@Override
								public void onSuccess( Vector<Question> result) {
									PlayQuizFormMobile pqf = new PlayQuizFormMobile(verwaltung);
									pqf.setShownQuiz(quiz);
									pqf.setQuestionVector(result);
									pqf.setClicker(clicker);
									clicker.setPlayQuizFormMobileToMain(pqf);
								}
								
							});
							
						}
						
					});
				}
				
				
				
				
			}
			
		});
		
		zurueckButton.addTapHandler(new TapHandler() {

			@Override
			public void onTap(TapEvent event) {
				clicker.setChooseQuizFormMobileToMain();
			}
		});
	}
	
	public void setClicker(Clicker clicker) {
		this.clicker = clicker;
	}
	
	public void setChooseQuizFormMobile(ChooseQuizFormMobile cqfm) {
		this.cqfm = cqfm;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	
	
	
}
