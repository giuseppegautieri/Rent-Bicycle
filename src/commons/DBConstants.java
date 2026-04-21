package commons;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConstants {

	private static final Logger LOGGER = Logger.getLogger(DBConstants.class.getName());
	private static final Properties props = new Properties();

	static {
		try (InputStream input = DBConstants.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input != null) {
				props.load(input);
			} else {
				LOGGER.log(Level.WARNING, "File config.properties non trovato, uso valori di default");
			}
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Errore nel caricamento di config.properties", e);
		}
	}

	public static final String DB_USER = props.getProperty("db.user", "root");
	public static final String DB_PASSWORD = props.getProperty("db.password", "");
	public static final String SERVER_NAME = props.getProperty("db.serverName", "127.0.0.1");
	public static final String PORT = props.getProperty("db.port", "3306");
	public static final String DB_NAME = props.getProperty("db.name", "database_biciclette_DEF");
	public static final String URI = props.getProperty("db.uri", "jdbc:mysql://127.0.0.1:3306");

	public static final String NAME_TABLE_BIKES = "biciclette";
	public static final String NAME_TABLE_CLIENTI = "clienti";
	public static final String NAME_TABLE_ADMIN = "admin";
	public static final String NAME_TABLE_AFFITTI = "affitti";
	public static final String NAME_TABLE_PAGAMENTI = "pagamenti";

	public static final String CREATE_DATABASE = "CREATE DATABASE `" + DB_NAME
			+ "` /*!40100 COLLATE 'utf8_general_ci' */;";

	public static final String CREATE_TABLE_BIKES = "CREATE TABLE `biciclette` ("
			+ "	`id` INT(11) NOT NULL AUTO_INCREMENT,"
			+ "	`categoria` VARCHAR(50) NOT NULL,"
			+ "	`equipaggiamento` VARCHAR(50) NULL DEFAULT NULL,"
			+ " `tariffa` DOUBLE NOT NULL, "
			+ "	PRIMARY KEY (`id`),"
			+ " INDEX `categoria_equipaggiamento` (`categoria`, `equipaggiamento`)"
			+ ");";

	public static final String CREATE_TABLE_CLIENTI = "CREATE TABLE `clienti` ("
			+ "	`username` VARCHAR(50) NOT NULL,"
			+ "	`nome` VARCHAR(50) NOT NULL,"
			+ "	`cognome` VARCHAR(50) NOT NULL,"
			+ "	`password` VARCHAR(128) NOT NULL,"
			+ "	`cellulare` VARCHAR(50) NOT NULL,"
			+ "	`email` VARCHAR(50) NOT NULL,"
			+ "	`metodo` VARCHAR(50) NOT NULL,"
			+ "	PRIMARY KEY (`username`),"
			+ "	UNIQUE INDEX `email` (`email`)"
			+ ");";

	public static final String CREATE_TABLE_ADMIN = "CREATE TABLE `admin` ("
			+ "	`username` VARCHAR(50) NOT NULL,"
			+ "	`nome` VARCHAR(50) NOT NULL,"
			+ "	`cognome` VARCHAR(50) NOT NULL,"
			+ "	`password` VARCHAR(128) NOT NULL,"
			+ "	PRIMARY KEY (`username`)"
			+ ");";

	public static final String CREATE_TABLE_AFFITTI = "CREATE TABLE `affitti` ("
			+ "	`codAffitto` VARCHAR(50) NOT NULL,"
			+ "	`cliente` VARCHAR(50) NOT NULL,"
			+ "	`bicicletta` VARCHAR(50) NOT NULL,"
			+ "	`cittaOrigine` VARCHAR(50) NOT NULL,"
			+ "	`cittaDest` VARCHAR(50) NOT NULL,"
			+ "	`durataKM` DOUBLE NOT NULL,"
			+ "	PRIMARY KEY (`codAffitto`),"
			+ "	INDEX `aUtenti` (`cliente`),"
			+ "	INDEX `aBicicletta` (`bicicletta`),"
			+ "	CONSTRAINT `aBicicletta` FOREIGN KEY (`bicicletta`)"
			+ " REFERENCES `biciclette` (`categoria`),"
			+ "	CONSTRAINT `aUtenti` FOREIGN KEY (`cliente`) "
			+ "REFERENCES `clienti` (`username`) ON UPDATE CASCADE ON DELETE CASCADE"
			+ ");";

	public static final String CREATE_TABLE_PAGAMENTI = "CREATE TABLE `pagamenti` ("
			+ "	`id` INT(11) NOT NULL AUTO_INCREMENT,"
			+ "	`cliente` VARCHAR(50) NOT NULL,"
			+ "	`affitto` VARCHAR(50) NOT NULL,"
			+ "	`modalita` VARCHAR(50) NOT NULL,"
			+ "	`costo` DOUBLE NOT NULL,"
			+ "	PRIMARY KEY (`id`),"
			+ "	INDEX `aCliente` (`cliente`),"
			+ "	INDEX `aAffitto` (`affitto`),"
			+ "	CONSTRAINT `aAffitto` FOREIGN KEY (`affitto`) "
			+ "REFERENCES `affitti` (`codAffitto`),"
			+ "	CONSTRAINT `aCliente` FOREIGN KEY (`cliente`)"
			+ " REFERENCES `clienti` (`username`) ON UPDATE CASCADE ON DELETE CASCADE"
			+ ");";

}
