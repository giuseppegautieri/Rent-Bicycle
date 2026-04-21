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
import javax.swing.border.EmptyBorder;

import controller.Database;
import exceptions.UtenteDuplicatoException;
import exceptions.UtenteNotFoundException;
import model.User.Cliente;


public class RegisterCliente extends JFrame {

	private static final Logger LOGGER = Logger.getLogger(RegisterCliente.class.getName());
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JTextField textFieldNome;
	private JTextField textFieldCognome;
	private JTextField textFieldEmail;
	private JTextField textFieldCellulare;
	private JTextField textFieldPagamento;
	private JFrame frame;

	public RegisterCliente() {
		frame = this;
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(11, 2, 0, 0));

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

		Box horizontalBox_11 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_11);
		JLabel lblEmail = new JLabel("Email");
		horizontalBox_11.add(lblEmail);

		Box horizontalBox_10 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_10);
		textFieldEmail = new JTextField();
		horizontalBox_10.add(textFieldEmail);
		textFieldEmail.setColumns(10);

		Box horizontalBox_13 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_13);
		JLabel lblCellulare = new JLabel("Cellulare");
		horizontalBox_13.add(lblCellulare);

		Box horizontalBox_12 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_12);
		textFieldCellulare = new JTextField();
		horizontalBox_12.add(textFieldCellulare);
		textFieldCellulare.setColumns(10);

		Box horizontalBox_16 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_16);
		JLabel lblModalitaPagamento = new JLabel("Modalita pagamento");
		horizontalBox_16.add(lblModalitaPagamento);

		Box horizontalBox_15 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_15);
		textFieldPagamento = new JTextField();
		horizontalBox_15.add(textFieldPagamento);
		textFieldPagamento.setColumns(10);

		Box horizontalBox_14 = Box.createHorizontalBox();
		contentPane.add(horizontalBox_14);

		JButton btnRegistrati = new JButton("Registrati");
		btnRegistrati.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String pw = new String(passwordField.getPassword());
				String pw1 = new String(passwordField_1.getPassword());

				if (pw.isEmpty() || pw1.isEmpty() || textFieldUsername.getText().isEmpty()
						|| textFieldNome.getText().isEmpty() || textFieldCognome.getText().isEmpty()
						|| textFieldEmail.getText().isEmpty() || textFieldCellulare.getText().isEmpty()
						|| textFieldPagamento.getText().isEmpty()) {
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
				if (!textFieldEmail.getText().contains("@")) {
					JOptionPane.showMessageDialog(null, "Inserisci un indirizzo email valido", "Errore", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Cliente cliente = new Cliente();
				cliente.setUsername(textFieldUsername.getText());
				cliente.setNome(textFieldNome.getText());
				cliente.setCognome(textFieldCognome.getText());
				cliente.setEmail(textFieldEmail.getText());
				cliente.setPassword(pw);
				cliente.setMetodo(textFieldPagamento.getText());
				cliente.setCellulare(textFieldCellulare.getText());
				try {
					Database db = Database.getDatabase();
					db.addCliente(cliente);
					JOptionPane.showMessageDialog(null, "Registrazione effettuata con successo", "Registrazione", JOptionPane.INFORMATION_MESSAGE);
					frame.dispose();
				} catch (UtenteDuplicatoException e1) {
					JOptionPane.showMessageDialog(null, "Username gia presente", "Errore", JOptionPane.ERROR_MESSAGE);
				} catch (UtenteNotFoundException e1) {
					LOGGER.log(Level.WARNING, "Errore imprevisto", e1);
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Errore collegamento database", "Errore", JOptionPane.ERROR_MESSAGE);
					LOGGER.log(Level.SEVERE, "Errore DB registrazione cliente", e1);
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
