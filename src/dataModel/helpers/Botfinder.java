import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Botfinder {
	
	public static void main(String[] args) {
		ArrayList<String> added = new ArrayList<>();
		
		File search = new File ("C:\\Users\\w16018262\\Documents\\GitHub\\New-masters\\Log files\\petersweb.me.uk jun.txt");
		File out = new File ("C:\\Users\\w16018262\\Documents\\GitHub\\New-masters\\Log files\\goodbots.csv");
		try {
			Scanner scanner = new Scanner(search);
			String searchTerm = "SemrushBot/7";
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] data = line.split(" ");
					String userAgent = null;
					for (int x = 11; x < data.length; x++) {
					      userAgent = userAgent +" "+ data[x];
					      System.err.println(userAgent);
					}
					if (userAgent.contains(searchTerm)) 
					{
						if (!added.contains(data[0])) {
						System.out.println("i am a bot");
						added.add(data[0]);
					 BufferedWriter writer = new BufferedWriter(new FileWriter(out, true));
					 writer.write(data[0]+",");
					 writer.close();
					}
					}

				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
