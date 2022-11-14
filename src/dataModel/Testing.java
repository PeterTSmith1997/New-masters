package dataModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dataModel.helpers.IPFunctions;

public class Testing {
	private String log = "dataNew.csv";
	private File dataFile = new File(log);
	private BufferedWriter write;
	private Analise analise = new Analise();
	private String countryCode;
	private IPFunctions ipFunctions = new IPFunctions();
	
	
	public void makeCSV(DataStore dataStore) {
		try {
			write = new BufferedWriter(new FileWriter(dataFile));
			for (Hits h : dataStore.getHits()) {
				if  (!dataStore.getReportedIps().contains(h.getiPaddr())) {
				countryCode = ipFunctions.getLocation(h.getiPaddr());
				Double risk = analise.risk(h.getiPaddr(), dataStore, countryCode);
				String rs = risk.toString();
				Integer o = dataStore.getOrrcancesOfip().get(h.getiPaddr());
				String os = o.toString();
				String line[] = new String[] {h.getiPaddr(), rs, os};
				write.append(String.join(",", line));
				write.append("\n");
				dataStore.addReportedIP(h.getiPaddr());
				}
				
			}
			write.flush();
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}


	/**
	 * 
	 */
	public Testing() {
	}
	
}
