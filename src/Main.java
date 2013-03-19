public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XMLParser xml = new XMLParser("Data10.xml");
		CreateDataFiles.createTerms(xml.getAds());
		CreateDataFiles.createDates(xml.getAds());
		CreateDataFiles.createPrices(xml.getAds());
		CreateDataFiles.createAds(xml.getAds());
	}

}
