package dataModel.helpers;

import java.awt.EventQueue;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BotAnalsis {
	Database database = new Database();
	ArrayList<String> goodSigins = database.knownAU();
	ArrayList<String> noted = new ArrayList<>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new BotAnalsis();
			}
		});
	}

	public BotAnalsis() {
		testThisStringforGoodness("Mozilla/5.0 (compatible; AhrefsBot/6.1; +http://ahrefs.com/robot/)");
	}

	public float testThisStringforGoodness(String userAgent) {

		String testBits[] = userAgent.split(" ");

		int score = 60;

		int countOfMatches = 0;

		int countOfTested = 0;

		// for each word in the test String

		for (String it : testBits)

		{

			// println("****", it, "*****" );

			countOfTested++;

			for (String good : goodSigins)

			{

				if (good.equals(it))

				{

					countOfMatches++;

					score -= 5;

					// println( it , " is good" , score ) ;

					break;

				} else {
					if (!noted.contains(it)) {

						//toFile(it);
					}
				}

			}

		}

		System.out.println(countOfMatches + (float) countOfTested);

		System.out.println("Test items. " + testBits.length);

		System.out.println("final score " + score + " factin correct" + countOfMatches / (float) countOfTested);

		System.out.println("Percentage of correct = " + 100 * (countOfMatches / (float) countOfTested) + "%");

		return 50+score;

	}

	private void toFile(String it) {
		File unkown = new File("uua.txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(unkown, true));
			writer.write(it + "\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			noted.add(it);
		}
	}

}
