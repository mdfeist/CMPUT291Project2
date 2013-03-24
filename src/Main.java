import java.io.IOException;
import java.util.Date;


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
		System.out.println("\nSearch apart% in terms");
		DatabaseManager.getInstance().getTermsBoth("apart%");
		System.out.println("\nSearch 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDate("2012/06/24");
		System.out.println("\nSearch since 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDatesFrom("2012/06/24");
		System.out.println("\nSearch until 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDatesTo("2012/06/24");
		System.out.println("\nSearch price < 14900");
		DatabaseManager.getInstance().getPrices(14900, false);
		System.out.println("\nSearch price > 14900");
		DatabaseManager.getInstance().getPrices(14900, true);
		
		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Final Time: " + (lEndTime - lStartTime));
	}

	

}
