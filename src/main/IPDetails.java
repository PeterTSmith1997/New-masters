package main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dataModel.Analise;
import dataModel.DataStore;
import dataModel.Hits;
import dataModel.helpers.Database;
import dataModel.helpers.IPFunctions;
import net.miginfocom.swing.MigLayout;

/**
 * @author peter
 * @version 18 Jul 2019
 */
public class IPDetails extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Analise analise = new Analise();
	private JPanel buttonPanel;
	private Database database;
	private DataStore dataStore;
	private JPanel highLevelPanel;
	private String ip;
	private String countryCode;
	private IPFunctions ipFunctions = new IPFunctions();
	private JLabel lbCounrtyCode;
	private JLabel lblAllHitsFor;
	private JLabel lblRiskFactor;
	private JLabel lblTimesReported;
	private boolean pane2Loaded = false;
	private JPanel panel;
	private JPanel panel_5;
	private JPanel panelRiskBar;
	private JPanel panelTop;
	private JPanel rawDataPanel;
	private JProgressBar riskBar;
	private JScrollPane sp;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField textFieldCounrtyCode;
	private JTextField textFieldTimes;
	private JTextField textFieldTimesVisted;
	private JTextField textFieldTotalData;
	private int timesVisted;
	private JLabel title;
	private int totalData;
	private JTextArea txtrAllHits;
	private JLabel lblLast30Days;
	private JTextField textFieldLast30Days;
	private JLabel lblAgentOrBot;
	private JTextField textFieldBots;
	private JButton btnFlagAsPossible;

	/**
	 * @param dataStore
	 */
	public IPDetails(DataStore dataStore, String ip) {

		this.dataStore = dataStore;
		this.ip = ip;
        database = new Database();
		makeUi();
	}
	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the timesVisted
	 */
	public int getTimesVisted() {
		return timesVisted;
	}

	/**
	 * @return the totalData
	 */
	public int getTotalData() {
		return totalData;
	}

	public void makeUi() {
		setBounds(100, 100, 969, 586);
		setTitle("Ip details for " + ip);
		setLocationRelativeTo(null);
		setResizable(false);
		// Get data
		setTimesVisted(dataStore.getOrrcancesOfip().get(ip));
		setTotalData(analise.getTotalDataForIP(dataStore.getHits(), ip));
		LinkedList<Hits> hits = dataStore.getHits();
		countryCode = ipFunctions.getLocation(ip);
		panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);

		title = new JLabel("Ip details for " + ip);
		panelTop.add(title);

		panelRiskBar = new JPanel();
		panelRiskBar.setMaximumSize(new Dimension(100, 32767));
		getContentPane().add(panelRiskBar, BorderLayout.SOUTH);
		panelRiskBar.setLayout(new BorderLayout(0, 0));
		double riskRaw = analise.risk(ip, dataStore, countryCode);
		
		int risk = dataStore.getRisks().get(ip);
		lblRiskFactor = new JLabel("Risk Factor: " + risk);
		lblRiskFactor.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRiskFactor.setHorizontalAlignment(SwingConstants.CENTER);
		lblRiskFactor.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelRiskBar.add(lblRiskFactor, BorderLayout.NORTH);

		riskBar = new JProgressBar();
		lblRiskFactor.setLabelFor(riskBar);
		riskBar.setValue(risk);
		riskBar.setOpaque(false);
		
		if (risk < 25) {
			riskBar.setForeground(Color.GREEN);
		} else if (risk < 50) {
			riskBar.setForeground(Color.YELLOW);
		} else if (risk < 75) {
			riskBar.setForeground(Color.ORANGE);
		} else {
			riskBar.setForeground(Color.RED);
		}
		panelRiskBar.add(riskBar, BorderLayout.SOUTH);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		panel_5 = new JPanel();
		tabbedPane.addTab("Summary", null, panel_5, null);
		panel_5.setLayout(new GridLayout(1, 0, 0, 0));

		panel = new JPanel();
		panel_5.add(panel);
		panel.setMinimumSize(new Dimension(10, 200));
		panel.setLayout(new BorderLayout(0, 0));

		buttonPanel = new JPanel();
		buttonPanel.setMaximumSize(new Dimension(50, 32767));
		panel.add(buttonPanel, BorderLayout.NORTH);

		JButton btnReportIp = new JButton("Report IP");
		btnReportIp.setMinimumSize(new Dimension(79, 25));
		btnReportIp.setMaximumSize(new Dimension(79, 25));
		btnReportIp.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnReportIp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(getContentPane(),
						"Report iP" + ip, "Comfrim", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					Database database = new Database();
					database.updateRiskIP(ip, dataStore, riskRaw);
					dataStore.addReportedIP(ip);
					btnReportIp.setEnabled(false);
					textFieldLast30Days.setText(Integer.toString(database.getOcourancesLast30days(ip)));
					textFieldTimes.setText(Integer.toString(database.getOcourances(ip)));
				}

			}
		});
		if (dataStore.getReportedIps().contains(ip)) {
			btnReportIp.setEnabled(false);
		} else {
			btnReportIp.setEnabled(true);
		}
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(btnReportIp);

		highLevelPanel = new JPanel();
		panel.add(highLevelPanel, BorderLayout.CENTER);
		highLevelPanel.setLayout(new MigLayout("", "[246.00][193.00px][347.00px,grow][]", "[70.00px][70.00px][70.00px][70.00px][70.00px][70.00px]"));

		JLabel lblTimesVisted = new JLabel("Times visted");
		highLevelPanel.add(lblTimesVisted, "cell 1 0,alignx center,growy");

		textFieldTimesVisted = new JTextField();
		lblTimesVisted.setLabelFor(textFieldTimesVisted);
		textFieldTimesVisted.setColumns(10);
		textFieldTimesVisted.setText(Integer.toString(timesVisted));
		textFieldTimesVisted.setEditable(false);
		highLevelPanel.add(textFieldTimesVisted, "cell 2 0,alignx center,growy");

		JLabel lblTotalData = new JLabel("Total data sent");
		highLevelPanel.add(lblTotalData, "cell 1 1,alignx center,growy");

		textFieldTotalData = new JTextField();
		lblTotalData.setLabelFor(textFieldTotalData);
		textFieldTotalData.setColumns(10);
		textFieldTotalData.setText(Integer.toString(totalData));
		textFieldTotalData.setEditable(false);
		highLevelPanel.add(textFieldTotalData, "cell 2 1,alignx center,growy");
		
		lbCounrtyCode = new JLabel("Counrty Code");
		highLevelPanel.add(lbCounrtyCode, "cell 1 2,alignx center,growy");
		
		textFieldCounrtyCode = new JTextField();
		textFieldCounrtyCode.setEditable(false);
		highLevelPanel.add(textFieldCounrtyCode, "cell 2 2,alignx center,growy");
		textFieldCounrtyCode.setColumns(10);
		textFieldCounrtyCode.setText(countryCode);
		
		lblTimesReported = new JLabel("Times reported");
		lblTimesReported.setLabelFor(textFieldTimes);
		highLevelPanel.add(lblTimesReported, "cell 1 3,alignx center,growy");
		
		textFieldTimes = new JTextField();
		textFieldTimes.setEditable(false);
		highLevelPanel.add(textFieldTimes, "cell 2 3,alignx center,growy");
		textFieldTimes.setColumns(10);
		textFieldTimes.setText(Integer.toString(database.getOcourances(ip)));
		
		lblLast30Days = new JLabel("Reports last 30 days");
		highLevelPanel.add(lblLast30Days, "cell 1 4,alignx center,growy");
		
		textFieldLast30Days = new JTextField();
		textFieldLast30Days.setEditable(false);
		textFieldLast30Days.setText(Integer.toString(database.getOcourancesLast30days(ip)));
		highLevelPanel.add(textFieldLast30Days, "cell 2 4,alignx center,growy");
		textFieldLast30Days.setColumns(10);
		
		lblAgentOrBot = new JLabel("Agent or bot");
		highLevelPanel.add(lblAgentOrBot, "cell 1 5,alignx center");
		
		
		
		btnFlagAsPossible = new JButton("Flag as possible bot");
		btnFlagAsPossible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String userAgent= "";
				for (int i = 0; i < hits.size(); i++) {
					if (hits.get(i).getiPaddr().equals(ip)) {
						userAgent = hits.get(i).getUserAgent();
						break;
					}
				}
				database.reportPossibleBot(ip, userAgent);
			}
		});
		textFieldBots = new JTextField();
		textFieldBots.setEditable(false);
		if (database.knownBots(ip).equals("n/a")) {
			textFieldBots.setText("Not listed");
		}else {
			btnFlagAsPossible.setEnabled(false);
			btnReportIp.setEnabled(false);
			textFieldBots.setText(database.knownBots(ip));
		}
		highLevelPanel.add(textFieldBots, "cell 2 5,alignx center,growy");
		textFieldBots.setColumns(10);
		highLevelPanel.add(btnFlagAsPossible, "cell 3 5");
		

		rawDataPanel = new JPanel();
		tabbedPane.addTab("Raw Data", null, rawDataPanel, "View raw data here");
		rawDataPanel.setLayout(new BorderLayout(0, 0));

		lblAllHitsFor = new JLabel("All hits for this IP");
		lblAllHitsFor.setHorizontalAlignment(SwingConstants.CENTER);
		rawDataPanel.add(lblAllHitsFor, BorderLayout.NORTH);

		txtrAllHits = new JTextArea(6, 0);
		txtrAllHits.setMaximumSize(new Dimension(100, 21));
		txtrAllHits.setAlignmentX(Component.LEFT_ALIGNMENT);
		txtrAllHits.setAlignmentY(Component.TOP_ALIGNMENT);
		txtrAllHits.setWrapStyleWord(true);
		txtrAllHits.setLineWrap(true);
		
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int pane = tabbedPane.getSelectedIndex();
				if (pane == 1 && !pane2Loaded) {
					for (int i = 0; i < hits.size(); i++) {
						if (hits.get(i).getiPaddr().equals(ip)) {
							txtrAllHits.append(hits.get(i) + "\n");
						}
						pane2Loaded = true;
						txtrAllHits.setSelectionStart(0);
						txtrAllHits.setSelectionEnd(0);
					}
				}
			}
		});
		sp = new JScrollPane(txtrAllHits,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		lblAllHitsFor.setLabelFor(sp);
		sp.setMaximumSize(new Dimension(100, 21));
		rawDataPanel.add(sp);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				new LogData(dataStore);
				dispose();
			}
		});
	}

	/**
	 * @param timesVisted
	 *            the timesVisted to set
	 */
	public void setTimesVisted(int timesVisted) {
		this.timesVisted = timesVisted;
	}

	/**
	 * @param totalData
	 *            the totalData to set
	 */
	public void setTotalData(int totalData) {
		this.totalData = totalData;
	}
}