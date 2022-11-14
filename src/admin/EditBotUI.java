package admin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dataModel.DataStore;
import dataModel.helpers.Database;
import net.miginfocom.swing.MigLayout;

/**
 * @author 16018262
 * @version 12 Dec 2019
 */
public class EditBotUI extends JFrame{
	private JFrame ui;
	private DataStore dataStore;
	private String ip;
	private Database database;
	private JPanel Panel;
	private JLabel lblIp;
	private JTextField IpAddressText;
	private JLabel lblType;
	private JComboBox<String> typeComboBox;
	private JLabel lblDiscription;
	private JLabel lblUserAgent;
	private JTextField textField;
	private Map<String, String> botInfo = new HashMap<String, String>();
	private JTextArea botDiscriptioTextArea;
	private JButton btnSave;
	
	/**
	 *  Main class to make a BOT Editing UI
	 * @param dataStore
	 * @param ip The IP of the BOT to be Edited
	 */
	public EditBotUI(DataStore dataStore, String ip) {
		this.dataStore = dataStore;
		this.ip = ip;
		database = new Database();
		botInfo = database.getBotInfo(ip);
		makeUi(); 
	}
	
	/**
	 * Makes a UI
	 */
	private void makeUi() {
		ui = new JFrame();
		setLocationRelativeTo(null);
		setResizable(false);
		setTitle("Add bots");
		getContentPane().setLayout(new BorderLayout(0,0));
		
		setBounds(100, 100, 1169, 686);
		
		Panel = new JPanel();
		getContentPane().add(Panel, BorderLayout.NORTH);
		
		JLabel lblAddBots = new JLabel("Add bots");
		Panel.add(lblAddBots);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new MigLayout("", "[98.00][][807.00,grow][]", "[][][][][][]"));
		
		lblIp = new JLabel("IP ");
		panel.add(lblIp, "cell 0 0,alignx center");
		
		IpAddressText = new JTextField();
		panel.add(IpAddressText, "cell 2 0,growx");
		IpAddressText.setColumns(10);
		IpAddressText.setText(ip);
		IpAddressText.setEditable(false);
		
		lblType = new JLabel("Type");
		panel.add(lblType, "cell 0 1,alignx center");
		
		typeComboBox = new JComboBox<String>();
		panel.add(typeComboBox, "cell 2 1");
		
		/**
		 * Make a drop down box with all the BOT categories
		 * If statement to select the BOT type that is held in the database
		 */
		ArrayList<String> botCategories = database.getBotCats();
		for (int i = 0; i < botCategories.size(); i++) {
			typeComboBox.addItem(botCategories.get(i));
			if (botInfo.get("cat").equals(botCategories.get(i).substring(0, 1))) {
				typeComboBox.setSelectedItem(botCategories.get(i));
			}
			
		}
		
		lblDiscription = new JLabel("Discription");
		panel.add(lblDiscription, "cell 0 2,alignx center");
		
		botDiscriptioTextArea = new JTextArea();
		panel.add(botDiscriptioTextArea, "cell 2 2,grow");
		
		lblUserAgent = new JLabel("User Agent");
		panel.add(lblUserAgent, "cell 0 3,alignx center");
		
		textField = new JTextField();
		panel.add(textField, "cell 2 3,growx");
		textField.setColumns(10);
		botDiscriptioTextArea.setText(botInfo.get("Discription"));
		textField.setText(botInfo.get("Name"));
		
		/**
		 * When the save button is clicked put all the data into a map to pass to the database
		 */
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String category = typeComboBox.getSelectedItem().toString().substring(0,1);
				botInfo.clear();
				botInfo.put("category", category);
				botInfo.put("ip", ip);
				botInfo.put("Discription", botDiscriptioTextArea.getText().trim());
				botInfo.put("Name", textField.getText().trim());
				
				if(database.updateBotInfo(botInfo)) {
					JOptionPane.showMessageDialog(null, "Bot added",
							"Info", JOptionPane.INFORMATION_MESSAGE);
				}else {
					JOptionPane.showMessageDialog(null, "Error adding Bot",
							"error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		panel.add(btnSave, "cell 0 5,alignx center");
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
