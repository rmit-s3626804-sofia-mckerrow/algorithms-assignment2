import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Random guessing player. This player is for task B.
 *
 * You may implement/extend other interfaces or classes, but ensure ultimately
 * that this class implements the Player interface (directly or indirectly).
 */
public class RandomGuessPlayer implements Player {

	/**
	 * Loads the game configuration from gameFilename, and also store the chosen
	 * person.
	 *
	 * @param gameFilename
	 *            Filename of game configuration.
	 * @param chosenName
	 *            Name of the chosen person for this player.
	 * @throws IOException
	 *             If there are IO issues with loading of gameFilename. Note you can
	 *             handle IOException within the constructor and remove the "throws
	 *             IOException" method specification, but make sure your
	 *             implementation exits gracefully if an IOException is thrown.
	 */
	private Map<String, ArrayList<String>> people;
	private String person;
	private String attribute;
	private String value;

	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		people = new HashMap<String, ArrayList<String>>();
		int counter = 0;

		try {
			FileReader reader = new FileReader(gameFilename);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String str = new String();

			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			String k = null;
			String v = null;

			while ((str = bufferedReader.readLine()) != null) {
				counter++;

				// Read file from line 11
				if (counter >= 11) {
					String string = str;
					if (string.contains("P")) {
						k = str;
						keys.add(k);
					} else if (str.length() > 2) {
						v = str;
						values.add(v);
					}
				}
				// Add person and attribute values to hashmap
				if (values.size() == 9) {
					ArrayList<String> temp = new ArrayList<String>(values);
					people.put(k, temp);
					// Clear values array before moving on to next person in file
					values.clear();
				}
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	} // end of RandomGuessPlayer()

	public Guess guess() {
		ArrayList<String> temp, attributes = new ArrayList<String>();
		
		// Get all the possible attribute-value pairs from the group of current candidate people
		for (Map.Entry<String, ArrayList<String>> entry : people.entrySet()) {
			temp = entry.getValue();
			attributes.addAll(temp);
		}
		
		// Get list of possible attribute-value pairs without duplicates
		Set<String> noDuplicates = new LinkedHashSet<String>(attributes);
		attributes.clear();
		attributes.addAll(noDuplicates);
		
		if (people.size() > 1) {
			// Get a random attribute-value pair from attributes arraylist
			Random r = new Random();
			String pair = attributes.get(r.nextInt(attributes.size()));
			String[] pairSplit;
			pairSplit = pair.split("\\s");
			attribute = pairSplit[0];
			value = pairSplit[1];
			return new Guess(Guess.GuessType.Attribute, attribute, value);
		}
		else if (people.size() == 1) {
			// Get name of person for guess
			for (Map.Entry<String, ArrayList<String>> entry : people.entrySet()) {
				person = entry.getKey();
			}
			return new Guess(Guess.GuessType.Person, "", person);
		}
		else {
			return new Guess(Guess.GuessType.Person, "", "Placeholder");
		}

	} // end of guess()

	public boolean answer(Guess currGuess) {

		// placeholder, replace
		return false;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {

		// placeholder, replace
		return true;
	} // end of receiveAnswer()

} // end of class RandomGuessPlayer
