package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import admin.LoginUI;
import dataModel.Analise;
import dataModel.DataStore;
import dataModel.Risk;
import dataModel.helpers.Database;
import dataModel.helpers.IPFunctions;
import dataModel.helpers.Reader;
import net.miginfocom.swing.MigLayout;


/**
 * @author peter
 * @version 19 Jul 2019
 */
public class LogData extends JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = -5672269806381056292L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new LogData();
			}
		});
	}

	private JFrame frmLogFileReader;
	private JScrollPane botsScrollPane;
	private JTable tbMain;
	private DefaultTableModel mainsMd;
	private JPanel serchIPPL;
	private int timesHitPages = 0;
	private int timesHitIp = 0;
	private JButton btnReadFile;
	private Reader reader;

	private DataStore dataStore;
	private Font deflautFont;
	private JButton btnAdmin;
	private JPanel panel;
	private JLabel lblNewLabel;

	public LogData() {
		dataStore = new DataStore();
		Database database = new Database();
		dataStore.setProtocalScores(database.Setprotcalscores());
		dataStore.setUrlSorces(database.setURLScores());

		reader = new Reader(dataStore);
		makeui();

	}

	/**
	 * @param dataStore
	 */
	public LogData(DataStore dataStore) {
		this.dataStore = dataStore;
		makeui();	
		reader = new Reader(dataStore);
		updateGUI();

	}

	/**
	 * @return the timesHitIp
	 */
	public int getTimesHitIp() {
		return timesHitIp;
	}

	/**
	 * @return the timesHitPages
	 */
	public int getTimesHitPages() {
		return timesHitPages;
	}

	public void makeui() {
		frmLogFileReader = new JFrame();
		frmLogFileReader.setResizable(false);
		frmLogFileReader.setTitle("Log file reader");
		frmLogFileReader.setExtendedState(Frame.MAXIMIZED_BOTH);
		frmLogFileReader.setBounds(100, 100, 1169, 686);
		frmLogFileReader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		frmLogFileReader.getContentPane().setLayout(new MigLayout("", "[654px,grow]", "[17px][715px][grow][85px][106px,grow]"));
        deflautFont = new Font("Tahoma", Font.BOLD, 14);
		JLabel lbIPs = new JLabel("IPs on site");
		lbIPs.setHorizontalAlignment(SwingConstants.CENTER);
		lbIPs.setFont(deflautFont);
		frmLogFileReader.getContentPane().add(lbIPs,
				"cell 0 0,alignx center,aligny center");

		botsScrollPane = new JScrollPane();
		frmLogFileReader.getContentPane().add(botsScrollPane, "cell 0 1,grow");

		tbMain = new JTable();
		String ipHeader[] = new String[] { "IP", "Frequency", "Risk" };
		mainsMd = new DefaultTableModel(null, ipHeader);
		tbMain.setModel(mainsMd);
		lbIPs.setLabelFor(tbMain);
		tbMain.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTable target = (JTable) e.getSource();
				int row = target.getSelectedRow();
				String ip = (String) tbMain.getValueAt(row, 0);
				IPDetails details = new IPDetails(dataStore, ip);
				details.setVisible(true);
				frmLogFileReader.dispose();
			}
		});

		tbMain.setAutoCreateRowSorter(true);

		botsScrollPane.setViewportView(tbMain);
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.WEST);
		
		panel = new JPanel();
		frmLogFileReader.getContentPane().add(panel, "cell 0 2,grow");
		IPFunctions ipFunctions = new IPFunctions();
		dataStore.setUsersIP(ipFunctions.getUserip());
		lblNewLabel = new JLabel("Current IP " + dataStore.getUsersIP());
		panel.add(lblNewLabel);

		serchIPPL = new JPanel();
		frmLogFileReader.getContentPane().add(serchIPPL, "cell 0 3,grow");

		
				btnReadFile = new JButton("Read file (Start here)");
				serchIPPL.add(btnReadFile);
				
				btnAdmin = new JButton("Admin login");
				serchIPPL.add(btnAdmin);
				btnAdmin.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
					LoginUI loginUI = new LoginUI(dataStore);
						loginUI.setVisible(true);
						frmLogFileReader.dispose();
					}
				});
				btnReadFile.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser jfc = new JFileChooser(FileSystemView
								.getFileSystemView().getDefaultDirectory());
						jfc.setDialogTitle("Select a log file");
						jfc.setAcceptAllFileFilterUsed(false);
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
								"Text files", "txt");
						jfc.addChoosableFileFilter(filter);
						jfc.setMultiSelectionEnabled(true);
						int returnValue = jfc.showSaveDialog(null);
						
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							File[] files = jfc.getSelectedFiles();
							if(dataStore.getRisks().size() != 0) {
								dataStore.clearRisks();
							}
							for (File f: files) {
								reader.setFile(f.getAbsolutePath());
								reader.readFile();
							}
							processLogs();
						
							makeTable();
						}

					}

					private void makeTable() {
						IPFunctions ipFunctions = new IPFunctions();
						String ipHeader[] = new String[] { "IP", "Frequency", "Risk" };
							    mainsMd = new DefaultTableModel(null, ipHeader) {
									/**
									 *
									 */
									private static final long serialVersionUID = 4585202425202280069L;

									@Override
									public boolean isCellEditable(int row, int columm) {
										return false;
									}
								};
								tbMain.setModel(mainsMd);
								HashMap<String, Integer> countMap = new HashMap<String, Integer>();
						for (int i = 0; i < dataStore.getHits().size(); i++) {
							String key = dataStore.getHits().get(i).getiPaddr();
							
							if (countMap.containsKey(key)) {


							} else {
								Risk calulator = new Risk();
								Integer risk = calulator.calulateRisk(key, dataStore, ipFunctions.getLocation(key));
								calulator = null;
								dataStore.addRisk(key, risk);
								
								Integer value = dataStore.getOrrcancesOfip().get(key);
								String vs = value.toString();
								countMap.put(key, value);
								mainsMd.addRow(new String[] {key, vs, risk.toString()});
								changeTable(tbMain, 2);
								tbMain.update(getGraphics());
								tbMain.setModel(mainsMd);
							}
							}
					}
				});

		this.frmLogFileReader.setVisible(true);

	}

	private void updateGUI() {
		ArrayList<String> temp = new ArrayList<String>();
		for (int i = 0; i < dataStore.getHits().size(); i++) {
			if (!temp.contains(dataStore.getHits().get(i).getiPaddr())) {
				String key = dataStore.getHits().get(i).getiPaddr();
				String  value = dataStore.getOrrcancesOfip().get(key).toString();
				String risk = dataStore.getRisks().get(key).toString();
				mainsMd.addRow(new String[] {key, value, risk});
				changeTable(tbMain, 2);
				temp.add(key);
			}
		}
	}
	/**
	 * @param timesHitIp
	 *            the timesHitIp to set
	 */
	public void setTimesHitIp(int timesHitIp) {
		this.timesHitIp = timesHitIp;
	}

	/**
	 * @param timesHitPages
	 *            the timesHitPages to set
	 */
	public void setTimesHitPages(int timesHitPages) {
		this.timesHitPages = timesHitPages;
	}

	class MyTableCellRenderer extends DefaultTableCellRenderer {

	    @Override
	    public Color getBackground() {
	        return super.getBackground();
	    }
	}
	public void changeTable(JTable table, int column_index) {
        table.getColumnModel().getColumn(column_index).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int riskVal = Integer.parseInt(table.getValueAt(row,  2).toString());
                
                if (riskVal <25) {
                    c.setBackground(Color.GREEN);
                } else if (riskVal < 50) {
					c.setBackground(Color.YELLOW);
				} else if (riskVal < 75) {
					c.setBackground(Color.ORANGE);
				} else {
					c.setBackground(Color.RED);
				}
                return c;
            }
        });
    }

	/**
	 * @return
	 */
	private void processLogs() {
		Analise analise = new Analise();
		dataStore.setOrrcancesOfip(analise.getIpCounts(dataStore.getHits()));
		dataStore.setReferers(analise.getRefererCounts(dataStore.getHits()));
		dataStore.setProtcals(analise.getProtocalCounts(dataStore.getHits()));
		dataStore.setPages(analise.getPageCounts(dataStore.getHits()));
		analise.getTimeCounts(dataStore.getHits());
	}

        
        
     
}