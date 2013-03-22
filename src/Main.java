import java.io.IOException;
import java.util.Date;

import com.sleepycat.db.Database;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Process process = null;  
		int exitCode;
		
		// Start Time
		long lStartTime = new Date().getTime();

		// Parse XML file
		XMLParser xml = new XMLParser("Data10.xml");

		// Clean Files
		try {
			process = Runtime.getRuntime().exec("rm -r data/");
			exitCode = process.waitFor();
			System.out.println("Process Clean returned: " + exitCode);
			
			process = Runtime.getRuntime().exec("mkdir data");
			exitCode = process.waitFor();
			System.out.println("Process data returned: " + exitCode);
		} catch (IOException e) {
			System.out.println("Error: When creating new data file");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Create Data Files
		CreateDataFiles.createTerms(xml.getAds());
		CreateDataFiles.createDates(xml.getAds());
		CreateDataFiles.createPrices(xml.getAds());
		CreateDataFiles.createAds(xml.getAds());
		
		// Sort Files and Build Indexes
		try {
			// Sort
			process = Runtime.getRuntime().exec("sort -k2 -t- data/terms.txt -o data/terms.txt");
			exitCode = process.waitFor();
			System.out.println("Process terms.txt returned: " + exitCode);
			
			process = Runtime.getRuntime().exec("sort -t: data/pdates.txt -o data/pdates.txt");
			exitCode = process.waitFor();
			System.out.println("Process pdates.txt returned: " + exitCode);
			
			process = Runtime.getRuntime().exec("sort -n -t: data/prices.txt -o data/prices.txt");
			exitCode = process.waitFor();
			System.out.println("Process prices.txt returned: " + exitCode);
			
			process = Runtime.getRuntime().exec("sort -n -t: data/ads.txt -o data/ads.txt");
			exitCode = process.waitFor();
			System.out.println("Process ads.txt returned: " + exitCode);
			
			// Build Index Files
			process = Runtime.getRuntime().exec("sh BuildIndex.sh");
			exitCode = process.waitFor();
			System.out.println("Process Build Index returned: " + exitCode);
			
		} catch (IOException e) {
			System.out.println("Error: Sort");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Tests
		//DatabaseManager.getInstance().test();
		Database terms = DatabaseManager.getInstance().getTerms();
		DatabaseManager.getInstance().get(terms, "b-with");
		
		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Final Time: " + (lEndTime - lStartTime));
	}

	

}
