import java.util.Set;
import java.util.HashSet;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.OperationStatus;


public abstract class QuerySearch {

	public QuerySearch() {}
	
	public Set<Integer> get(Database db, String searchKey) {
		Set<Integer> ids = new HashSet<Integer>();
		
		if (db == null) {
			System.err.println("ERROR: QuerySearch: no database given");
			return ids;
		}
		
		try {
			// DatabaseEntry key,data;
			DatabaseEntry key = buildKey(searchKey);
			DatabaseEntry data = buildData();

			Cursor cursor = db.openCursor(null, null);
			
			if (cursor == null) {
				System.err.println("ERROR: QuerySearch: unable to create cursor");
				return ids;
			}
			
			OperationStatus retVal = first(cursor, key, data);
			
			if (cursor.count() > 0) {
				while (retVal == OperationStatus.SUCCESS) {
					
					Integer id = getId(key, data);
					
					if (id != null)
						ids.add(id);
					
					key = buildKey(searchKey);
					data = buildData();
					
					retVal = next(cursor, key, data);
				}
			}
			
			cursor.close();

		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return ids;
	}
	
	abstract public OperationStatus first(Cursor cursor, DatabaseEntry key, DatabaseEntry data);
	abstract public OperationStatus next(Cursor cursor, DatabaseEntry key, DatabaseEntry data);
	
	abstract public Integer getId(DatabaseEntry key, DatabaseEntry data);
	
	public DatabaseEntry buildKey(String key) {
		DatabaseEntry keyData = null;
		
		try {
			keyData = new DatabaseEntry(key.getBytes("UTF-8"));
		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return keyData;
	}
	
	public DatabaseEntry buildData() { 
		return new DatabaseEntry(); 
	}
	
}
