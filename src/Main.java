import java.util.Date;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long lStartTime = new Date().getTime();
		
		XMLParser xml = new XMLParser("Data1k.xml");
		CreateDataFiles.createTerms(xml.getAds());
		CreateDataFiles.createDates(xml.getAds());
		CreateDataFiles.createPrices(xml.getAds());
		CreateDataFiles.createAds(xml.getAds());
		
		long lEndTime = new Date().getTime();
		
		System.out.println("Time: " + (lEndTime - lStartTime));
	}

}
