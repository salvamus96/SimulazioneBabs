package it.polito.tdp.babs;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.babs.model.CountResult;
import it.polito.tdp.babs.model.Model;
import it.polito.tdp.babs.model.SimulationResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class BabsController {

	private Model model;

	public void setModel(Model model) {
		this.model = model;
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	// implementa un calendario
	private DatePicker pickData;

	@FXML
	private Slider sliderK;

	@FXML
	private TextArea txtResult;

	@FXML
	void doContaTrip(ActionEvent event) {
		txtResult.clear();
		try {
			
			// restituisce una LocalDate
			LocalDate date = pickData.getValue();
			List<CountResult> results = model.getTripCounts(date);

			if (results == null) {
				txtResult.setText("Non ci sono trip per la data selezionata");
				return;
			}

			for (CountResult cc : results) {
				txtResult.appendText(cc.toString());
			}
			
		} catch (RuntimeException e) {
			txtResult.setText("Errore di connession al DB");
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		try {
			
			LocalDate date = pickData.getValue();
			
			// getDayOfWeek va da Monday a Sunday
			if (date.getDayOfWeek().getValue() > 4) {
				txtResult.setText("Selezionare una data dal Lun al Ven");
				return;
			}
			
			// slider � una barra mobile di valori predefiniti
			Double k = sliderK.getValue();
			SimulationResult sr = model.simula(date, k);
			txtResult.setText(sr.toString());
			
		} catch (RuntimeException e) {
			txtResult.setText("Errore di connession al DB");
		}
	}

	@FXML
	void initialize() {
		assert pickData != null : "fx:id=\"pickData\" was not injected: check your FXML file 'Babs.fxml'.";
		assert sliderK != null : "fx:id=\"sliderK\" was not injected: check your FXML file 'Babs.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Babs.fxml'.";

		pickData.setValue(LocalDate.of(2013, 9, 1));
	}
}
