package test;

import controller.Database;
import exceptions.BikeNotFoundException;
import exceptions.UtenteDuplicatoException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bike.Bike;
import model.User.Cliente;


public class Scenario {

	private static final Logger LOGGER = Logger.getLogger(Scenario.class.getName());

	public static void main(String[] args) {
		try {
			Database db = Database.getDatabase();

			
			Cliente utente = new Cliente();
			utente.setUsername("test_user");
			utente.setNome("tuo nome");
			utente.setCognome("tuo cognome");
			utente.setPassword("password123");
			utente.setCellulare("3331234567");
			utente.setEmail("test@email.com");
			utente.setMetodo("carta");

			try {
				Cliente inserito = db.addCliente(utente);
				LOGGER.log(Level.INFO, "Cliente inserito: {0}", inserito.getUsername());
			} catch (UtenteDuplicatoException e) {
				LOGGER.log(Level.INFO, "Cliente gia esistente (OK)");
			}

			
			try {
				Bike bike = db.getBike("categoria");
				LOGGER.log(Level.INFO, "Bike trovata: {0}", bike.toString());

				
				double perc = db.visPercentuale(bike);
				LOGGER.log(Level.INFO, "Percentuale utilizzo: {0}%", perc);
			} catch (BikeNotFoundException e) {
				LOGGER.log(Level.INFO, "Nessuna bike trovata con categoria 'categoria'");
			}

			
			db.close();
			LOGGER.log(Level.INFO, "Test completato con successo");

		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Errore nel test", e);
		}
	}
}
