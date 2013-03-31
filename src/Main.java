import java.io.IOException;
import java.util.Date;
import java.util.Set;


public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Process process = null;  
		int exitCode;
		
		// Start Time
		long lStartTime = new Date().getTime();

		// Clean Files
		try {
			process = Runtime.getRuntime().exec("rm -r data/");
			exitCode = process.waitFor();
			System.out.println("Process Clean returned: " + exitCode);

			process = Runtime.getRuntime().exec("mkdir data");
			exitCode = process.waitFor();
			System.out.println("Process data returned: " + exitCode);
		} catch (IOException e) {
			System.err.println("Error: When creating new data file");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Parse XML file
		XMLParser xml = new XMLParser();
		boolean err = xml.parseFile("test_10.xml");
		
		if (!err) {
			return;
		}
		
		// Sort Files and Build Indexes
		try {
			// Build Index Files
			process = Runtime.getRuntime().exec("sh BuildIndex.sh");
			exitCode = process.waitFor();
			System.out.println("Process Build Index returned: " + exitCode);
			
		} catch (IOException e) {
			System.err.println("ERROR: Unable to execute BuildScript.sh");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// Tests
		//DatabaseManager.getInstance().test();
		/*
		System.out.println("\nSearch 4runner in terms tile");
		DatabaseManager.getInstance().getTerms("t-4runner");
		System.out.println("\nSearch 4runner in terms body");
		DatabaseManager.getInstance().getTerms("b-4runner");
		System.out.println("\nSearch with in terms");
		DatabaseManager.getInstance().getTermsBoth("with");
		System.out.println("\nSearch apart% in terms");
		DatabaseManager.getInstance().getTermsBoth("apart%");
		System.out.println("\nSearch 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDate("2012/06/24");
		System.out.println("\nSearch since 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDatesFrom("2012/06/24");
		System.out.println("\nSearch until 2012/06/24 in pdates");
		DatabaseManager.getInstance().getDatesTo("2012/06/24");
		System.out.println("\nSearch price < 16995");
		DatabaseManager.getInstance().getPrices("16995", false);
		System.out.println("\nSearch price > 16995");
		DatabaseManager.getInstance().getPrices("16995", true);
		
		System.out.println("\nQuery:");
		QueryStatment q = new QueryStatment();
		
		QueryDate lowerD = new QueryDate("2012/05/08", QueryDate.SearchDate.SINCE);
		QueryDate upperD = new QueryDate("2013/01/07", QueryDate.SearchDate.UNTIL);
		
		q.addDate(lowerD);
		q.addDate(upperD);
		
		q.printDate();
		
		QueryPrice lowerP = new QueryPrice(new Integer(0), true);
		QueryPrice qreaterP = new QueryPrice(new Integer(2000), false);
		
		q.addPrice(lowerP);
		q.addPrice(qreaterP);
		
		q.printPrice();
		
		QueryTerm t = new QueryTerm("great%");
		TermQuerySearch termSearch = new TermQuerySearch();
		Set<Integer> ids = termSearch.get(t);
		
		PriceQuerySearch priceSearch = new PriceQuerySearch();
		ids.retainAll(priceSearch.get(lowerP));
		ids.retainAll(priceSearch.get(qreaterP));
		
		DateQuerySearch dateSearch = new DateQuerySearch();
		ids.retainAll(dateSearch.get(lowerD));
		ids.retainAll(dateSearch.get(upperD));
		*/
		boolean run = true;
		while (run) {
			System.out.println("Enter Query:");
			String input = Keyboard.getString();
			
			if (input == null) {
				System.err.println("ERROR: Keyboard input was null");
				return;
			}
			
			if (input.equals("q")) {
				return;
			}
			
			QueryStatment query = QueryStatment.createQuery(input);
			Set<Integer> ids = query.execute();
			
			for (Integer id : ids) {
				//System.out.println(id);
				QueryAdSearch adQuery = new QueryAdSearch();
				Ad ad = adQuery.getAd(id);
				ad.print();
			}
		}
		
		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Final Time: " + (lEndTime - lStartTime));
	}

	

}
