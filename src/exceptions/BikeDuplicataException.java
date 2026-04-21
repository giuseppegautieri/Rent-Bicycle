package exceptions;

import model.Bike.Bike;


public class BikeDuplicataException extends Exception {
	public BikeDuplicataException(Bike b) {
		super("La bicicletta " + b.toString() + " e' gia' presente in db");
	}
}
