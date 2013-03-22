import java.io.IOException;
import java.util.Date;
import com.sleepycat.db.*;

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
		XMLParser xml = new XMLParser("Data1k.xml");

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
		
		// Sort Files
		try {
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
			
			process = Runtime.getRuntime().exec("sh BuildIndex.sh");
			exitCode = process.waitFor();
			System.out.println("Process Build Index returned: " + exitCode);
			
		} catch (IOException e) {
			System.out.println("Error: Sort");
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// Tests
		System.out.println("Ads:");
		test(DatabaseType.HASH, "data/ad.idx");
		System.out.println("\npDates:");
		test(DatabaseType.BTREE, "data/da.idx");
		System.out.println("\nPrices:");
		test(DatabaseType.BTREE, "data/pr.idx");
		System.out.println("\nTerms:");
		test(DatabaseType.BTREE, "data/te.idx");

		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Time: " + (lEndTime - lStartTime));
	}

	// Prints ads in Database from ad.idx
	public static void test(DatabaseType type, String file) {
		try {

			// database configuration
			DatabaseConfig dbConfig = new DatabaseConfig();

			// dbConfig.setErrorStream(System.err);
			// dbConfig.setErrorPrefix("MyDbs");

			dbConfig.setType(type);
			dbConfig.setAllowCreate(false);

			// database
			Database std_db = new Database(file, null, dbConfig);

			// DatabaseEntry key,data;
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry data = new DatabaseEntry();

			String aKey;
			String aData;

			Cursor std_cursor = std_db.openCursor(null, null);

			if (std_cursor.getFirst(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				aKey = new String(key.getData());
				aData = new String(data.getData());

				System.out.println("Key: " + aKey + ", Data: " + aData);

				key = new DatabaseEntry();
				data = new DatabaseEntry();

				while (std_cursor.getNext(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {

					aKey = new String(key.getData());
					aData = new String(data.getData());

					System.out.println("Key: " + aKey + ", Data: " + aData);

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
	}

}
