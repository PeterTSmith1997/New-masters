package admin;

import javax.swing.JFrame;

import dataModel.DataStore;
import dataModel.helpers.Database;
import main.LogData;
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author 16018262
 * @version 11 Dec 2019
 */
public class AdminUI extends JFrame {
	private DataStore dataStore;
	private JLabel lblReportedIpsLast;
	private JTable tblLast30Days;
	private JTable tblAllTimeReports;
	private Database database;
	private JButton txtUpdateCountryRisks;
	private JButton btnLogOut;
	private JPanel panel_2;
	private JLabel lblXx;
	private JLabel label;
	private JLabel lblTotalReportsAll;
	private JLabel lblTotalReportsLast;
	private JLabel lblAllTimeReports;
	private JScrollPane scrollPane;
	private JPanel panel_1;
	private JPanel panel;
	private JScrollPane scrollPane_2;
	private JTable table;
	private JLabel lblPossibleBots;
	private JScrollPane scrollPane_3;
	private JTable knownBotsTbl;
	private JLabel lblKnownBots;

	/**
	 * Main method for creating an admin UI
	 * @param dataStore
	 */
	public AdminUI(DataStore dataStore) {
	
		database = new Database();
		this.dataStore = dataStore;
		makeGUI();

	}

	/**
	 * Method to create the UI itself
	 */
	public void makeGUI() {
		setLocationRelativeTo(null);
		setTitle("Admin");
		getContentPane().setLayout(new BorderLayout(0, 0));

		setBounds(100, 100, 1169, 686);

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblWelcome = new JLabel("Welcome");
		panel.add(lblWelcome);

		panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(
				new MigLayout("", "[275.00][][275.00][][:374.00:329.00,grow][][grow]", "[28.00][grow][]"));

		lblReportedIpsLast = new JLabel("Reported ips last 30 days");
		panel_1.add(lblReportedIpsLast, "cell 0 0,alignx center");

		lblAllTimeReports = new JLabel("All time reports");
		panel_1.add(lblAllTimeReports, "cell 2 0,alignx center,aligny center");
		
		lblPossibleBots = new JLabel("Possible bots");
		panel_1.add(lblPossibleBots, "cell 4 0,alignx center");
		
		lblKnownBots = new JLabel("Known bots");
		panel_1.add(lblKnownBots, "cell 6 0,alignx center");

		scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "cell 0 1,grow");

		tblLast30Days = new JTable();
		tblLast30Days.setModel(database.getRisk30DaysAdmin());
		scrollPane.setViewportView(tblLast30Days);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, "cell 2 1,alignx center,growy");

		tblAllTimeReports = new JTable();

		tblAllTimeReports.setModel(database.getTotalReports());
		
		scrollPane_1.setViewportView(tblAllTimeReports);

		scrollPane_2 = new JScrollPane();
		panel_1.add(scrollPane_2, "cell 4 1,grow");

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				String ip = (String) table.getValueAt(row, 0);
				EditBotUI botUI = new EditBotUI(dataStore, ip);
				dispose();
			}
		});
		table.setModel(database.getPosibleBots());
		scrollPane_2.setViewportView(table);
		
		scrollPane_3 = new JScrollPane();
		panel_1.add(scrollPane_3, "cell 6 1,grow");
		
		knownBotsTbl = new JTable();
		knownBotsTbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				String ip = (String) table.getValueAt(row, 0);
				EditBotUI botUI = new EditBotUI(dataStore, ip);
				dispose();
			}
		});
			knownBotsTbl.setModel(database.getKnownBots());
		scrollPane_3.setViewportView(knownBotsTbl);

		panel_2 = new JPanel();
		panel_1.add(panel_2, "cell 2 2,grow");
		panel_2.setLayout(new MigLayout("",
				"[160.00,center][33.00,center][146.00,grow,center]",
				"[][][][][][]"));

		lblTotalReportsLast = new JLabel("Total reports last 30  days");
		lblTotalReportsLast.setHorizontalAlignment(SwingConstants.CENTER);
		panel_2.add(lblTotalReportsLast, "cell 0 0,grow");

		lblXx = new JLabel(Integer.toString(database.numberReports30Days()));
		panel_2.add(lblXx, "cell 2 0,alignx center");

		lblTotalReportsAll = new JLabel("Total reports all time");
		panel_2.add(lblTotalReportsAll, "cell 0 2");

		label = new JLabel(Integer.toString(database.numberReportsAllTime()));
		panel_2.add(label, "cell 2 2");

		btnLogOut = new JButton("log out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LogData logData = new LogData(dataStore);
				dispose();
			}
		});

		txtUpdateCountryRisks = new JButton();
		txtUpdateCountryRisks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdateCountryRiskUI countryRiskUI = new UpdateCountryRiskUI(
						dataStore);
				dispose();
			}
		});
		txtUpdateCountryRisks.setText("Update Country risks");
		panel_2.add(txtUpdateCountryRisks, "cell 2 4,alignx center");
		panel_2.add(btnLogOut, "cell 2 5");
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				LogData logData = new LogData(dataStore);
				dispose();
			}
		});
	}

}
