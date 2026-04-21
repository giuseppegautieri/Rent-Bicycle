package view;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTextField;

import commons.PasswordUtils;
import controller.Database;
import exceptions.UtenteNotFoundException;
import model.User.AbstractUtente;
import model.User.Admin;
import model.User.Cliente;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;


public class Root {

	private static final Logger LOGGER = Logger.getLogger(Root.class.getName());
	private JFrame frmMyBikes;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Root window = new Root();
					window.frmMyBikes.setVisible(true);
				} catch (Exception e) {
					LOGGER.log(Level.SEVERE, "Errore avvio applicazione", e);
				}
			}
		});
	}


	public Root() {
		initialize();
	}


	private void initialize() {
		frmMyBikes = new JFrame();
		frmMyBikes.setTitle("My Bikes");
		frmMyBikes.setBounds(100, 100, 450, 300);
		frmMyBikes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMyBikes.getContentPane().setLayout(null);

		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(190, 35, 145, 20);
		frmMyBikes.getContentPane().add(textFieldUsername);
		textFieldUsername.setColumns(10);

		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(87, 38, 93, 14);
		frmMyBikes.getContentPane().add(lblUsername);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(87, 63, 93, 14);
		frmMyBikes.getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(190, 60, 145, 20);
		frmMyBikes.getContentPane().add(passwordField);

		JButton btnLogin = new JButton("Login");
		btnLogin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (textFieldUsername.getText().isEmpty() || passwordField.getPassword().length == 0) {
					JOptionPane.showMessageDialog(null, "Inserisci i campi", "alert", JOptionPane.ERROR_MESSAGE);
				} else {
					String username = textFieldUsername.getText();
					String pw = new String(passwordField.getPassword());
					Database db = null;
					try {
						db = Database.getDatabase();
						
						try {
							Cliente cliente = db.getUtente(username);
							if (PasswordUtils.verifyPassword(pw, cliente.getPassword())) {
								new GUICliente(cliente);
								frmMyBikes.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Password non corretta", "Errore", JOptionPane.ERROR_MESSAGE);
							}
							return;
						} catch (UtenteNotFoundException e) {
							
						}
						
						try {
							Admin admin = db.getAdmin(username);
							if (PasswordUtils.verifyPassword(pw, admin.getPassword())) {
								new GUIAdmin(admin);
								frmMyBikes.dispose();
							} else {
								JOptionPane.showMessageDialog(null, "Password non corretta", "Errore", JOptionPane.ERROR_MESSAGE);
							}
						} catch (UtenteNotFoundException e1) {
							JOptionPane.showMessageDialog(null, "Utente non trovato", "Errore", JOptionPane.ERROR_MESSAGE);
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "Errore collegamento database", "Errore", JOptionPane.ERROR_MESSAGE);
						LOGGER.log(Level.SEVERE, "Errore DB durante login", e);
					}
				}
			}
		});
		btnLogin.setBounds(156, 118, 124, 23);
		frmMyBikes.getContentPane().add(btnLogin);

		JButton btnRegistraCliente = new JButton("Registra cliente");
		btnRegistraCliente.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new RegisterCliente();
			}
		});
		btnRegistraCliente.setBounds(156, 163, 124, 23);
		frmMyBikes.getContentPane().add(btnRegistraCliente);

		JButton btnRegistraAdmin = new JButton("Registra admin");
		btnRegistraAdmin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new GUIRegAdmin();
			}
		});
		btnRegistraAdmin.setBounds(156, 197, 124, 23);
		frmMyBikes.getContentPane().add(btnRegistraAdmin);
	}
}
