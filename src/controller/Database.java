package controller;

import commons.DBConstants;
import commons.PasswordUtils;
import exceptions.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Bike.Bike;
import model.Parcheggio.Parcheggio;
import model.User.*;


public class Database {

	private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
	private static Database instance;
	private Connection toDB;

	private Database() throws SQLException {
		toDB = DriverManager.getConnection(DBConstants.URI, DBConstants.DB_USER, DBConstants.DB_PASSWORD);
		try (Statement create = toDB.createStatement()) {
			create.execute(DBConstants.CREATE_DATABASE);
		} catch (SQLException e) {
			LOGGER.log(Level.INFO, "Database gia esistente");
		}

		toDB = DriverManager.getConnection(DBConstants.URI + "/" + DBConstants.DB_NAME, DBConstants.DB_USER, DBConstants.DB_PASSWORD);
		try (Statement create = toDB.createStatement()) {
			try { create.execute(DBConstants.CREATE_TABLE_BIKES); } catch (SQLException e) {
				LOGGER.log(Level.INFO, "Tabella biciclette gia esistente");
			}
			try { create.execute(DBConstants.CREATE_TABLE_CLIENTI); } catch (SQLException e) {
				LOGGER.log(Level.INFO, "Tabella clienti gia esistente");
			}
			try { create.execute(DBConstants.CREATE_TABLE_ADMIN); } catch (SQLException e) {
				LOGGER.log(Level.INFO, "Tabella admin gia esistente");
			}
			try { create.execute(DBConstants.CREATE_TABLE_AFFITTI); } catch (SQLException e) {
				LOGGER.log(Level.INFO, "Tabella affitti gia esistente");
			}
			try { create.execute(DBConstants.CREATE_TABLE_PAGAMENTI); } catch (SQLException e) {
				LOGGER.log(Level.INFO, "Tabella pagamenti gia esistente");
			}
		}
	}

	
	public static synchronized Database getDatabase() throws SQLException {
		if (instance == null)
			instance = new Database();
		return instance;
	}

	
	public synchronized void close() {
		if (toDB != null) {
			try {
				toDB.close();
				LOGGER.log(Level.INFO, "Connessione al database chiusa");
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "Errore nella chiusura della connessione", e);
			}
			instance = null;
		}
	}

	
	public synchronized Cliente getUtente(String username) throws SQLException, UtenteNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_CLIENTI + " WHERE username = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new UtenteNotFoundException(username);
				Cliente nuovo = new Cliente();
				nuovo.setUsername(username);
				nuovo.setNome(result.getString("nome"));
				nuovo.setCognome(result.getString("cognome"));
				nuovo.setPassword(result.getString("password"));
				nuovo.setCellulare(result.getString("cellulare"));
				nuovo.setEmail(result.getString("email"));
				nuovo.setMetodo(result.getString("metodo"));
				return nuovo;
			}
		}
	}

	
	public synchronized Cliente addCliente(Cliente utente) throws UtenteNotFoundException, SQLException, UtenteDuplicatoException {
		try {
			getUtente(utente.getUsername());
			throw new UtenteDuplicatoException(utente.getUsername());
		} catch (UtenteNotFoundException ex) {
			String sql = "INSERT INTO " + DBConstants.DB_NAME + ".clienti (username, nome, cognome, password, cellulare, email, metodo) VALUES (?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = toDB.prepareStatement(sql)) {
				ps.setString(1, utente.getUsername());
				ps.setString(2, utente.getNome());
				ps.setString(3, utente.getCognome());
				ps.setString(4, PasswordUtils.hashPassword(utente.getPassword()));
				ps.setString(5, utente.getCellulare());
				ps.setString(6, utente.getEmail());
				ps.setString(7, utente.getMetodo());
				ps.executeUpdate();
			}
			return getUtente(utente.getUsername());
		}
	}

	
	public synchronized Admin getAdmin(String username) throws SQLException, UtenteNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_ADMIN + " WHERE username = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, username);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new UtenteNotFoundException(username);
				Admin nuovo = new Admin();
				nuovo.setUsername(username);
				nuovo.setNome(result.getString("nome"));
				nuovo.setCognome(result.getString("cognome"));
				nuovo.setPassword(result.getString("password"));
				return nuovo;
			}
		}
	}

	
	public synchronized Admin addAdmin(Admin admin) throws UtenteNotFoundException, SQLException, UtenteDuplicatoException {
		try {
			getAdmin(admin.getUsername());
			throw new UtenteDuplicatoException(admin.getUsername());
		} catch (UtenteNotFoundException ex) {
			String sql = "INSERT INTO " + DBConstants.DB_NAME + ".admin (username, nome, cognome, password) VALUES (?, ?, ?, ?)";
			try (PreparedStatement ps = toDB.prepareStatement(sql)) {
				ps.setString(1, admin.getUsername());
				ps.setString(2, admin.getNome());
				ps.setString(3, admin.getCognome());
				ps.setString(4, PasswordUtils.hashPassword(admin.getPassword()));
				ps.executeUpdate();
			}
			return getAdmin(admin.getUsername());
		}
	}

	
	public synchronized Bike getBike(String categoria, String equipaggiamento) throws SQLException, BikeNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_BIKES + " WHERE categoria = ? AND equipaggiamento = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, categoria);
			ps.setString(2, equipaggiamento);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new BikeNotFoundException(categoria, equipaggiamento);
				Bike nuovo = new Bike();
				nuovo.setCategoria(categoria);
				nuovo.addEquipaggiamento(equipaggiamento);
				nuovo.setTariffa(result.getDouble("tariffa"));
				return nuovo;
			}
		}
	}

	
	public synchronized Bike getBike(String categoria) throws SQLException, BikeNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_BIKES + " WHERE categoria = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, categoria);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new BikeNotFoundException(categoria);
				Bike nuovo = new Bike();
				nuovo.setId(result.getInt("id"));
				nuovo.setCategoria(result.getString("categoria"));
				nuovo.addEquipaggiamento(result.getString("equipaggiamento"));
				nuovo.setTariffa(result.getDouble("tariffa"));
				while (result.next()) {
					nuovo.addEquipaggiamento(result.getString("equipaggiamento"));
				}
				return nuovo;
			}
		}
	}

	
	public synchronized ArrayList<Bike> getBikes() throws SQLException {
		ArrayList<Bike> bikes = new ArrayList<>();
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_BIKES;
		try (PreparedStatement ps = toDB.prepareStatement(sql);
			 ResultSet result = ps.executeQuery()) {
			while (result.next()) {
				Bike nuovo = new Bike();
				nuovo.setId(result.getInt("id"));
				nuovo.setCategoria(result.getString("categoria"));
				nuovo.addEquipaggiamento(result.getString("equipaggiamento"));
				nuovo.setTariffa(result.getDouble("tariffa"));
				bikes.add(nuovo);
			}
		}
		return bikes;
	}

	
	public synchronized void addBike(Bike bike) throws SQLException, BikeDuplicataException, BikeNotFoundException {
		if (bike.getEquipaggiamento().isEmpty()) {
			String sql = "INSERT INTO " + DBConstants.DB_NAME + ".biciclette (categoria, tariffa) VALUES (?, ?)";
			try (PreparedStatement ps = toDB.prepareStatement(sql)) {
				ps.setString(1, bike.getCategoria());
				ps.setDouble(2, bike.getTariffa());
				ps.executeUpdate();
			}
		} else {
			String sql = "INSERT INTO " + DBConstants.DB_NAME + ".biciclette (categoria, equipaggiamento, tariffa) VALUES (?, ?, ?)";
			try (PreparedStatement ps = toDB.prepareStatement(sql)) {
				for (String equip : bike.getEquipaggiamento()) {
					ps.setString(1, bike.getCategoria());
					ps.setString(2, equip);
					ps.setDouble(3, bike.getTariffa());
					ps.executeUpdate();
				}
			}
		}
	}

	
	public synchronized void addEquipaggiamento(Bike bike, String equipaggiamento) throws SQLException, BikeDuplicataException, BikeNotFoundException {
		String sql = "INSERT INTO " + DBConstants.DB_NAME + ".biciclette (categoria, equipaggiamento, tariffa) VALUES (?, ?, ?)";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, bike.getCategoria());
			ps.setString(2, equipaggiamento);
			ps.setDouble(3, bike.getTariffa());
			ps.executeUpdate();
		}
		bike.addEquipaggiamento(equipaggiamento);
	}

	
	public synchronized Affitto getAffitto(String id) throws AffittoNotFoundException, SQLException, BikeNotFoundException, UtenteNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_AFFITTI + " WHERE codAffitto = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, id);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new AffittoNotFoundException(id);
				Affitto affitto = new Affitto(id);
				affitto.setBike(getBike(result.getString("bicicletta")));
				affitto.setCliente(getUtente(result.getString("cliente")));
				affitto.setOrigine(new Parcheggio(result.getString("cittaOrigine")));
				affitto.setDestinazione(new Parcheggio(result.getString("cittaDest")));
				affitto.setDurataKm(result.getDouble("durataKM"));
				return affitto;
			}
		}
	}

	
	public synchronized ArrayList<Affitto> getAllAffitti() throws SQLException, BikeNotFoundException, UtenteNotFoundException {
		ArrayList<Affitto> affitti = new ArrayList<>();
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_AFFITTI;
		try (PreparedStatement ps = toDB.prepareStatement(sql);
			 ResultSet result = ps.executeQuery()) {
			while (result.next()) {
				Affitto affitto = new Affitto();
				affitto.setId(result.getString("codAffitto"));
				affitto.setBike(getBike(result.getString("bicicletta")));
				affitto.setCliente(getUtente(result.getString("cliente")));
				affitto.setOrigine(new Parcheggio(result.getString("cittaOrigine")));
				affitto.setDestinazione(new Parcheggio(result.getString("cittaDest")));
				affitto.setDurataKm(result.getDouble("durataKM"));
				affitti.add(affitto);
			}
		}
		return affitti;
	}

	
	public synchronized void addAffitto(Affitto affitto) throws SQLException, AffittoDuplicatoException, BikeNotFoundException, UtenteNotFoundException {
		try {
			getAffitto(affitto.getCodAffitto());
			throw new AffittoDuplicatoException();
		} catch (AffittoNotFoundException ex) {
			String sql = "INSERT INTO " + DBConstants.DB_NAME + ".affitti (codAffitto, cliente, bicicletta, cittaOrigine, cittaDest, durataKM) VALUES (?, ?, ?, ?, ?, ?)";
			try (PreparedStatement ps = toDB.prepareStatement(sql)) {
				ps.setString(1, affitto.getCodAffitto());
				ps.setString(2, affitto.getCliente().getUsername());
				ps.setString(3, affitto.getBike().getCategoria());
				ps.setString(4, affitto.getOrigine().getCitta());
				ps.setString(5, affitto.getDestinazione().getCitta());
				ps.setDouble(6, affitto.getDurataKm());
				ps.executeUpdate();
			}
		}
	}

	
	public synchronized double addPagamento(Pagamento pagamento) throws SQLException {
		double costo = pagamento.getAffitto().getDurataKm() * pagamento.getAffitto().getBike().getTariffa();
		String sql = "INSERT INTO " + DBConstants.DB_NAME + ".pagamenti (cliente, affitto, modalita, costo) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setString(1, pagamento.getCliente().getUsername());
			ps.setString(2, pagamento.getAffitto().getCodAffitto());
			ps.setString(3, pagamento.getCliente().getMetodo());
			ps.setDouble(4, costo);
			ps.executeUpdate();
		}
		LOGGER.log(Level.INFO, "Pagamento aggiunto con costo: {0}", costo);
		return costo;
	}

	
	public synchronized Pagamento getPagamento(int id) throws SQLException, PagamentoNotFoundException, AffittoNotFoundException, BikeNotFoundException, UtenteNotFoundException {
		String sql = "SELECT * FROM " + DBConstants.NAME_TABLE_PAGAMENTI + " WHERE id = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet result = ps.executeQuery()) {
				if (!result.next())
					throw new PagamentoNotFoundException(id);
				Affitto affitto = getAffitto(result.getString("affitto"));
				Cliente cliente = getUtente(result.getString("cliente"));
				Pagamento pagamento = new Pagamento();
				pagamento.setAffitto(affitto);
				pagamento.setCliente(cliente);
				pagamento.setCosto(result.getDouble("costo"));
				pagamento.setModalita(result.getString("modalita"));
				return pagamento;
			}
		}
	}

	
	public synchronized void aggiornaTariffa(Bike bike, double tariffa) throws SQLException {
		String sql = "UPDATE " + DBConstants.DB_NAME + ".biciclette SET tariffa = ? WHERE id = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sql)) {
			ps.setDouble(1, tariffa);
			ps.setInt(2, bike.getId());
			ps.executeUpdate();
		}
		LOGGER.log(Level.INFO, "Tariffa aggiornata per bike id={0} a {1}", new Object[]{bike.getId(), tariffa});
	}

	
	public synchronized double visPercentuale(Bike bike) throws SQLException, BikeNotFoundException, UtenteNotFoundException {
		int numTot = 0;
		int numDistinct = 0;

		String sqlDistinct = "SELECT COUNT(*) AS result FROM " + DBConstants.DB_NAME + ".affitti WHERE bicicletta = ?";
		try (PreparedStatement ps = toDB.prepareStatement(sqlDistinct)) {
			ps.setString(1, bike.getCategoria());
			try (ResultSet result = ps.executeQuery()) {
				if (result.next())
					numDistinct = result.getInt(1);
			}
		}

		String sqlTotal = "SELECT COUNT(*) AS result FROM " + DBConstants.DB_NAME + ".affitti";
		try (PreparedStatement ps = toDB.prepareStatement(sqlTotal);
			 ResultSet result = ps.executeQuery()) {
			if (result.next())
				numTot = result.getInt(1);
		}

		if (numTot == 0)
			return 0.0;

		return ((double) numDistinct / numTot) * 100;
	}

}
