package dataModel.helpers;
import java.io.File;
import java.io.FileNotFoundException;

import java.util.Scanner;

import javax.swing.JOptionPane;

import dataModel.DataStore;
import dataModel.Hits;

/**
 * @author peter
 * @version 18 Jul 2019
 */
public class Reader {
	private Scanner scanner;
	private File logg;
	private DataStore dataStore;

	public Reader(DataStore dataStore, String pathToFile) {
		logg = new File(pathToFile);
		this.dataStore = dataStore;
	}

	/**
	 * @param dataStore
	 */
	public Reader(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	/**
	 * 
	 */
	public void readFile() {
		try {
			scanner = new Scanner(logg);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] data = line.split(" ");
			int response = Integer.parseInt(data[8]);
			int size = Integer.parseInt(data[9]);
			String userAgent = "";
			for (int x = 11; x < data.length; x++) {
				userAgent = userAgent +" "+ data[x];
			}
			Hits h = new Hits(data[0], data[5] + " " + data[6], data[7],
					data[3] + " " + data[4], response, size, data[10],
					userAgent);
			dataStore.addHit(h);
		}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error in processing file, please "
					+ "make sure that the file is in the correct formmat",
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
		}

	public void setFile(String pathToFile) {
		logg = new File(pathToFile);
	}

}

