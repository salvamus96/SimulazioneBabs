package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Simulazione {
	
	private LocalDate date;
	private double k;
	private Model model;
	
	// coda prioritaria di eventi da processare in relazione temporale
	private PriorityQueue<Event> pq;
	
	private int PICKmiss = 0;
	private int DROPmiss = 0;
	private Map<Station, Integer> stationCount;
	
	// tipi di eventi esclusivi tra loro
	private enum EventType {
		PICK, DROP;
	}
	
	public Simulazione(LocalDate date, double k, Model model) {
		this.date = date;
		this.k = k;
		this.model = model; // si usa il model su cui si sta operando
		pq = new PriorityQueue<Event>();
		stationCount = new HashMap<Station, Integer>();
	}
	
	/**
	 * Tale metodo si occupa sia dell'avvio della simulazione ma all'inizio anche dell'inizializzazione
	 */
	public void run() {
		List<Trip> trips = model.getTripsByDate(date);
		
		// Aggiungere gli eventi alla pq
		for (Trip t : trips) {
			pq.add(new Event(EventType.PICK, t.getStartDate(), t));
		}
		
		// Inizializzo il numero di biciclette per ogni stazione
		for (Station s : model.getStations()) {
			stationCount.put(s, (int)(s.getDockCount() * k));
		}
		
		// processare gli eventi
		while(!pq.isEmpty()) {
			// estrazione dell'evento in testa alla coda
			Event e = pq.poll();
			
			switch(e.type) {
			case PICK:
				// accedo alla stazione tramite l'id della stazione di partenza del trip con IdentityMap
				Station station = model.stationIdMap.get(e.trip.getStartStationID());
				int count = stationCount.get(station);
				
				if (count > 0) {
					// bicicletta disponibile
					count--;
					stationCount.put(station, count);
					// aggiungo un nuovo evento DROP
					pq.add(new Event(EventType.DROP, e.trip.getEndDate(), e.trip));
				} else {
					// l'utente non Ã¨ riuscito a prendere la bicicletta
					PICKmiss++;
				}
				break;
			
			case DROP:
				station = model.stationIdMap.get(e.trip.getEndStationID());
				count = stationCount.get(station);
				
				if (station.getDockCount() > count) {
					// ci sono ancora dei posti disponibili
					count++;
					// sovrascrive il count della stazione presente nella mappa
					stationCount.put(station, count);
				} else {
					DROPmiss++;
				}
				break;
			}
		}
	}

	/**
	 * Restituisce un oggetto predisposto per la visualizzazione in interfaccia
	 * @return
	 */
	public SimulationResult getResults() {
		return new SimulationResult(PICKmiss, DROPmiss);
	}
	
	// la classe evento esiste sono in relazione alla classe simulazione
	private class Event implements Comparable<Event> {
		
		EventType type;
		// la data è necessaria per poter ordinare gli eventi in scala temporale
		// meglio usare LocalDateTime perchè nel confronto rientra anche l'orario e non solo il giorno
		LocalDateTime date;
		// ogni evento è associato a un specifico trip
		Trip trip;
		
		public Event(EventType type, LocalDateTime date, Trip trip) {
			this.type = type;
			this.date = date;
			this.trip = trip;
		}

		@Override
		public int compareTo(Event o) {
			return date.compareTo(o.date);
		}
		
		
	}
}
