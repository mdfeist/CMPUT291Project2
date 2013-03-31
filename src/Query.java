import com.sleepycat.db.Database;


/**
 * Storage class for a query. Contains the database to query
 * and the content of the query as a string.
 * @author gcoomber
 *
 */
public class Query
{
	private String search = "";
	private Database db;
	
	public Query(Database db, String search) {
		this.db = db;
		this.search = search;
	};
	
	public Database getDatabase() {
		return db;
	}
	
	public String getSearch() {
		return search;
	}
}
