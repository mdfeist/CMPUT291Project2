import java.util.HashSet;
import java.util.Set;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.LockMode;
import com.sleepycat.db.OperationStatus;

/**
 * Queries the database by term.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class TermQuerySearch extends QuerySearch {

	private boolean wildCardSearch;
	private boolean forceEnd;
	
	private String searchText;
	
	public TermQuerySearch() {};
	
	public Set<Integer> get(QueryTerm term) {
		Set<Integer> ids = new HashSet<Integer>();
		
		this.forceEnd = false;
		
		this.searchText = term.getTerm();
		
		if (this.searchText != null) {
			this.searchText = this.searchText.toLowerCase();
		} else {
			System.err.println("ERROR: Term text is null");
			return ids;
		}
		
		QueryTerm.TermField field = term.getField();
		this.wildCardSearch = term.hasWildCard();
		
		Database db = DatabaseManager.getInstance().getTerms();
		
		if (field == QueryTerm.TermField.TITLE) {
			String key = String.format("t-%s", this.searchText);
			ids.addAll(get(db, key));
		} else if (field == QueryTerm.TermField.BODY) {
			String key = String.format("b-%s", this.searchText);
			ids.addAll(get(db, key));
		} else if (field == QueryTerm.TermField.BOTH) {
			String key;
			
			key = String.format("t-%s", this.searchText);
			ids.addAll(get(db, key));
			
			key = String.format("b-%s", this.searchText);
			ids.addAll(get(db, key));
		} else {
			System.err.println("ERROR: TermField given to search has unknown type");
		}
		
		return ids;
	}
	
	@Override
	public OperationStatus first(Cursor cursor, DatabaseEntry key,
			DatabaseEntry data) {
		OperationStatus retVal = OperationStatus.NOTFOUND;
		
		try {
			if (this.wildCardSearch)
				retVal = cursor.getSearchKeyRange(key, data, LockMode.DEFAULT);
			else
				retVal = cursor.getSearchKey(key, data, LockMode.DEFAULT);
		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return retVal;
	}

	@Override
	public OperationStatus next(Cursor cursor, DatabaseEntry key,
			DatabaseEntry data) {
		
		OperationStatus retVal = OperationStatus.NOTFOUND;
		
		if (this.forceEnd)
			return retVal;
		
		try {
			
			if (this.wildCardSearch)
				retVal = cursor.getNext(key, data, LockMode.DEFAULT);
			else
				retVal = cursor.getNextDup(key, data, LockMode.DEFAULT);
			
		} catch (Exception ex) {
			ex.getMessage();
		}
		
		return retVal;
	}

	@Override
	public Integer getId(DatabaseEntry key, DatabaseEntry data) {

		String keyString = new String(key.getData());
		String dataString = new String(data.getData());
		
		if (!keyString.contains(this.searchText)) {
			this.forceEnd = true;
			return null;
		}
		
		Integer id = null;
		
		try {
			id = new Integer(dataString);
		} catch (Exception ex) {
			id = null;
		}
		
		return id;
	}

}
