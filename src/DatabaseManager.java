import java.io.FileNotFoundException;

import com.sleepycat.db.*;

public class DatabaseManager {
	
	// Static Instance
	private static DatabaseManager _instance = null;
	
	private Database terms = null;
	private Database pdates = null;
	private Database prices = null;
	private Database ads = null;

	private DatabaseManager() {
		terms = createDatabase(DatabaseType.BTREE, "data/te.idx");
		pdates = createDatabase(DatabaseType.BTREE, "data/da.idx");
		prices = createDatabase(DatabaseType.BTREE, "data/pr.idx");
		ads = createDatabase(DatabaseType.HASH, "data/ad.idx");
	}

	public static DatabaseManager getInstance() {
		if (_instance == null)
			_instance = new DatabaseManager();

		return _instance;
	}
	
	private Database createDatabase(DatabaseType type, String file)
	{
		// database configuration
		DatabaseConfig dbConfig = new DatabaseConfig();

		// dbConfig.setErrorStream(System.err);
		// dbConfig.setErrorPrefix("MyDbs");

		dbConfig.setType(type);
		dbConfig.setAllowCreate(false);

		// database
		Database std_db = null;
		
		try {
			std_db = new Database(file, null, dbConfig);
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File not found. " + file);
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		return std_db;
	}

	public void test()
	{
		testDB(terms);
		testDB(pdates);
		testDB(prices);
		testDB(ads);
	}
	
	// Prints Database entrys
	public void testDB(Database std_db) {
		try {
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

		} catch (Exception ex) {
			ex.getMessage();
		}
	}
}
