package it.polito.tdp.babs.model;

// Creazione di una classe di risultato per l'intefaccia
public class CountResult implements Comparable<CountResult> {
	
	private Station station;
	private int numArrivals;
	private int numDepartures;
	
	public CountResult(Station station, int numArrivals, int numDepartures) {
		this.station = station;
		this.numArrivals = numArrivals;
		this.numDepartures = numDepartures;
	}
	
	@Override
	public String toString() {
		// per il primo campo vengono messi a disposizione 50 caratteri e si inizia a stampare
		// da sinistra a causa del "-", altrimenti la stringa occuperà i suoi caratteri occupando 
		// i caratteri da destra
		
		// i numeri sono allineati a destra, nota sono senza "-"
		return String.format("%-50s %4d %4d\n", station.getName(), numArrivals, numDepartures);
	}
	
	// DA NORD A SUD, perciò ordine crescente
	@Override
	public int compareTo(CountResult o) {
		return Double.compare(station.getLat(), o.station.getLat());	
	}
}
