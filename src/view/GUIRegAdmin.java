package view;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controller.Database;
import exceptions.UtenteDuplicatoException;
import exceptions.UtenteNotFoundException;
import model.User.Admin;


public class GUIRegAdmin extends JFrame {

	private static final Logger LOGGER = Logger.getLogger(GUIRegAdmin.class.getName());
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textFieldNome;
	private JTextField textFieldCognome;
	private JFrame frame;

	public GUIRegAdmin() {
		frame = this;
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(9, 2, 0, 0));

		Box horizontalBox_1 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_1);
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setAlignmentX(Component.RIGHT_ALIGNMENT);
		horizontalBox_1.add(lblUsername);

		Box horizontalBox_2 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_2);
		textFieldUsername = new JTextField();
		horizontalBox_2.add(textFieldUsername);
		textFieldUsername.setColumns(10);

		Box horizontalBox_3 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_3);
		JLabel lblPassword = new JLabel("Password");
		horizontalBox_3.add(lblPassword);

		Box horizontalBox_4 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_4);
		passwordField = new JPasswordField();
		horizontalBox_4.add(passwordField);

		Box horizontalBox_5 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_5);
		JLabel lblConfermaPassword = new JLabel("Conferma Password");
		horizontalBox_5.add(lblConfermaPassword);

		Box horizontalBox_6 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_6);
		passwordField_1 = new JPasswordField();
		horizontalBox_6.add(passwordField_1);

		Box horizontalBox_7 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_7);
		JLabel lblNome = new JLabel("Nome");
		horizontalBox_7.add(lblNome);

		Box horizontalBox_8 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_8);
		textFieldNome = new JTextField();
		horizontalBox_8.add(textFieldNome);
		textFieldNome.setColumns(10);

		Box horizontalBox_9 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_9);
		JLabel lblCognome = new JLabel("Cognome");
		horizontalBox_9.add(lblCognome);

		Box horizontalBox = Box.createHorizontalBox();
		contentPane.add(horizontalBox);
		textFieldCognome = new JTextField();
		horizontalBox.add(textFieldCognome);
		textFieldCognome.setColumns(10);

		// Empty boxes for spacing
		contentPane.add(Box.createHorizontalBox());
		contentPane.add(Box.createHorizontalBox());
		contentPane.add(Box.createHorizontalBox());
		contentPane.add(Box.createHorizontalBox());

		Box horizontalBox_14 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_14);

		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String pw = new String(passwordField.getPassword());
				String pw1 = new String(passwordField_1.getPassword());

				if (pw.isEmpty() || pw1.isEmpty() || textFieldUsername.getText().isEmpty()
						|| textFieldNome.getText().isEmpty() || textFieldCognome.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Controlla i campi, tutti sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (pw.length() < 4) {
					JOptionPane.showMessageDialog(null, "La password deve avere almeno 4 caratteri", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!pw.equals(pw1)) {
					JOptionPane.showMessageDialog(null, "Le password non coincidono", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Admin admin = new Admin();
				admin.setUsername(textFieldUsername.getText());
				admin.setNome(textFieldNome.getText());
				admin.setCognome(textFieldCognome.getText());
				admin.setPassword(pw);
				try {
					Database db = Database.getDatabase();
					db.addAdmin(admin);
					JOptionPane.showMessageDialog(null, "Registrazione effettuata con successo", "Registrazione", JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
				} catch (UtenteDuplicatoException e1) {
					JOptionPane.showMessageDialog(null, "Username gia presente", "Errore", JOptionPane.ERROR_MESSAGE);
				} catch (UtenteNotFoundException e1) {
					LOGGER.log(Level.WARNING, "Errore imprevisto", e1);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore collegamento database", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.SEVERE, "Errore DB registrazione admin", e1);
				}
			}
		});
		horizontalBox_14.add(btnRegistrati);

		Box verticalBox = Box.createVerticalBox();
		contentPane.add(verticalBox);
		JButton btnAnnulla = new JButton("Annulla");
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		verticalBox.add(btnAnnulla);
	}

}
