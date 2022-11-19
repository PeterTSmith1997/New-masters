package dataModel.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import dataModel.DataStore;

/**
 * @author peter
 * @version 21 Jul 2019
 */
public class Database {
	private String driver = "jdbc:ucanaccess://";
	private String Db = "database//Main.accdb";
	private Connection conn = null;
	private String url = driver + Db;

	public Database() {
		connect();
	}

	public boolean connect() {
		try {
			conn = DriverManager.getConnection(url);
			System.err.println("Connected");
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Could not establish databse connection. Contact database administrator",
					"Database error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
		}
	}

	public String knownBots(String ip) {
		String botName = "n/a";
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT BotName FROM Bots  Where IP=?");
			stmt.setString(1, ip);
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return botName;
			} else {
				return rs.getString("BotName");
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return botName;

	}

	public ArrayList<String> getCategories() {

		try {
			Statement stmt = conn.createStatement();
			String query = "SELECT CatName FROM IPType";
			ResultSet rs = stmt.executeQuery(query);

			ArrayList<String> categories = new ArrayList<String>();
			while (rs.next()) {
				categories.add(rs.getString("CatName"));
			}

			return categories;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/**
	 * Takes an insert SQL statement and executes it
	 * 
	 * @param A
	 *            formatted SQL statement
	 * @return If the statement executed correctly
	 */
	public Boolean executeSQL(String insertSQL) {
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(insertSQL);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public double getRiskIP(String ip) {
		Modifiers modifiers = new Modifiers();
		return (getRisk30Days(ip) * Modifiers.RECENT) + 
				(getRiskAlltime(ip) * Modifiers.ALLTIMEDB);
	}

	public void updateRiskIP(String ip, DataStore dataStore, double risk) {

		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String today = dateFormat.format(date);
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO IPLog (IP, Risk, DateReported, Type) VALUES (?,?,?,?)");
			stmt.setString(1, ip);
			stmt.setDouble(2, risk);
			stmt.setDate(3, convertStringToSQLDate(today));
			stmt.setInt(4, 4);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getOcourances(String ip) {
		int occurances = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT count(IP) AS count from IPLog Where IP=?");
			stmt.setString(1, ip);
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			occurances = Integer.parseInt(rs.getString("count"));
			System.out.println("count=" + occurances);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurances;

	}

	public int getOcourancesLast30days(String ip) {
		int occurances = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT count(IP) AS count from IPLog Where IP=? and DateReported>?");
			stmt.setString(1, ip);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.DATE, -30);
			stmt.setDate(2,
					convertStringToSQLDate(dateFormat.format(cal.getTime())));
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			occurances = Integer.parseInt(rs.getString("count"));
			System.out.println("count=" + occurances);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return occurances;

	}

	public java.sql.Date convertStringToSQLDate(String date)
			throws ParseException {
		Date convertedDate;
		java.sql.Date sConvertedDate;

		if (date != null) {
			convertedDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			sConvertedDate = new java.sql.Date(convertedDate.getTime());
		} else {
			sConvertedDate = null;
		}
		return sConvertedDate;
	}

	private double getRisk30Days(String ip) {
		int risk = 0;
		try {
			System.out.println(ip);
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT Avg(risk) AS Risk FROM IPLog where IP=? and DateReported>?");
			stmt.setString(1, ip);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.DATE, -30);
			stmt.setDate(2,
					convertStringToSQLDate(dateFormat.format(cal.getTime())));
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			risk = rs.getInt("Risk");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return risk;
	}

	public DefaultTableModel getRisk30DaysAdmin() {
		String ipHeader[] = new String[] { "ip", "Number", "Risk" };
		DefaultTableModel last30Days = new DefaultTableModel(null, ipHeader);
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT ip, Count(*) AS num, Avg(risk) AS Risk FROM IPLog "
							+ "Where DateReported>? " + "GROUP BY ip "
							+ "ORDER BY Count (*)");

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.DATE, -31);
			stmt.setDate(1,
					convertStringToSQLDate(dateFormat.format(cal.getTime())));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				last30Days.addRow(new String[] { rs.getString("ip"),
						Integer.toString(rs.getInt("num")),
						Integer.toString(rs.getInt("Risk")) });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return last30Days;
	}

	private double getRiskAlltime(String ip) {
		int risk = 0;
		try {
			System.out.println(ip);
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT Avg(risk) AS Risk FROM IPLog where IP=? and DateReported>?");
			stmt.setString(1, ip);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.DATE, -31);
			stmt.setDate(2,
					convertStringToSQLDate(dateFormat.format(cal.getTime())));
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			risk = rs.getInt("Risk");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return risk;
	}

	public boolean validateLogin(String user, String password) {
		boolean validLogin = false;
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT PasswordHash FROM User WHERE Username = '" + user
							+ "'");
			ResultSet rs = stmt.executeQuery();
			boolean moreRecords = rs.next();
			// If there are no records to show validLogin is set to false
			if (!moreRecords) {
				System.out.println("ResultSet contained no records");
				return false;
			}
			// If the entered password matches the one stored in the database
			// validLogin is set to true
			if ((password.equals(rs.getString("PasswordHash")))) {
				validLogin = true;
				System.out.println("Sucess");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// Return validLogin to check if login was successful
		System.out.println(validLogin);
		return validLogin;
	}

	public DefaultTableModel getTotalReports() {
		String ipHeader[] = new String[] { "ip", "Number", "Risk" };
		DefaultTableModel allTime = new DefaultTableModel(null, ipHeader);
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT ip, Count(*) AS num, Avg(risk) AS Risk"
							+ " FROM IPLog " + "GROUP BY ip "
							+ "ORDER BY Count (ip) DESC");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				allTime.addRow(new String[] { rs.getString("ip"),
						Integer.toString(rs.getInt("num")),
						Integer.toString(rs.getInt("Risk")) });
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allTime;

	}

