import com.sleepycat.db.Database;


/**
 * A class that interprets the user's input for a query and queries
 * the database.
 * @author gcoomber
 *
 */
public class QueryParser
{
	/**
	 * Get user input and interpret the query based on the project specs.
	 */
	public Query getQuery() {
		Database db;
		String input = "";
		String search = "";
		Keyboard.getString();
		
		db = DatabaseManager.getInstance().getTerms();
		
		Query query = new Query(db, search);
		
		return query;
	}
}
