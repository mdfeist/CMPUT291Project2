import java.util.ArrayList;
import java.util.Set;

public class QueryStatment {
	
	private ArrayList<QueryTerm> terms;
	private QueryPrice[] prices;
	private QueryDate[] dates;
	
	public QueryStatment() {
		this.terms = new ArrayList<QueryTerm>();
		this.prices = new QueryPrice[2];
		this.dates = new QueryDate[2];
	}
	
	public QueryTerm addTerm(QueryTerm term) {
		
		for (QueryTerm t : terms) {
			if (t.equals(term)) {
				return t;
			}
		}
		
		this.terms.add(term);
		
		return term;
	}
	
	public QueryTerm getTermAt(int index) {
		return this.terms.get(index);
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
			System.out.println("Date: no date restrictions");
		} else if (lowerDate == null) {
			String upperDateValue = upperDate.getDate();
			System.out.println("Date: until " + upperDateValue);
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
	
	public Set<Integer> execute() {
		Set<Integer> ids = null;
		
		if (this.terms != null) {
			TermQuerySearch termSearch = new TermQuerySearch();
			
			for (QueryTerm t : this.terms) {
				if (ids == null)
					ids = termSearch.get(t);
				else
					ids.retainAll(termSearch.get(t));
			}
		}
		
		QueryDate lowerDate = this.dates[0];
		QueryDate upperDate = this.dates[1];
		
		if (lowerDate == null && upperDate == null) {
		} else if (lowerDate == null) {
			DateQuerySearch dateSearch = new DateQuerySearch();
			
			if (ids == null)
				ids = dateSearch.get(upperDate);
			else
				ids.retainAll(dateSearch.get(upperDate));
		}  else if (upperDate == null) {
			DateQuerySearch dateSearch = new DateQuerySearch();
			
			if (ids == null)
				ids = dateSearch.get(lowerDate);
			else
				ids.retainAll(dateSearch.get(lowerDate));
		} else {
			DateQuerySearch dateSearch = new DateQuerySearch();
			
			if (ids == null)
				ids = dateSearch.get(lowerDate);
			else
				ids.retainAll(dateSearch.get(lowerDate));
			
			ids.retainAll(dateSearch.get(upperDate));
		}
		
		QueryPrice lowerPrice = this.prices[0];
		QueryPrice upperPrice = this.prices[1];
		
		if (lowerPrice == null && upperPrice == null) {
		} else if (lowerPrice == null) {
			PriceQuerySearch priceSearch = new PriceQuerySearch();
			
			if (ids == null)
				ids = priceSearch.get(upperPrice);
			else
				ids.retainAll(priceSearch.get(upperPrice));
		}  else if (upperPrice == null) {
			PriceQuerySearch priceSearch = new PriceQuerySearch();
			
			if (ids == null)
				ids = priceSearch.get(lowerPrice);
			else
				ids.retainAll(priceSearch.get(lowerPrice));
		} else {
			PriceQuerySearch priceSearch = new PriceQuerySearch();
			
			if (ids == null)
				ids = priceSearch.get(lowerPrice);
			else
				ids.retainAll(priceSearch.get(lowerPrice));
			
			ids.retainAll(priceSearch.get(upperPrice));
		}
		
		return ids;
	}
	
	private static QueryDate getDate(int position, String input, QueryDate.SearchDate type) {
		QueryDate qDate = null;
		
		if (position <= input.length())
		{
			String date = input.substring(position);
			date = date.trim();
			
			if (date.length() >= 10) {
				date = date.substring(0, 10);
				
				String dateFormat = date.replaceAll("[0-9]", "*");
				
				if (!dateFormat.equals("****/**/**")) {
					System.err.println("ERROR: Date is in wrong format YYYY/MM/DD");
				} else {
					qDate = new QueryDate(date, type);
				}
			}
		}
		
		return qDate;
	}
	
	public static QueryStatment createQuery(String input) {
		QueryStatment query = new QueryStatment();
		
		input = input.trim().toLowerCase();
		int positionUntil = input.indexOf("until");
		
		String untilString = input;
		
		if (positionUntil >= 0)
			input = input.substring(0, positionUntil);
		
		while (positionUntil >= 0) {
			int position = positionUntil + "until".length() + 1;
			QueryDate date = getDate(position, untilString, QueryDate.SearchDate.UNTIL);
			
			if (date != null) {
				query.addDate(date);
				
				untilString = untilString.substring(position);
				positionUntil = untilString.indexOf("until");
				
				int startRemove = untilString.indexOf(date.getDate()) + date.getDate().length();
				
				if (startRemove >= 0) {
					if (positionUntil < 0) {
						input += untilString.substring(startRemove);
					} else {
						input += untilString.substring(startRemove, positionUntil);
					}
				}
				
			} else {
				System.err.println("ERROR: Invalid query failed at until");
				return null;
			}
		}
		
		int positionSince = input.indexOf("since");
		
		String sinceString = input;
		
		if (positionSince >= 0)
			input = input.substring(0, positionSince);
		
		while (positionSince >= 0) {
			int position = positionSince + "since".length() + 1;
			QueryDate date = getDate(position, sinceString, QueryDate.SearchDate.SINCE);
			
			if (date != null) {
				query.addDate(date);
				
				sinceString = sinceString.substring(position);
				positionSince = sinceString.indexOf("since");
				
				int startRemove = sinceString.indexOf(date.getDate()) + date.getDate().length();
				
				if (startRemove >= 0) {
					if (positionSince < 0) {
						input += sinceString.substring(startRemove);
					} else {
						input += sinceString.substring(startRemove, positionSince);
					}
				}
				
			}  else {
				System.err.println("ERROR: Invalid query failed at since");
				return null;
			}
		}
		
		int positionPrice = input.indexOf("price");
		String priceString = input;
		
		if (positionPrice >= 0)
			input = input.substring(0, positionPrice);
		
		while (positionPrice >= 0) {
			int position = positionPrice + "price".length();
			Integer priceValue = null;
			
			boolean err = false;
			
			if (position <= priceString.length())
			{
				String priceSign = priceString.substring(position);
				priceSign = priceSign.trim();
				
				
				if (priceSign.length() >= 1) {
					char sign = priceSign.charAt(0);
					boolean greaterThan = false;
					
					if (sign == '<')
						greaterThan = false;
					else if (sign == '>')
						greaterThan = true;
					
					String price = priceSign.substring(1);
					price = price.trim();
					
					int positionOfPrice = price.indexOf(' ');
					
					if (positionOfPrice < 0) {
						price = price.substring(0);
					} else {
						price = price.substring(0, positionOfPrice);
					}
					
					try {
						priceValue = new Integer(price);
					} catch (Exception ex) {
						priceValue = null;
					}
					
					if (priceValue != null) {
						QueryPrice qPrice = new QueryPrice(priceValue, greaterThan);
						query.addPrice(qPrice);
					} else {
						err = true;
					}
				} else {
					err = true;
				}
			} else {
				err = true;
			}
			
			if (err) {
				System.err.println("ERROR: Invalid query failed at price");
				return null;
			}
			
			priceString = priceString.substring(position);
			positionPrice = priceString.indexOf("price");
			
			if (priceValue != null) {
				
				int startRemove = priceString.indexOf(priceValue.toString()) + priceValue.toString().length();
				
				if (startRemove >= 0) {
					if (positionPrice < 0) {
						input += priceString.substring(startRemove);
					} else {
						input += priceString.substring(startRemove, positionPrice);
					}
				}
			}
		}
		
		String[] termInput = input.split(" ");
		
		for (String str : termInput) {
			str = str.trim();
			
			if (str.length() >= 3 || str.equals("%")) {
				query.addTerm(new QueryTerm(str));
			} else {
				if (str.length() > 0) {
					System.err.println("ERROR: String to short " + str);
				}
			}
		}
		
		return query;
	}
} 
