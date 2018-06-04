package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polito.tdp.babs.db.BabsDAO;

public class Model {
	
	// riferimento al dao
	private BabsDAO bdao;
	
	private List<Station> stations;
	StationIdMap stationIdMap;
	
	public Model() {
		bdao = new BabsDAO();
		
		// utile creare un riferimento per ogni trip alla stazione
		stationIdMap = new StationIdMap();
		stations = bdao.getAllStations(stationIdMap);
	}
	
	public List<Trip> getTripsByDate(LocalDate date) {
		return bdao.getAllTrips(date);
	}

	
	public List<CountResult> getTripCounts(LocalDate date) {
		if (getTripsByDate(date).size() == 0) {
			return null;
		}
		
		List<CountResult> results = new ArrayList<CountResult>();
		for (Station station : stations) {
			// oggetto utile per la stampa
			CountResult cc = new CountResult(station, 
											 bdao.getArrivals(station, date), 
											 bdao.getDepartures(station, date));
			results.add(cc);
		}
		// ordinare i risultati da nord a sud (latitudine)
		Collections.sort(results);
		return results;
	}
	
	public List<Station> getStations() {
		return stations;
	}

	public SimulationResult simula(LocalDate date, Double k) {
		// classe separata per la simulazione
		Simulazione sim = new Simulazione(date, k, this);
		sim.run();
		return sim.getResults();
	}

}
