package exceptions;


public class BikeNotFoundException extends Exception {
	public BikeNotFoundException(String categoria, String equipaggiamento) {
		super("La bicicletta: " + categoria + " - " + equipaggiamento + " non e' presente");
	}
	public BikeNotFoundException(String categoria) {
		super("La bicicletta: " + categoria + " non e' presente");
	}
}
