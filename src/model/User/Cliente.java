package model.User;


public class Cliente extends AbstractUtente {

	private String cellulare;
	private String email;
	private String metodo;

	public Cliente() {
		super();
	}

	public Cliente(String cellulare, String email, String metodo) {
		super();
		this.cellulare = cellulare;
		this.email = email;
		this.metodo = metodo;
	}

	public String getCellulare() { return cellulare; }
	public void setCellulare(String cellulare) { this.cellulare = cellulare; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getMetodo() { return metodo; }
	public void setMetodo(String metodo) { this.metodo = metodo; }

}
