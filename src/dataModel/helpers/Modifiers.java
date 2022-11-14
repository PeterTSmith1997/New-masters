package dataModel.helpers;
 
public final class Modifiers {
	//IP risks
	private final double orrcances =0.3;
	private final double requestRespose = 0.4;
	private final double network = 0.3;
	
	//network
	private final double asn = 0.4;
	private final double contrry = 0.4;
	private final double Useragegent = 0.2;
	
	//database mod
	private final double allTimedb = 0.25;
	private final double recent = 0.75;
	
	// final cal
	private final double ip = 0.90;
	private final double db = 0.1;
	/**
	 * @return the orrcances
	 */
	public double getOrrcances() {
		return orrcances;
	}
	/**
	 * @return the requestRespose
	 */
	public double getRequestRespose() {
		return requestRespose;
	}
	/**
	 * @return the network
	 */
	public double getNetwork() {
		return network;
	}
	/**
	 * @return the asn
	 */
	public double getAsn() {
		return asn;
	}
	/**
	 * @return the contrry
	 */
	public double getContrry() {
		return contrry;
	}
	/**
	 * @return the useragegent
	 */
	public double getUseragegent() {
		return Useragegent;
	}
	/**
	 * @return the allTimedb
	 */
	public double getAllTimedb() {
		return allTimedb;
	}
	/**
	 * @return the recent
	 */
	public double getRecent() {
		return recent;
	}
	/**
	 * @return the ip
	 */
	public double getIp() {
		return ip;
	}
	/**
	 * @return the db
	 */
	public double getDb() {
		return db;
	}
	
	
}
