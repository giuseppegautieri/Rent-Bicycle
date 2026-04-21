package exceptions;


public class AffittoNotFoundException extends Exception {
	public AffittoNotFoundException(String id) {
		super("L'affitto " + id + " non e' stato trovato");
	}
}
