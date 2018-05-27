import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

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
	private Map<String, ArrayList<String>> peopleP1;
	private Map<String, ArrayList<String>> peopleP2;
	private String playerPerson;
	private String person;
	private String attribute;
	private String value;
	private int turnCount;

	public RandomGuessPlayer(String gameFilename, String chosenName) throws IOException {
		playerPerson = chosenName;
		people = new HashMap<String, ArrayList<String>>();
		turnCount = 1;
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
			
			peopleP1 = copyHashMap(people);
			peopleP2 = copyHashMap(people);

		} catch (IOException e) {
			e.printStackTrace();
		}

	} // end of RandomGuessPlayer()

	public Guess guess() {
		Guess currGuess = null;

		// If turnCount is odd, it is player 1's turn
		if ((turnCount % 2) != 0) {
			currGuess = getGuess(peopleP2);
		}
		// If turnCount is even, it is player 2's turn
		else if ((turnCount % 2) == 0) {
			currGuess = getGuess(peopleP1);
		}
		turnCount++;
		return currGuess;
	} // end of guess()

	public boolean answer(Guess currGuess) {
		boolean answer = false;

		// If turnCount is odd, it is player 2's turn to answer
		if ((turnCount % 2) != 0) {
			// If the player's guess is an attribute-value pair
			if (currGuess.getType().toString().equals("Attribute")) {
				answer = attributeAnswer(currGuess, peopleP2);
			}
			// If the player's guess is a person
			else if (currGuess.getType().toString().equals("Person")) {
				answer = personAnswer(currGuess, peopleP2);
			}
		}
		// If turnCount is even, it is player 1's turn to answer
		else if ((turnCount % 2) == 0) {
			// If the player's guess is an attribute-value pair
			if (currGuess.getType().toString().equals("Attribute")) {
				answer = attributeAnswer(currGuess, peopleP1);
			}
			// If the player's guess is a person
			else if (currGuess.getType().toString().equals("Person")) {
				answer = personAnswer(currGuess, peopleP1);
			}
		}

		return answer;
	} // end of answer()

	public boolean receiveAnswer(Guess currGuess, boolean answer) {
		boolean receiveAnswer = false;

		if (currGuess.getType().toString().equals("Attribute")) {
			receiveAnswer = false;
		} else if (currGuess.getType().toString().equals("Person")) {
			if (answer = true)
				receiveAnswer = true;
			else
				receiveAnswer = false;
		}

		return receiveAnswer;
	} // end of receiveAnswer()

	public Map<String, ArrayList<String>> copyHashMap(Map<String, ArrayList<String>> original) {
		Map<String, ArrayList<String>> copy = new HashMap<String, ArrayList<String>>();
		for (Map.Entry<String, ArrayList<String>> entry : original.entrySet()) {
			copy.put(entry.getKey(), new ArrayList<String>(entry.getValue()));
		}
		return copy;
	}

	public Guess getGuess(Map<String, ArrayList<String>> map) {
		Guess guess = null;
		ArrayList<String> temp, attributes = new ArrayList<String>();

		// Get all the possible attribute-value pairs from the group of current
		// candidate people
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
			temp = entry.getValue();
			attributes.addAll(temp);
		}

		// Get list of possible attribute-value pairs without duplicates
		Set<String> noDuplicates = new LinkedHashSet<String>(attributes);
		attributes.clear();
		attributes.addAll(noDuplicates);

		if (map.size() > 1) {
			// Get a random attribute-value pair from attributes arraylist
			Random r = new Random();
			String pair = attributes.get(r.nextInt(attributes.size()));
			String[] pairSplit;
			pairSplit = pair.split("\\s");
			attribute = pairSplit[0];
			value = pairSplit[1];
			guess = new Guess(Guess.GuessType.Attribute, attribute, value);
		} else if (map.size() == 1) {
			// Get name of person for guess
			for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
				person = entry.getKey();
			}
			guess = new Guess(Guess.GuessType.Person, "", person);
		}

		return guess;
	}

	public boolean attributeAnswer(Guess currGuess, Map<String, ArrayList<String>> map) {
		boolean answer = false;
		ArrayList<String> peopleToRemove = new ArrayList<String>();

		String guessAttribute = currGuess.getAttribute().toString();
		String guessValue = currGuess.getValue().toString();
		String guess = guessAttribute + " " + guessValue;

		// Check if the player's chosen person has the guessed attribute-value pair
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
			if (playerPerson.equals(entry.getKey())) {
				ArrayList<String> personAttributes = entry.getValue();
				for (String attribute : personAttributes) {
					if (attribute.equals(guess)) {
						answer = true;
						break;
					} else {
						answer = false;
					}
				}
			}
		}

		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
			ArrayList<String> pairs = entry.getValue();
			// If the answer is true, get a list of people to remove from the hashmap who
			// don't have the guessed attribute-value pair
			if (answer = true) {
				if (!pairs.contains(guess)) {
					person = entry.getKey();
					peopleToRemove.add(person);
				}
			}
			// If the answer is false, get a list of people to remove from the hashmap who
			// have the guessed attribute-value pair
			else if (answer = false) {
				if (pairs.contains(guess)) {
					person = entry.getKey();
					peopleToRemove.add(person);
				}
			}
		}

		// Remove people from the hashmap
		for (int i = 0; i < peopleToRemove.size(); i++) {
			person = peopleToRemove.get(i);

			for (Iterator<Entry<String, ArrayList<String>>> iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, ArrayList<String>> e = iterator.next();
				if (e.getKey().equals(person)) {
					iterator.remove();
				}
			}
		}

		return answer;
	}

	public boolean personAnswer(Guess currGuess, Map<String, ArrayList<String>> map) {
		boolean answer = false;

		// Check if the guessed person is the person chosen by the player
		for (Map.Entry<String, ArrayList<String>> entry : map.entrySet()) {
			if (playerPerson.equals(entry.getKey())) {
				answer = true;
				break;
			} else
				answer = false;
		}

		return answer;
	}

} // end of class RandomGuessPlayer
