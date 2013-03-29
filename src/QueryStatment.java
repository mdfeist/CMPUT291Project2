public class QueryStatment {
	
	private QueryTerm term;
	private QueryPrice[] prices;
	private QueryDate[] dates;
	
	public QueryStatment() {
		this.prices = new QueryPrice[2];
		this.dates = new QueryDate[2];
	}
	
	public void setTerm(QueryTerm term) {
		this.term = term;
	}
	
	public QueryTerm getTerm() {
		return this.term;
	}
	
	public void addDate(QueryDate date) {
		if (date == null) {
			System.out.println("ERROR: The date given to the query was null");
			return;
		}
		
		String dateString = date.getDate();
		QueryDate.SearchDate search = date.getSearchFor();
		
		if (dateString == null) {
			System.out.println("ERROR: The date string of the date given to the query was null");
			return;
		}
		
		if (search == null) {
			System.out.println("ERROR: The search date given to the query was null");
			return;
		}
		
		if (search == QueryDate.SearchDate.ON) {
			this.dates[0] = date;
			this.dates[1] = date;
		} else if (search == QueryDate.SearchDate.SINCE) {
			this.dates[0] = date;
		} else if (search == QueryDate.SearchDate.UNTIL) {
			this.dates[1] = date;
		}
	}
	
	public void printDate() {
		QueryDate lowerDate = this.dates[0];
		QueryDate upperDate = this.dates[1];
		
		if (lowerDate == null && upperDate == null) {
			System.out.println("Date: no price restrictions");
		} else if (lowerDate == null) {
			String upperDateValue = upperDate.getDate();
			System.out.println("Date: unitl " + upperDateValue);
		}  else if (upperDate == null) {
			String lowerDateValue = lowerDate.getDate();
			System.out.println("Date: since " + lowerDateValue);
		} else {
			String lowerDateValue = lowerDate.getDate();
			String upperDateValue = upperDate.getDate();
			
			System.out.println("Date: since " + lowerDateValue + " until " + upperDateValue);
		}
	}
	
	public void addPrice(QueryPrice price) {
		
		if (price == null) {
			System.out.println("ERROR: The price given to the query was null");
			return;
		}
		
		boolean greaterThan = price.isGreaterThan();
		Integer priceValue = price.getPrice();
		
		if (priceValue == null) {
			System.out.println("ERROR: The Integer value of the price given to the query was null");
			return;
		}
		
		if (greaterThan) {
			
			if (this.prices[0] == null) {
				this.prices[0] = price;
				return;
			}
			
			Integer lowerPrice = this.prices[0].getPrice();
			
			if (lowerPrice == null) {
				System.out.println("ERROR: Lower price was null");
				return;
			}
			
			if (lowerPrice.intValue() < priceValue.intValue()) {
				this.prices[0] = price;
			}
		} else {
			if (this.prices[1] == null) {
				this.prices[1] = price;
				return;
			}
			
			Integer upperPrice = this.prices[1].getPrice();
			
			if (upperPrice == null) {
				System.out.println("ERROR: Upper price was null");
				return;
			}
			
			if (upperPrice.intValue() > priceValue.intValue()) {
				this.prices[1] = price;
			}
		}
	}
	
	public void printPrice() {
		
		QueryPrice lowerPrice = this.prices[0];
		QueryPrice upperPrice = this.prices[1];
		
		if (lowerPrice == null && upperPrice == null) {
			System.out.println("Price: no price restrictions");
		} else if (lowerPrice == null) {
			Integer upperPriceValue = upperPrice.getPrice();
			System.out.println("Price: price < " + upperPriceValue);
		}  else if (upperPrice == null) {
			Integer lowerPriceValue = lowerPrice.getPrice();
			System.out.println("Price: price > " + lowerPriceValue);
		} else {
			Integer lowerPriceValue = lowerPrice.getPrice();
			Integer upperPriceValue = upperPrice.getPrice();
			
			System.out.println("Price: " + lowerPriceValue + " < price < " + upperPriceValue);
		}
	}
	
	public static QueryStatment createQuery(String input) {
		QueryStatment query = new QueryStatment();
		
		return query;
	}
} 
