package dataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import main.LogData;

/**
 * @author 16018262
 * @version 14 Dec 2019
 */
public class Analise {
	/**
	 *
	 */
	public Analise() {

	}

	/**
	 * @return the dbRiskMod
	 */
	

	/**
	 * sets the map of IP counts
	 * 
	 * @param linkedList
	 *            - the ArrayList to be sorted
	 * @return The map of sorted IPs with the no. of times in the dataset
	 */
	public HashMap<String, Integer> getIpCounts(LinkedList<Hits> linkedList) {
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for (int i = 0; i < linkedList.size(); i++) {
			String key = linkedList.get(i).getiPaddr();
			if (countMap.containsKey(key)) {
				int count = countMap.get(key);
				count++;
				countMap.put(key, count);
			} else {
				countMap.put(key, 1);
			}
		}
		return countMap;
	}


	/**
	 * sets the map of IP counts
	 * 
	 * @param hits
	 *            - the ArrayList to be sorted
	 * @param mainUi 
	 * @param mainUi 
	 * @return 
	 */
	public void SetRiskmap(ArrayList<Hits> hits, DataStore dataStore, LogData mainUi) {

	}

	/**
	 * Set the map of page count
	 * @param linkedList - the ArrayList to be sorted
	 * @return The map of page counts
	 */
	public Map<String, Integer> getPageCounts(LinkedList<Hits> linkedList) {
		Map<String, Integer> countMap = new TreeMap<String, Integer>();
		for (int i = 0; i < linkedList.size(); i++) {
			String key = linkedList.get(i).getRequest();
			if (countMap.containsKey(key)) {
				int count = countMap.get(key);
				count++;
				countMap.put(key, count);
			} else {
				countMap.put(key, 1);
			}
		}

		return countMap;
	}

	/**
	 *
	 * @param hits- the ArrayList to be sorted
	 * @return The map of protocol counts
	 */
	public Map<String, Integer> getProtocalCounts(LinkedList<Hits> linkedList) {
		Map<String, Integer> countMap = new TreeMap<String, Integer>();
		for (int i = 0; i < linkedList.size(); i++) {
			String key = linkedList.get(i).getProtocal();
			if (countMap.containsKey(key)) {
				int count = countMap.get(key);
				count++;
				countMap.put(key, count);
			} else {
				countMap.put(key, 1);
			}
		}

		return countMap;
	}

		/**
	 *
	 * @param linkedList - the ArrayList to be sorted
	 * @return The map of referer counts
	 */
	public Map<String, Integer> getRefererCounts(LinkedList<Hits> linkedList) {
		Map<String, Integer> countMap = new TreeMap<String, Integer>();
		for (int i = 0; i < linkedList.size(); i++) {
			String key = linkedList.get(i).getReferer();
			if (countMap.containsKey(key)) {
				int count = countMap.get(key);
				count++;
				countMap.put(key, count);
			} else {
				countMap.put(key, 1);
			}
		}
		return countMap;
	}

	/**
	 *
	 * @param linkedList - the ArrayList to be sorted
	 * @return The map of time counts
	 */
	public Map<String, Integer> getTimeCounts(LinkedList<Hits> linkedList) {
		Map<String, Integer> countMap = new TreeMap<String, Integer>();
		for (int i = 0; i < linkedList.size(); i++) {
			String dateTime = linkedList.get(i).getDateTime();
			String[] data = dateTime.split(":");
			String key = data[1] + ":" + data[2];
			if (countMap.containsKey(key)) {
				int count = countMap.get(key);
				count++;
				countMap.put(key, count);
			} else {
				countMap.put(key, 1);
			}
		}
		return countMap;
	}

	/**
	 *
	 * @param hits - the ArrayList to be sorted
	 * @return The total amount of data sent
	 */
	public int getTotalData(ArrayList<Hits> hits) {
		int total = 0;
		for (Hits h : hits) {
			total = total + h.getSize();
		}
		return total;
	}

	/**
	 *
	 * @param linkedList - the ArrayList of all hits
	 * @param ip The IP to get the data for
	 * @return the total amount of data for an IP
	 */
	public int getTotalDataForIP(LinkedList<Hits> linkedList, String ip) {
		int total = 0;
		for (Hits h : linkedList) {
			if (h.getiPaddr().equals(ip)) {
				total = total + h.getSize();
			}
		}
		return total;
	}

	/**
	 *
	 * @param hits - The array of the hits
	 * @return int the total size of the array
	 */
	public int getTotalHits(ArrayList<Hits> hits) {
		return hits.size();
	}

	/**
	 *
	 * @param ip  the IP to calculate the risk for
	 * @param dataStore Contains all the data needed
	 * @param countryCode The countrycode that the IP originates from
	 * @return Double Returns the risk factor
	 */
}
