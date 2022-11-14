package admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dataModel.DataStore;
import dataModel.helpers.Database;
import net.miginfocom.swing.MigLayout;

/**
 * @author 16018262
 * @version 13 Dec 2019
 */
public class UpdateCountryRiskUI extends JFrame {
	private DataStore dataStore;
	private JComboBox<String> countryDropdown;
	private JLabel lblCountryCode;
	private Database database;
	private JTextField textFieldRisk;
	private JButton btnExit;
	private JButton btnSave;

	/**
	 * @param dataStore
	 */
	public UpdateCountryRiskUI(DataStore dataStore) {
		
		this.dataStore = dataStore;
		database = new Database();
		makeUI();
	}

	/**
	 * Makes a UI to update the country risk
	 */
	public void makeUI() {
		setTitle("Update Risk");
		setSize(451, 170);
		setLocationRelativeTo(null);
				setResizable(false);
		getContentPane().setLayout(
				new MigLayout("", "[87.00][][grow]", "[][][][][][][]"));

		lblCountryCode = new JLabel("Country code");
		lblCountryCode.setHorizontalTextPosition(SwingConstants.CENTER);
		lblCountryCode.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(lblCountryCode, "cell 0 0,alignx center");

		countryDropdown = new JComboBox<String>();
		countryDropdown.setMaximumRowCount(10);
		ArrayList<String> counties = database.getCounties();
		countryDropdown.addItem("Select a country");
		for (int i = 0; i < counties.size(); i++) {
			countryDropdown.addItem(counties.get(i));
		}
		countryDropdown.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					if (!countryDropdown.getSelectedItem()
							.equals("Select a country")) {
						String code = countryDropdown.getSelectedItem().toString().substring(0, 2);
						textFieldRisk.setText(
								Double.toString(database.countryRisk(code)));
					}
				}
			}
		});

		getContentPane().add(countryDropdown, "cell 2 0,alignx center");

		JLabel lblRiskScore = new JLabel("Risk score");
		getContentPane().add(lblRiskScore, "cell 0 3,alignx center");

		textFieldRisk = new JTextField();
		getContentPane().add(textFieldRisk, "cell 2 3,alignx center");
		textFieldRisk.setColumns(10);

		btnExit = new JButton("Exit");
		getContentPane().add(btnExit, "cell 0 6,alignx center");

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int risk = Integer.parseInt(textFieldRisk.getText());
				String code = countryDropdown.getSelectedItem().toString();
				boolean error = false;
				if (risk < 0) {
					error=true;
					JOptionPane.showMessageDialog(null,
							"Risk must be higher than 0", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				if (risk > 100) {
					error=true;
					JOptionPane.showMessageDialog(null,
							"Risk must be less than 100", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				if (countryDropdown.getSelectedItem().toString().equals("Select a country")) {
					error=true;
					JOptionPane.showMessageDialog(null, "Select a country", "error", JOptionPane.ERROR_MESSAGE);
				}
				if (!error) {
					database.updateCountryRisk(code.substring(0, 2), risk);
				}
			}
		});
		getContentPane().add(btnSave, "cell 2 6,alignx center");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				new AdminUI(dataStore);
				dispose();
			}
		});
		setVisible(true);
	}
}
