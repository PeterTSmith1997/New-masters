package dataModel;

import java.util.*;

import org.apache.commons.lang.StringUtils;

public class DataStore {
	public static final long monthMins = 43800;
	private LinkedList<Hits> hits = new LinkedList<>();
	private ArrayList<String> reportedIps = new ArrayList<>();
	private Map<String, Integer> orrcancesOfip = new HashMap<String, Integer>();
	private Map<String, Integer> ResponseRisk= new HashMap<String, Integer>();
	private Map<String, Integer> referers = new HashMap<String, Integer>();
	private Map<String, Integer> protcals = new HashMap<String, Integer>();
	private Map<String, Integer> pages = new HashMap<String, Integer>();
	private Map<Integer, Integer> responses = new HashMap<Integer, Integer>();
	private Map<String, Integer> risk = new HashMap<String,Integer>();
	private Map <Integer, Double> ProtocalScores = new HashMap<Integer, Double>();
	private Map <String, Integer> urlSorces = new HashMap<String,Integer>();
	public static final int autoReport = 50;
	private String usersIP;
	public DataStore() {
	}

	public void addHit(Hits h) {
		hits.add(h);
	}

	public void addReportedIP(String ip) {
		reportedIps.add(ip);
	}


	/**
	 * @return the hits
	 */
	public LinkedList<Hits> getHits() {
		return hits;
	}


	/**
	 * @return the orrcancesOfip
	 */
	public Map<String, Integer> getOrrcancesOfip() {
		return orrcancesOfip;
	}

	/**
	 * @return the pages
	 */
	public Map<String, Integer> getPages() {
		return pages;
	}

	/**
	 * @return the protcals
	 */
	public Map<String, Integer> getProtcals() {
		return protcals;
	}

	/**
	 * @return the referers
	 */
	public Map<String, Integer> getReferers() {
		return referers;
	}

	public Map<String, Integer> getRisk() {
		return risk;
	}

	public void setRisk(Map<String, Integer> risk) {
		this.risk = risk;
	}

	public String getUsersIP() {
		return usersIP;
	}

	public void setUsersIP(String usersIP) {
		this.usersIP = usersIP;
	}

	/**
	 * @return the reportedIps
	 */
	public ArrayList<String> getReportedIps() {
		return reportedIps;
	}

	/**
	 * @return the responses
	 */
	public Map<Integer, Integer> getResponses() {
		return responses;
	}

	/**
	 * @param hits
	 *            the hits to set
	 */
	public void setHits(LinkedList<Hits> hits) {
		this.hits = hits;
	}
	/**
	 * @return the risk map
	 */
	public Map<String, Integer> getRisks() {
		return risk;
	}

	/**
	 * @param risk
	 *            the risk to set
	 */
	public void setRik(Map<String, Integer> risk) {
		this.risk = risk;
	}
	public void addRisk(String ip, Integer risk) {
		this.risk.put(ip, risk);
	}


	/**
	 * @param orrcancesOfip
	 *            the orrcancesOfip to set
	 */
	public void setOrrcancesOfip(Map<String, Integer> orrcancesOfip) {
		this.orrcancesOfip = orrcancesOfip;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(Map<String, Integer> pages) {
		this.pages = pages;
	}

	/**
	 * @param protcals
	 *            the protcals to set
	 */
	public void setProtcals(Map<String, Integer> protcals) {
		this.protcals = protcals;
	}

	/**
	 * @param referers
	 *            the referers to set
	 */
	public void setReferers(Map<String, Integer> referers) {
		this.referers = referers;
	}

	/**
	 * @param reportedIps
	 *            the reportedIps to set
	 */
	public void setReportedIps(ArrayList<String> reportedIps) {
		this.reportedIps = reportedIps;
	}

	/**
	 * @param responses
	 *            the responses to set
	 */
	public void setResponses(Map<Integer, Integer> responses) {
		this.responses = responses;
	}

	public Map<Integer, Double> getReponseScores() {
		return ProtocalScores;
	}

	public void setProtocalScores(Map<Integer, Double> protocalScores) {
		ProtocalScores = protocalScores;
	}

	public Map<String, Integer> getResponseRisk() {
		return ResponseRisk;
	}

	public void setResponseRisk(Map<String, Integer> responseRisk) {
		ResponseRisk = responseRisk;
	}
	public void clearRisks() {
		risk.clear();
	}

	public Map<String, Integer> getUrlSorces() {
		return urlSorces;
	}

	public void setUrlSorces(Map<String, Integer> urlSorces) {
		this.urlSorces = urlSorces;
	}

	public Map<Integer, Double> getProtocalScores() {
		return ProtocalScores;
	}

	public double serchurls(String request) {
		double total = 0;
		for (Map.Entry<String, Integer> entry : urlSorces.entrySet()) {
			String key = entry.getKey();
			if (Risk.containIgnoreCase(request, key)) {
				System.out.println(entry.getValue());
				total = total + entry.getValue();
				System.err.println(request + " total "  + total + entry);
			}
		}
		System.err.println(request + total);
		return total;
	}


}


