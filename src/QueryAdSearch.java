import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

/**
 * Queries the database by ad.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class QueryAdSearch {
	
	public QueryAdSearch() {}
	
	public Ad getAd(Integer id) {
		Ad ad = null;
		
		try {
			String stringId = id.toString();
			
			// DatabaseEntry key,data;
			DatabaseEntry key = new DatabaseEntry(stringId.getBytes("UTF-8"));
			DatabaseEntry data = new DatabaseEntry();
			
			Database db = DatabaseManager.getInstance().getAds();
			
			Cursor std_cursor = db.openCursor(null, null);

			if (std_cursor.getSearchKey(key, data, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
				String aData = new String(data.getData());
				
				XMLParser parser = new XMLParser();
				ad = parser.parseAd(aData);
				
				//System.out.println("Key: " + aKey + ", Data: " + aData);
				
			} else {
				System.out.println("The database is empty\n");
			}

			std_cursor.close();

		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return ad;
	}
}
