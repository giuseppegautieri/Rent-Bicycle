package view;

import controller.Affitto;
import controller.Database;
import controller.Pagamento;
import exceptions.AffittoDuplicatoException;
import exceptions.BikeNotFoundException;
import exceptions.UtenteNotFoundException;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Bike.Bike;
import model.Parcheggio.Parcheggio;
import model.User.AbstractUtente;
import model.User.Cliente;


public class GUICliente {

	private static final Logger LOGGER = Logger.getLogger(GUICliente.class.getName());
	private JFrame frame;
	private JTextField textFieldCittaOr;
	private JTextField textFieldCittaDest;
	private JTextField textFieldKm;
	private Cliente cliente;
	private Affitto affitto;
	private Database db;

	public GUICliente(AbstractUtente utente) {
		this.cliente = (Cliente) utente;
		try {
			db = Database.getDatabase();
			initialize();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Errore collegamento database", "Errore", JOptionPane.ERROR_MESSAGE);
			LOGGER.log(Level.SEVERE, "Errore connessione DB", e);
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JList<Bike> list = new JList<>();
		list.setModel(this.getBikes());
		list.setBounds(10, 10, 207, 242);
		panel.add(list);

		Panel panel_1 = new Panel();
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(9, 2, 0, 0));

		JLabel lblCittaPartenza = new JLabel("Citta di partenza");
		panel_1.add(lblCittaPartenza);

		textFieldCittaOr = new JTextField();
		panel_1.add(textFieldCittaOr);
		textFieldCittaOr.setColumns(10);

		JLabel lblCittaDestinazione = new JLabel("Citta di destinazione");
		panel_1.add(lblCittaDestinazione);

		textFieldCittaDest = new JTextField();
		panel_1.add(textFieldCittaDest);
		textFieldCittaDest.setColumns(10);

		JLabel lblKmPercorsi = new JLabel("Km percorsi");
		panel_1.add(lblKmPercorsi);

		textFieldKm = new JTextField();
		textFieldKm.setText("");
		panel_1.add(textFieldKm);
		textFieldKm.setColumns(10);

		// Filler panels
		for (int i = 0; i < 6; i++) panel_1.add(new Panel());

		JButton btnPaga = new JButton("Paga");
		btnPaga.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (affitto == null) {
					JOptionPane.showMessageDialog(null, "Effettua prima un affitto", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Pagamento pagamento = new Pagamento();
				pagamento.setAffitto(affitto);
				pagamento.setCliente(cliente);
				try {
					double costo = db.addPagamento(pagamento);
					JOptionPane.showMessageDialog(null, "Pagamento riuscito, hai pagato: " + costo, "Pagamento", JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Pagamento non riuscito", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Errore pagamento", e1);
				}
			}
		});
		btnPaga.setEnabled(false);
		panel_1.add(btnPaga);

		JButton btnAffitta = new JButton("Affitta");
		btnAffitta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Bike bikeSelezionata = list.getSelectedValue();
				if (bikeSelezionata == null) {
					JOptionPane.showMessageDialog(null, "Seleziona una bicicletta", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (textFieldCittaOr.getText().isEmpty() || textFieldCittaDest.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Inserisci citta di partenza e destinazione", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				double km;
				try {
					km = Double.parseDouble(textFieldKm.getText());
					if (km <= 0) throw new NumberFormatException();
				} catch (NumberFormatException exc) {
					JOptionPane.showMessageDialog(null, "Inserisci un numero valido per i km (maggiore di 0)", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				affitto = new Affitto();
				affitto.setBike(bikeSelezionata);
				affitto.setCliente(cliente);
				affitto.setOrigine(new Parcheggio(textFieldCittaOr.getText()));
				affitto.setDestinazione(new Parcheggio(textFieldCittaDest.getText()));
				affitto.setDurataKm(km);
				try {
					db.addAffitto(affitto);
					JOptionPane.showMessageDialog(null, "Affitto eseguito con successo", "Affitto", JOptionPane.INFORMATION_MESSAGE);
					btnPaga.setEnabled(true);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore durante l'affitto", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Errore addAffitto", e1);
				} catch (AffittoDuplicatoException | BikeNotFoundException | UtenteNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Affitto non valido: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Affitto non valido", e1);
				}
			}
		});
		panel_1.add(btnAffitta);
	}

	private DefaultListModel<Bike> getBikes() {
		List<Bike> bikes = new ArrayList<>();
		try {
			db = Database.getDatabase();
			bikes = db.getBikes();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Errore collegamento database", "Errore", JOptionPane.ERROR_MESSAGE);
			LOGGER.log(Level.WARNING, "Errore getBikes", e);
		}
		DefaultListModel<Bike> model = new DefaultListModel<>();
		for (Bike bike : bikes)
			model.addElement(bike);
		return model;
	}
}
