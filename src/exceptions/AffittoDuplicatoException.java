package exceptions;


public class AffittoDuplicatoException extends Exception {

	public AffittoDuplicatoException(int id) {
		super("L'affitto " + id + " e' gia' presente");
	}

	public AffittoDuplicatoException() {
		super("Affitto duplicato");
	}

}
