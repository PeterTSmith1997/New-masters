import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class parts {

	public static void main(String[] args) {
		ArrayList<String> added = new ArrayList<>();

		File search = new File ("C:\\Users\\w16018262\\Documents\\GitHub\\New-masters\\Log files\\petersweb.me.uk jun.txt");
		File out = new File ("C:\\Users\\w16018262\\Documents\\GitHub\\New-masters\\Log files\\parts.csv");
		try {
			Scanner scanner = new Scanner(search);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] data = line.split(" ");
				for (int x = 11; x < data.length; x++) {

					if (!added.contains(data[x])) {
						System.out.println("i am a bot");
						added.add(data[x]);
						BufferedWriter writer = new BufferedWriter(new FileWriter(out, true));
						writer.write(data[x]+",\n");
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
