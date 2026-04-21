package exceptions;


public class PagamentoNotFoundException extends Exception {
	public PagamentoNotFoundException(int id) {
		super("Il pagamento " + id + " non e' stato trovato");
	}
}
