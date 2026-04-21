package controller;

import model.User.Cliente;


public class Pagamento {

	private String modalita;
	private double costo;
	private Cliente cliente;
	private Affitto affitto;

	public String getModalita() { return modalita; }
	public void setModalita(String modalita) { this.modalita = modalita; }

	public double getCosto() { return costo; }
	public void setCosto(double costo) { this.costo = costo; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public Affitto getAffitto() { return affitto; }
	public void setAffitto(Affitto affitto) { this.affitto = affitto; }

}
