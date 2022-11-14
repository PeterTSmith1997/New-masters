package dataModel;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import dataModel.helpers.Database;
/**
 * Quick class to allow the updating of the country risk from a text file
 * Not part of the main system
 * @author 16018262
 * @version 29 Jan 2020
 */
public class CountryUpdate {
	private static Scanner scanner;
	private static File file;
	private static String path = "Risks.txt";
	private static Database database = new Database();
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

		file = new File(path);  
		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				String[] data = line.split(" ");
				String contry = data[0];
				int risk = Integer.parseInt(data[1]);
				database.updateCountryRisk(contry, risk);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
			}
		});


	}
	}