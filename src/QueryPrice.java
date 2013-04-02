/**
 * Holds information for a price query. Includes price
 * and whether to search greater than or less than.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class QueryPrice {

	private Integer price;
	private boolean greaterThan;
	
	public QueryPrice() {
		this.price = new Integer(0);
		this.greaterThan = false;
	}
	
	public QueryPrice(Integer price, boolean greaterThan) {
		this.price = price;
		this.greaterThan = greaterThan;
	}
	
	public Integer getPrice() {
		return this.price;
	}
	
	public boolean isGreaterThan() {
		return this.greaterThan;
	}
}
