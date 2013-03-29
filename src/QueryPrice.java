
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
