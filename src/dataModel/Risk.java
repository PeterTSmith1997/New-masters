package dataModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dataModel.helpers.BotAnalsis;
import dataModel.helpers.Database;
import dataModel.helpers.IPFunctions;
import dataModel.helpers.Modifiers;

public class Risk {
	private double orrcancesOfipLog;
	double countryRisk;
	private double responseRisk;
	private double requestRisk;
	private double userAgegentRisk;
	private Database database;
	private double risk;

	/**
	 * Small method to aid in the analysis of URL's by changing all the inputs to
	 * lower case
	 * 
	 * @param str The string to check
	 * @param sub The substring that we are looking for
	 * @return Boolean The comparison between strings
	 */
	public static boolean containIgnoreCase(String str, String sub) {
		return str.toLowerCase().contains(sub.toLowerCase());
	}

	/**
	 * @param ip
	 * @param dataStore
	 * @param countryCode
	 * @return
	 */
	public double risk(String ip, DataStore dataStore, String countryCode) {
		File temp = new File("temp.csv");
		risk = 0;
		database = new Database();
		if (!database.knownBots(ip).equals("n/a")) {
			return risk;
		}
		BotAnalsis botAnalsis = new BotAnalsis();

		orrcancesOfipLog = calulateOccances(ip, dataStore);
		countryRisk = database.countryRisk(countryCode);
		responseRisk = 0;
		requestRisk = 0;
		userAgegentRisk = 0;
		mainLoop(ip, dataStore, botAnalsis);
		Double asnrisk = asn(ip, database);
		Double requestResponseRisk = Math.log(requestRisk + responseRisk);
		if (Double.isNaN(requestResponseRisk)) {
			requestResponseRisk = 0.01;
		}
		Modifiers modifiers = new Modifiers();

		double networkRisk = (countryRisk * modifiers.getContrry()) + (asnrisk * modifiers.getAsn())
				+ (userAgegentRisk * modifiers.getUseragegent());

		risk = calulateRisk(orrcancesOfipLog, requestResponseRisk, modifiers, networkRisk);

		logToFile(ip, temp, risk, orrcancesOfipLog, countryRisk, responseRisk, requestRisk, requestResponseRisk,
				userAgegentRisk);

		return normalise(ip, dataStore, risk, database);
	}

	private void mainLoop(String ip, DataStore dataStore, BotAnalsis botAnalsis) {
		for (Hits h : dataStore.getHits()) {
			if (h.getiPaddr().equals(ip)) {
//				switch (h.getResponse()) {
//				case 400:
//					responseRisk += 0.5;
//					break;
//				case 401:
//					responseRisk += 5;
//					break;
//				case 403:
//					responseRisk += 2;
//					break;
//				case 404:
//					responseRisk += 1;
//					break;
//				case 429:
//					responseRisk += +2;
//					break;
//				case 500:
//					responseRisk += 0.2;
//					break;
//				case 200:
//					responseRisk -= 1;
//					break;
//				}
				requestResponse(dataStore, h);
				userAgegentRisk = botAnalsis.testThisStringforGoodness(h.getUserAgent());
			}

			

		}
	}

	private void requestResponse(DataStore dataStore, Hits h) {
		if (dataStore.getReponseScores().get(h.getResponse()) == null) {
			responseRisk = +0;
		} else {
			responseRisk += dataStore.getReponseScores().get(h.getResponse());
		}
		if (containIgnoreCase(h.getUserAgent(), "${")) {
			requestRisk += 5;
		}
		/*
		 * if (containIgnoreCase(h.getRequest(), "wp-admin")) { requestRisk += 3; } if
		 * (containIgnoreCase(h.getRequest(), "login")) { requestRisk += 10; }
		 */
		requestRisk = requestRisk + dataStore.serchurls(h.getRequest());
		if (h.getSize() == 0) {
			responseRisk = +6;
		}
		if (containIgnoreCase(h.getProtocal(), "1.1")) {
			requestRisk += 10;
		}
	}

	private Double asn(String ip, Database database) {
		IPFunctions ipFunctions = new IPFunctions();
		Integer asn;
		Double asnrisk;
		if (!(ipFunctions.getASN(ip) == null)) {
			asn = ipFunctions.getASN(ip);
			asnrisk = database.getASNRisk(asn);
		} else {
			asnrisk = 0.0;
		}
		return asnrisk;
	}

	private double normalise(String ip, DataStore dataStore, double risk, Database database) {
		if (risk >= DataStore.autoReport) {
			database.updateRiskIP(ip, dataStore, risk);
			dataStore.addReportedIP(ip);
		}
		if (risk > 100) {
			return 100;
		} else if (risk < 1) {
			return 1;
		} else {
			return risk;
		}
	}

	private double calulateRisk(double orrcancesOfipLog, Double requestResponseRisk, Modifiers modifiers,
			double networkRisk) {
		double risk;
		risk = (orrcancesOfipLog * modifiers.getOrrcances()) + (requestResponseRisk * modifiers.getRequestRespose())
				+ (networkRisk * modifiers.getNetwork());
		return risk;
	}

	private double calulateOccances(String ip, DataStore dataStore) {
		double orrcancesOfipLog = Math.log(dataStore.getOrrcancesOfip().get(ip));
		orrcancesOfipLog = orrcancesOfipLog == 0.00 ? 00.1 : orrcancesOfipLog;
		return orrcancesOfipLog;
	}

	private void logToFile(String ip, File temp, double risk, double orrcancesOfipLog, double countryRisk,
			double responseRisk, double requestRisk, double rr, double uaRisk) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp, true));
			writer.write(ip + "," + Double.toString(orrcancesOfipLog) + "," + Double.toString(responseRisk) + ","
					+ Double.toString(requestRisk) + "," + Double.toString(rr) + "," + Double.toString(countryRisk)
					+ "," + Double.toString(uaRisk) + "," + Double.toString(risk) + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param dbRiskMod the dbRiskMod to set
	 */
	public int calulateRisk(String ip, DataStore dataStore, String countryCode) {
		if (dataStore.getUsersIP().equals(ip)) {
			return -1;
		}
		Database database = new Database();
		Modifiers modifiers = new Modifiers();

		int totalRisk = (int) ((risk(ip, dataStore, countryCode) * modifiers.getIp())
				+ (database.getRiskIP(ip) * modifiers.getDb()));
		if (totalRisk > 100) {
			return 100;
		} else {
			return totalRisk;
		}
	}

}
