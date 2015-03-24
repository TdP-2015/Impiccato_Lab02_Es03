package it.polito.tdp.impiccato;

import it.polito.tdp.impiccato.model.ImpiccatoModel;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ImpiccatoController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtSegreto;

	@FXML
	private TextField txtParola;

	@FXML
	private ProgressBar pbErrori;

	@FXML
	private Button btnStart;

	@FXML
	private HBox boxSegreto;

	@FXML
	private TextField txtErrori;

	@FXML
	private ComboBox<String> comboLettera;

	@FXML
	private Button btnTry;

	@FXML
	private TextField txtSoluzione;

	// Reference to ImpiccatoModel
	private ImpiccatoModel model;

	// In-match flag (is the match already started?)
	boolean inMatch = false;

	private void updateView() {
		if (inMatch) {
			// In match
			boxSegreto.setVisible(false);
			btnStart.setDisable(true);
			comboLettera.setDisable(false);
			btnTry.setDisable(false);

		} else {
			// Not in match
			boxSegreto.setVisible(true);
			btnStart.setDisable(false);
			comboLettera.setDisable(true);
			btnTry.setDisable(true);
		}

		txtErrori.setText(String.format("%d", model.getErrori()));
		txtParola.setText(model.getMaschera());
		pbErrori.setProgress((double) model.getErrori() / model.getMAX_ERRORI());

	}

	@FXML
	void doStart(ActionEvent event) {

		String segreto = txtSegreto.getText();

		// remove spaces and converto to uppercase
		segreto = segreto.trim().toUpperCase();

		// must not be empty
		if (segreto.length() == 0) {
			txtSoluzione.setText("ERRORE parola non valida");
			return;
		}

		// must only contain letters
		for (int i = 0; i < segreto.length(); i++) {
			if (!Character.isUpperCase(segreto.charAt(i))) {
				txtSoluzione.setText("ERRORE parola non valida");
				return;
			}
		}

		model.startGame(segreto);

		inMatch = true;
		updateView();
	}

	@FXML
	void doTry(ActionEvent event) {

		String t = comboLettera.getValue();
		if (t == null) {
			// no selected letter
			return;
		}

		model.tryLetter(t);

		if (model.isWinner()) {
			txtSoluzione.setText(txtSegreto.getText());
			txtSegreto.clear();
			inMatch = false;
		} else if (model.isLoser()) {
			txtSoluzione.setText(txtSegreto.getText());
			txtSegreto.clear();
			inMatch = false;
		}

		updateView();
	}

	@FXML
	void initialize() {
		assert txtSegreto != null : "fx:id=\"txtSegreto\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert txtParola != null : "fx:id=\"txtParola\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert pbErrori != null : "fx:id=\"pbErrori\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert btnStart != null : "fx:id=\"btnStart\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert boxSegreto != null : "fx:id=\"boxSegreto\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert txtErrori != null : "fx:id=\"txtErrori\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert comboLettera != null : "fx:id=\"comboLettera\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert btnTry != null : "fx:id=\"btnTry\" was not injected: check your FXML file 'Impiccato.fxml'.";
		assert txtSoluzione != null : "fx:id=\"txtSoluzione\" was not injected: check your FXML file 'Impiccato.fxml'.";

		// Popola la combo box
		for (char ch = 'A'; ch <= 'Z'; ch++) {
			comboLettera.getItems().add(String.valueOf(ch));
		}

	}

	public void setModel(ImpiccatoModel model) {
		this.model = model;

		updateView();
	}
}
