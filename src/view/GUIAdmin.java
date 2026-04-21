package view;

import controller.Database;
import exceptions.BikeDuplicataException;
import exceptions.BikeNotFoundException;
import java.awt.GridLayout;
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
import model.User.Admin;


public class GUIAdmin extends JFrame {

	private static final Logger LOGGER = Logger.getLogger(GUIAdmin.class.getName());
	private Admin admin;
	private JFrame frame;
	private JPanel contentPane;
	private Database db;
	private JTextField textFieldTariffa;
	private JTextField textFieldEquipaggiamento;
	private JTextField textFieldCat;
	private JTextField textField;
	private JButton btnAggiungiEquipaggiamento;
	private JButton btnAggiornaTariffa;
	private JLabel lblNewLabel;

	public GUIAdmin(Admin admin) {
		this.admin = admin;
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
		frame.setBounds(100, 100, 550, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JList<Bike> list = new JList<>();
		list.setModel(getBikes());
		list.setBounds(10, 10, 207, 340);
		panel.add(list);

		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(new GridLayout(12, 2, 0, 0));

		
		JPanel panelCatLabel = new JPanel();
		panel_1.add(panelCatLabel);
		JLabel lblCategoria = new JLabel("Categoria");
		panelCatLabel.add(lblCategoria);

		JPanel panelCatField = new JPanel();
		panel_1.add(panelCatField);
		textFieldCat = new JTextField();
		panelCatField.add(textFieldCat);
		textFieldCat.setColumns(10);

		
		JPanel panelTarLabel = new JPanel();
		panel_1.add(panelTarLabel);
		JLabel lblTariffaBici = new JLabel("Tariffa");
		panelTarLabel.add(lblTariffaBici);

		JPanel panelTarField = new JPanel();
		panel_1.add(panelTarField);
		textField = new JTextField();
		panelTarField.add(textField);
		textField.setColumns(10);

		
		JPanel panelEmpty1 = new JPanel();
		panel_1.add(panelEmpty1);

		JPanel panelAddBike = new JPanel();
		panel_1.add(panelAddBike);
		JButton btnAggiungiBici = new JButton("Aggiungi Bicicletta");
		btnAggiungiBici.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldCat.getText().isEmpty() || textField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Inserisci categoria e tariffa", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				double tariffa;
				try {
					tariffa = Double.parseDouble(textField.getText());
				} catch (NumberFormatException exc) {
					JOptionPane.showMessageDialog(null, "Inserisci un valore numerico per la tariffa", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Bike bike = new Bike(textFieldCat.getText());
				bike.setTariffa(tariffa);
				try {
					db.addBike(bike);
					JOptionPane.showMessageDialog(null, "Bicicletta aggiunta", "Successo", JOptionPane.INFORMATION_MESSAGE);
					list.setModel(getBikes());
					textFieldCat.setText("");
					textField.setText("");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore database", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Errore addBike", e1);
				} catch (BikeDuplicataException | BikeNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Bike duplicata/non trovata", e1);
				}
			}
		});
		panelAddBike.add(btnAggiungiBici);

		
		JPanel panelEmpty2 = new JPanel();
		panel_1.add(panelEmpty2);
		JPanel panelEmpty3 = new JPanel();
		panel_1.add(panelEmpty3);

		
		JPanel panelEquipLabel = new JPanel();
		panel_1.add(panelEquipLabel);
		JLabel lblNuovoEquipaggiamento = new JLabel("Nuovo Equipaggiamento");
		panelEquipLabel.add(lblNuovoEquipaggiamento);

		JPanel panelEquipField = new JPanel();
		panel_1.add(panelEquipField);
		textFieldEquipaggiamento = new JTextField();
		panelEquipField.add(textFieldEquipaggiamento);
		textFieldEquipaggiamento.setColumns(10);

		JPanel panelEmpty4 = new JPanel();
		panel_1.add(panelEmpty4);

		JPanel panelAddEquip = new JPanel();
		panel_1.add(panelAddEquip);
		btnAggiungiEquipaggiamento = new JButton("Aggiungi Equipaggiamento");
		btnAggiungiEquipaggiamento.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldEquipaggiamento.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Inserisci un equipaggiamento", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Bike bike = list.getSelectedValue();
				if (bike == null) {
					JOptionPane.showMessageDialog(null, "Seleziona una bicicletta dalla lista", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					db.addEquipaggiamento(bike, textFieldEquipaggiamento.getText());
					JOptionPane.showMessageDialog(null, "Equipaggiamento aggiunto", "Successo", JOptionPane.INFORMATION_MESSAGE);
					list.setModel(getBikes());
					textFieldEquipaggiamento.setText("");
				} catch (SQLException | BikeDuplicataException | BikeNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Errore: " + e1.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Errore addEquipaggiamento", e1);
				}
			}
		});
		panelAddEquip.add(btnAggiungiEquipaggiamento);

		
		JPanel panelPercLabel = new JPanel();
		panel_1.add(panelPercLabel);
		JLabel lblPercentualeUtilizzo = new JLabel("Percentuale utilizzo");
		panelPercLabel.add(lblPercentualeUtilizzo);

		JPanel panelPercValue = new JPanel();
		panel_1.add(panelPercValue);
		lblNewLabel = new JLabel("");
		panelPercValue.add(lblNewLabel);

		
		JPanel panelNuovaTarLabel = new JPanel();
		panel_1.add(panelNuovaTarLabel);
		JLabel lblNuovaTariffa = new JLabel("Nuova Tariffa");
		panelNuovaTarLabel.add(lblNuovaTariffa);

		JPanel panelNuovaTarField = new JPanel();
		panel_1.add(panelNuovaTarField);
		textFieldTariffa = new JTextField();
		panelNuovaTarField.add(textFieldTariffa);
		textFieldTariffa.setColumns(10);

		JPanel panelEmpty5 = new JPanel();
		panel_1.add(panelEmpty5);

		JPanel panelUpdTar = new JPanel();
		panel_1.add(panelUpdTar);
		btnAggiornaTariffa = new JButton("Aggiorna tariffa");
		btnAggiornaTariffa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (textFieldTariffa.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Inserisci un numero", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				double tariffa;
				try {
					tariffa = Double.parseDouble(textFieldTariffa.getText());
				} catch (NumberFormatException exc) {
					JOptionPane.showMessageDialog(null, "Inserisci un numero valido", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Bike bike = list.getSelectedValue();
				if (bike == null) {
					JOptionPane.showMessageDialog(null, "Seleziona una bicicletta", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					db.aggiornaTariffa(bike, tariffa);
					JOptionPane.showMessageDialog(null, "Tariffa aggiornata", "Successo", JOptionPane.INFORMATION_MESSAGE);
					list.setModel(getBikes());
					textFieldTariffa.setText("");
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore collegamento Database", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.WARNING, "Errore aggiornaTariffa", e1);
				}
			}
		});
		panelUpdTar.add(btnAggiornaTariffa);

		
		list.addListSelectionListener(e1 -> {
			if (!e1.getValueIsAdjusting()) {
				Bike selected = list.getSelectedValue();
				if (selected != null) {
					btnAggiungiEquipaggiamento.setEnabled(true);
					btnAggiornaTariffa.setEnabled(true);
					try {
						double perc = db.visPercentuale(selected);
						lblNewLabel.setText(String.format("%.2f%%", perc));
					} catch (SQLException | BikeNotFoundException | exceptions.UtenteNotFoundException ex) {
						lblNewLabel.setText("N/A");
						LOGGER.log(Level.WARNING, "Errore visPercentuale", ex);
					}
				}
			}
		});
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
