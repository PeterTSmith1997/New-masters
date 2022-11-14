package admin;

import javax.swing.JFrame;

import dataModel.DataStore;
import dataModel.helpers.Database;

/**
 * @author peter
 * @version 20 Jul 2019
 */
public class IPDetails extends JFrame {

	private DataStore dataStore;
	private String ip;
	private Database database;

	public IPDetails(DataStore dataStore, String ip) {

		this.dataStore = dataStore;
		this.ip = ip;
		database = new Database();
		makeUi();
	}

	private void makeUi() {
		// TODO Auto-generated method stub

	}

}
