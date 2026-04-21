package controller;

import java.util.UUID;
import model.Bike.Bike;
import model.Parcheggio.Parcheggio;
import model.User.Cliente;


public class Affitto {

	private Bike bike;
	private Parcheggio origine;
	private Parcheggio destinazione;
	private Cliente cliente;
	private double durataKm;
	private String id;

	public Affitto(String id, Bike bike, Parcheggio origine,
			Parcheggio destinazione, Cliente cliente, double durataKm) {
		this.id = id;
		this.bike = bike;
		this.origine = origine;
		this.destinazione = destinazione;
		this.cliente = cliente;
		this.durataKm = durataKm;
	}

	public Affitto(Bike bike, Parcheggio origine,
			Parcheggio destinazione, Cliente cliente, double durataKm) {
		this.id = UUID.randomUUID().toString();
		this.bike = bike;
		this.origine = origine;
		this.destinazione = destinazione;
		this.cliente = cliente;
		this.durataKm = durataKm;
	}

	public Affitto(String id) {
		this.id = id;
	}

	public Affitto() {
		this.id = UUID.randomUUID().toString();
	}

	public Bike getBike() { return bike; }
	public void setBike(Bike bike) { this.bike = bike; }

	public Parcheggio getOrigine() { return origine; }
	public void setOrigine(Parcheggio origine) { this.origine = origine; }

	public Parcheggio getDestinazione() { return destinazione; }
	public void setDestinazione(Parcheggio destinazione) { this.destinazione = destinazione; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public double getDurataKm() { return durataKm; }
	public void setDurataKm(double durataKm) { this.durataKm = durataKm; }

	
	public String getCodAffitto() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Affitto [codAffitto=" + id + " bike=" + bike.toString() + ", origine=" + origine.getCitta()
				+ ", destinazione=" + destinazione.getCitta() + ", cliente="
				+ cliente.getUsername() + ", durataKm=" + durataKm + "]";
	}

}
