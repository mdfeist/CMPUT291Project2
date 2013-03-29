
public class QueryDate {
	
	public static enum SearchDate {UNTIL, SINCE, ON}
	
	private String date;
	private SearchDate search;
	
	public QueryDate() {
		this.date = null;
		this.search = SearchDate.ON;
	}
	
	public QueryDate(String date, SearchDate search) {
		this.date = date;
		this.search = search;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public SearchDate getSearchFor() {
		return this.search;
	}
}