	public double countryRisk(String code) {
		double risk = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT Risk FROM countryRisk WHERE ID=?");
			stmt.setString(1, code);
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			risk = rs.getInt("Risk");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return risk;

	}

	public ArrayList<String> getCounties() {
		ArrayList<String> counties = new ArrayList<String>();
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT ID, Name FROM countryRisk ORDER BY ID");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				counties.add(rs.getString("ID").toUpperCase() + " - "
						+ rs.getString("Name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counties;

	}

	public boolean updateCountryRisk(String code, int risk) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"UPDATE countryRisk SET Risk=? WHERE ID=?");
			stmt.setInt(1, risk);
			stmt.setString(2, code);
			int result = stmt.executeUpdate();
			if (result == 0) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	public int numberReports30Days() {
		int reports = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT count(ID) AS num FROM IPLog where DateReported>?");
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			Date date = new Date();
			cal.setTime(date);
			cal.add(Calendar.DATE, -31);
			stmt.setDate(1,
					convertStringToSQLDate(dateFormat.format(cal.getTime())));
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			reports = rs.getInt("num");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reports;
	}

	public int numberReportsAllTime() {
		int reports = 0;
		try {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT count(ID) AS num FROM IPLog");
			ResultSet rs = stmt.executeQuery();
			Boolean moreRecords = rs.next();
			if (!moreRecords) {
				System.err.println("no R");
				return 0;
			}
			reports = rs.getInt("num");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reports;
	}

	public void reportPossibleBot(String ip, String userAgent) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO Bots (IP, Cat, Discription, BotName) "
							+ "VALUES (?,?,?,?)");
			stmt.setString(1, ip);
			stmt.setInt(2, 2);
			stmt.setString(3, "user added");
			stmt.setString(4, userAgent);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DefaultTableModel getPosibleBots() {
		String ipHeader[] = new String[] { "ip", "User Agent",
				"Number of reports" };
		DefaultTableModel posibleBots = new DefaultTableModel(null, ipHeader);
		try {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT count(IP) AS num, BotName, IP "
							+ "FROM Bots " + "WHERE Cat=? "
							+ "GROUP BY IP, BotName " + "ORDER BY count(IP)");
			stmt.setInt(1, 2);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				posibleBots.addRow(new String[] { rs.getString("IP"),
						rs.getString("BotName"),
						Integer.toString(rs.getInt("num")) });
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return posibleBots;

	}

