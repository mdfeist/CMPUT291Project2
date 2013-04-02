import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Allows users to get input from keyboard.
 * 
 * @author Michael Feist and George Coomber
 *
 */
public class Keyboard {
	/**
	 * Reads user input from the keyboard. 
	 * @return keyboard input
	 */
	public static String getString() {
		String str = null;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return str;
	}

}
