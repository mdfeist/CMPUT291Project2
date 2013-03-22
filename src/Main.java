import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import com.sleepycat.db.*;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Start Time
		long lStartTime = new Date().getTime();

		// Parse XML file
		XMLParser xml = new XMLParser("Data10.xml");

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
			
			Runtime.getRuntime().exec("awk -F: '{print $1; print $2}' < ads.txt | db_load -T -t hash ad.idx");
		} catch (IOException e) {
			System.out.println("Error: Sort");
			e.printStackTrace();
		}

		// Prints ads in Database from ad.idx
		try {

			// database configuration
			DatabaseConfig dbConfig = new DatabaseConfig();

			// dbConfig.setErrorStream(System.err);
			// dbConfig.setErrorPrefix("MyDbs");

			dbConfig.setType(DatabaseType.HASH);
			dbConfig.setAllowCreate(false);

			// database
			Database std_db = new Database("ad.idx", null, dbConfig);

			// DatabaseEntry key,data;
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();

			String aKey;
			String aData;

			Cursor std_cursor = std_db.openCursor(null, null);

			if (std_cursor.getFirst(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				aKey = new String(key.getData());
				aData = new String(data.getData());
				
				System.out.println("Id: " + aKey + ", Ad: " + aData);
				
				key = new DatabaseEntry();
				data = new DatabaseEntry();
				
				while (std_cursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

					aKey = new String(key.getData());
					aData = new String(data.getData());
					
					System.out.println("Id: " + aKey + ", Ad: " + aData);
					
					key = new DatabaseEntry();
					data = new DatabaseEntry();
				}
			} else {
				System.out.println("The database is empty\n");
			}

			std_cursor.close();

			std_db.close();

		} catch (Exception ex) {
			ex.getMessage();
		}

		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Time: " + (lEndTime - lStartTime));
	}

}
