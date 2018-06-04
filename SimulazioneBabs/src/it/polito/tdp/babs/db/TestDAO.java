package it.polito.tdp.babs.db;

import java.time.LocalDate;
import java.util.List;

import it.polito.tdp.babs.model.Station;
import it.polito.tdp.babs.model.StationIdMap;
import it.polito.tdp.babs.model.Trip;

public class TestDAO {

	public static void main(String args[]) {
		
		BabsDAO dao = new BabsDAO();
		StationIdMap map = new StationIdMap();
		List<Station> stations = dao.getAllStations(map);
		
		for (Station s : stations) {
			System.out.format("%2d %-20s\n", s.getStationID(), s.getName());
		}

		LocalDate d = LocalDate.of(2013,8,29);
		List<Trip> trips = dao.getAllTrips(d);
		System.out.format("Found %d trips", trips.size());
		
	}
}