	public DefaultTableModel getKnownBots() {
		String ipHeader[] = new String[] { "ip", "Bot Name", "type" };
		DefaultTableModel knownBots = new DefaultTableModel(null, ipHeader);
		try {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT BotName, IP, CatName "
							+ "FROM Bots INNER JOIN IPType "
							+ "ON Bots.Cat=IPType.TypeRef " + "WHERE Cat!=? ");
			stmt.setInt(1, 2);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				knownBots.addRow(new String[] { rs.getString("IP"),
						rs.getString("BotName"), rs.getString("catName") });
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return knownBots;

	}

	public ArrayList<String> getBotCats() {
		ArrayList<String> botCats = new ArrayList<>();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT Typeref, CatName FROM IPType");
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				String data = rs.getString("Typeref") + " - "
						+ rs.getString("CatName");
				botCats.add(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return botCats;

	}

	public Map<String, String> getBotInfo(String ip) {
		Map<String, String> botInfo = new HashMap<String, String>();
		try {
			PreparedStatement stmt = conn
					.prepareStatement("SELECT BotName, Discription, " + "Cat "
							+ "FROM Bots INNER JOIN IPType "
							+ "ON Bots.Cat=IPType.TypeRef " + "WHERE IP=? ");
			stmt.setString(1, ip);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				return null;
			}
			botInfo.put("cat", rs.getString("cat"));
			botInfo.put("Discription", rs.getString("Discription"));
			botInfo.put("Name", rs.getString("Botname"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return botInfo;

	}

	public boolean updateBotInfo(Map<String, String> botInfo) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("Delete * from Bots" + " WHERE IP=?");
			stmt.setString(1, botInfo.get("ip"));
			stmt.execute();
			PreparedStatement stmt2 = conn.prepareStatement("Insert into Bots (IP,"
					+ "Cat, Discription, BotName) VALUES (?,?,?,?)");
			stmt2.setString(1, botInfo.get("ip"));
			stmt2.setString(2, botInfo.get("category"));
			stmt2.setString(3, botInfo.get("Discription"));
			stmt2.setString(4, botInfo.get("Name"));
			stmt2.execute();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private void updateContryRisk(String country, int risk) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"update CountryRisk" + "SET Risk+?" + "WHERE ID=?");
			stmt.setInt(1, risk);
			stmt.setString(2, country);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Map<Integer, Double> Setprotcalscores() {
		try {
			Map<Integer, Double> codes = new HashMap<Integer, Double>();
			PreparedStatement stmt = conn.prepareStatement("select HttpCode, Risk from Codes");
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				codes.put(resultSet.getInt("HttpCode"), resultSet.getDouble("risk"));
				System.out.println(codes);
							
			}return codes;	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Map<String, Integer> setURLScores() {
		try {
		Map<String, Integer> codes = new HashMap<String, Integer>();
		PreparedStatement stmt = conn.prepareStatement("select Request, Risk from RequestRisk");
		ResultSet resultSet = stmt.executeQuery();
		while (resultSet.next()) {
			codes.put(resultSet.getString("Request"), resultSet.getInt("risk"));
		}return codes;
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null; 
	}
	
	public Double getASNRisk(int asn) {
		try {
			PreparedStatement stmt = conn.prepareStatement("select Risk from ASNRisk "
					+ "where ASN = ?");
			stmt.setInt(1, asn);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				return 0.0;
			}
			else {
				return rs.getDouble("Risk");
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public Set<String> knownAU() { 
		Set<String> goodSigns = new HashSet<>();
		try {
		PreparedStatement stmt = conn.prepareStatement("select UA from UARisk");
		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
		goodSigns.add(rs.getString("UA"));
		System.out.println(goodSigns);
		}
		} catch (SQLException e) {
		// TODO: handle exception
		e.printStackTrace();
		}
		return goodSigns;
		}
}