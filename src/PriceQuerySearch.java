import java.util.HashSet;
import java.util.Set;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

/**
 * Queries the database by price.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class PriceQuerySearch extends QuerySearch {

	private QueryPrice price;
	private String searchText;
	
	public PriceQuerySearch() {}
	
	public Set<Integer> get(QueryPrice price) {
		Set<Integer> ids = new HashSet<Integer>();
		this.price = price;
		
		if (price == null) {
			System.err.println("ERROR: Price was null");
			return ids;
		}
		
		Database db = DatabaseManager.getInstance().getPrices();
		
		String key = String.format("%8s", price.getPrice());
		this.searchText = key;
		ids.addAll(get(db, key));
		
		return ids;
	}
	
	@Override
	public OperationStatus first(Cursor cursor, DatabaseEntry key,
			DatabaseEntry data) {
		OperationStatus retVal = OperationStatus.NOTFOUND;
		
		try {
			retVal = cursor.getSearchKeyRange(key, data, LockMode.DEFAULT);
			
			if ((OperationStatus) retVal != OperationStatus.SUCCESS &&
					!this.price.isGreaterThan()) {
				retVal = cursor.getLast(key, data, LockMode.DEFAULT);
			}
		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return retVal;
	}

	@Override
	public OperationStatus next(Cursor cursor, DatabaseEntry key,
			DatabaseEntry data) {
		OperationStatus retVal = OperationStatus.NOTFOUND;
		
		try {
			if (this.price.isGreaterThan())
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			else
				retVal = cursor.getPrev(key, data, LockMode.DEFAULT);
		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return retVal;
	}

	@Override
	public Integer getId(DatabaseEntry key, DatabaseEntry data) {
		Integer id = null;
		
		String keyString = new String(key.getData());
		String dataString = new String(data.getData());
		
		if (!price.isGreaterThan()) {
			if (keyString.compareTo(this.searchText) > 0) {
				return id;
			}
		}
		
		if (!keyString.equals(this.searchText)) {
			try {
				id = new Integer(dataString);
			} catch (Exception ex) {
				id = null;
			}
		}
		
		return id;
	}

}
