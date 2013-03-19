import java.io.IOException;
import java.util.Date;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Start Time
		long lStartTime = new Date().getTime();
		
		// Parse XML file
		XMLParser xml = new XMLParser("Data1k.xml");
		
		// Create Data Files
		CreateDataFiles.createTerms(xml.getAds());
		CreateDataFiles.createDates(xml.getAds());
		CreateDataFiles.createPrices(xml.getAds());
		CreateDataFiles.createAds(xml.getAds());
		
		// Sort Files
		try {
			Runtime.getRuntime().exec("sort -k2 -t- terms.txt -o terms.txt");
			Runtime.getRuntime().exec("sort -t':' pdates.txt -o pdates.txt");
			Runtime.getRuntime().exec("sort -n -t: prices.txt -o prices.txt");
			Runtime.getRuntime().exec("sort -n -t: ads.txt -o ads.txt");
		} catch (IOException e) {
			System.out.println("Error: Sort");
			e.printStackTrace();
		}
		
		// End Time
		long lEndTime = new Date().getTime();
		
		// Print elapsed time
		System.out.println("Time: " + (lEndTime - lStartTime));
	}

}
