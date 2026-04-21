package model.Bike;


public class Bike extends AbstractBike {

	private int id;
	private double tariffa;

	public Bike(String categoria) {
		super(categoria);
	}

	public Bike() {
		super();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public double getTariffa() {
		return tariffa;
	}

	public void setTariffa(double tariffa) {
		this.tariffa = tariffa;
	}

	@Override
	public String toString() {
		return super.toString() + " tariffa: " + tariffa;
	}

}
