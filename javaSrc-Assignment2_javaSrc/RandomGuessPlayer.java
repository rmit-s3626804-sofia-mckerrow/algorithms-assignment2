import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		people = new HashMap<String, ArrayList<String>>();
		int counter = 0;

		try {
			FileReader reader = new FileReader(gameFilename);
			BufferedReader bufferedReader = new BufferedReader(reader);

			String str = new String();

			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<String> values = new ArrayList<String>();
			String key = null;
			String value = null;

			while ((str = bufferedReader.readLine()) != null) {
				counter++;

				// Read file from line 11
				if (counter >= 11) {
					String string = str;
					if (string.contains("P")) {
						key = str;
						keys.add(key);
					} else if (str.length() > 2) {
						value = str;
						values.add(value);
					}
				}
				// Add person and attribute values to hashmap
				if (values.size() == 9) {
					ArrayList<String> temp = new ArrayList<String>(values);
					people.put(key, temp);
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

		// placeholder, replace
		return new Guess(Guess.GuessType.Person, "", "Placeholder");
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
