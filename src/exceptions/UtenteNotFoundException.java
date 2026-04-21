package exceptions;


public class UtenteNotFoundException extends Exception {
	public UtenteNotFoundException(String username) {
		super("L'utente " + username + " non e' stato trovato");
	}
}
