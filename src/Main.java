import java.io.IOException;
import java.util.Date;
import java.util.Set;

/**
 * Main Program.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class Main {
	
	public static void main(String[] args) {

		Process process = null;  
		int exitCode;

		// Clean Files
		try {
			process = Runtime.getRuntime().exec("rm -r data/");
			exitCode = process.waitFor();
			System.out.println("Process Clean returned: " + exitCode);

			process = Runtime.getRuntime().exec("mkdir data");
			exitCode = process.waitFor();
			System.out.println("Process make data/ returned: " + exitCode);
		} catch (IOException e) {
			System.err.println("Error: When creating new data file");
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Enter xml file name (example file.xml):");
		String xmlFile = Keyboard.getString();

		if (xmlFile == null)
		{
			System.err.println("ERROR: Keyboard input was null");
			return;
		}
		
		// Start Time
		long lStartTime = new Date().getTime();

		// Parse XML file
		XMLParser xml = new XMLParser();
		boolean err = xml.parseFile(xmlFile);
		
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
		
		// End Time
		long lEndTime = new Date().getTime();

		// Print elapsed time
		System.out.println("Final Time: " + (lEndTime - lStartTime));
		
		boolean run = true;
		while (run) {
			System.out.println("Enter Query (exit to quit):");
			String input = Keyboard.getString();
			
			if (input == null) {
				System.err.println("ERROR: Keyboard input was null");
				continue;
			}
			
			if (input.equals("exit")) {
				System.out.println("Exiting Program ...");
				break;
			}
			
			QueryStatment query = QueryStatment.createQuery(input);
			Set<Integer> ids = query.execute();
			
			if (ids == null) {
				System.err.println("ERROR: Invalid query");
			} else {	
				for (Integer id : ids) {
					//System.out.println(id);
					QueryAdSearch adQuery = new QueryAdSearch();
					Ad ad = adQuery.getAd(id);
					ad.print();
				}
			}
		}
		
		System.out.println("Closing Database ...");
		DatabaseManager.getInstance().close();
		System.out.println("Bye");
	}

	

}
