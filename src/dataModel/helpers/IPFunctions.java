package dataModel.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;

/**
 * @author peter
 * @version 18 Jul 2019
 */
public class IPFunctions {
	
	File contryDatabase = new File("database\\GeoLite2-City.mmdb");
	File ASNDatabase = new File("database\\GeoLite2-ASN.mmdb");
	/**
	 * 
	 */
	public IPFunctions() {
	}

	public String getLocation(String ip) {
		try {
			DatabaseReader reader = new DatabaseReader.Builder(contryDatabase).build();
			InetAddress ipadress = InetAddress.getByName(ip);
			CityResponse response = reader.city(ipadress);
			Country country = response.getCountry();
			return country.getIsoCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;
	}
	
	public Integer getASN(String ip) {
		try {
			DatabaseReader reader = new DatabaseReader.Builder(ASNDatabase).build();
			InetAddress ipadress = InetAddress.getByName(ip);
			AsnResponse ASN = reader.asn(ipadress);
			return ASN.getAutonomousSystemNumber();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeoIp2Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	public String getUserip() {
		try {
			URL urlname = new URL ("http://checkip.amazonaws.com");
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlname.openStream()));
			return reader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


}
