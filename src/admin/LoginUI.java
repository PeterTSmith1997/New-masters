package admin;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dataModel.DataStore;
import dataModel.helpers.Database;
import main.LogData;
import net.miginfocom.swing.MigLayout;

/**
 * @author 16018262
 * @version 13 Dec 2019
 */
public class LoginUI extends JFrame {
	
	private static final long serialVersionUID = 8342538030734597748L;
	private static String username, passwordString;
	private static char[] password;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private Database database = new Database();
	private JButton btnCancel;
	private JButton btnLogin;
	private JPanel buttonPnl;
	private JLabel lblPassword;
	private JLabel lblUsername;

	/**
	 * Create the frame.
	 * @Param dataStore
	 */
	public LoginUI(DataStore dataStore) {
		setResizable(false);
		setLocationRelativeTo(null);
		setBounds(100, 100, 370, 196);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[89px,grow][177px]",
				"[20px][20px][53.00px]"));

		txtUsername = new JTextField();
		contentPane.add(txtUsername, "cell 1 0,growx,aligny top");
		txtUsername.setColumns(10);

		txtPassword = new JPasswordField();
		contentPane.add(txtPassword, "cell 1 1,growx,aligny top");

		lblUsername = new JLabel("Username");
		contentPane.add(lblUsername, "cell 0 0,growx,aligny center");

		lblPassword = new JLabel("Password");
		contentPane.add(lblPassword, "cell 0 1,growx,aligny center");

		buttonPnl = new JPanel();
		contentPane.add(buttonPnl, "cell 1 2,growx,aligny top");
		buttonPnl.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get values from user input
				username = txtUsername.getText().trim();
				password = txtPassword.getPassword();
				// Convert password input to String
				passwordString = new String(password);
				passwordString.trim();
				// Check if username password combo matches
				// If login successful
				if (database.validateLogin(username, passwordString)) {
					// Welcome message
					JOptionPane.showMessageDialog(null, "Welcome, " + username,
							"Success", JOptionPane.INFORMATION_MESSAGE);
					new AdminUI(dataStore);
					// Close login window
					dispose();
				}
				// If login failed
				else {
					// Error message informing user of incorrect login
					JOptionPane.showMessageDialog(null,
							"Username and password do not match.",
							"Login Failed", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		buttonPnl.add(btnLogin);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LogData(dataStore);
				dispose();
			}
		});
		buttonPnl.add(btnCancel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				new LogData(dataStore);
				dispose();
			}
		});
	}
}