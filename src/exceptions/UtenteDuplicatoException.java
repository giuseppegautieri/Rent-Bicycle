package exceptions;


public class UtenteDuplicatoException extends Exception {
	public UtenteDuplicatoException(String username) {
		super("L'utente " + username + " e' gia' presente");
	}
}
