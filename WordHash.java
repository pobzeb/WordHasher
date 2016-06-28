public class WordHash {
	// Define the LETTERS we are allowed to use.
	public static final String LETTERS = "acdegilmnoprstuw";

	// Hash function from example psudo code.
	public long hash(String str) {
		long answer = 7;
		for (int i = 0; i < str.length(); i++) {
			answer = (answer * 37 + LETTERS.indexOf(str.charAt(i)));
		}

		return answer;
	}

	// ReverseHash method.
	// Uses a recursive permutation to test posible strings
	// against an answer hash.
	//
	// answer - The hash we need to match
	// str - The resulting string that is returned.
	//       str can contain a partial string to start the test
	// maxLen - Do not let the string grow past this value.
	//
	// Returns the found string that matches the answer hash or null
	// if no match found.
	public String reverseHash(long answer, String str, int maxLen) {
		// Check the string length against the maxLen.
		if (str.length() > maxLen) return null;

		// If the hash of this str matches the answer hash, we found
		// a posible answer.
		if (hash(str) == answer) {
			// Return the string.
			return str;
		}
		else {
			// Loop over the LETTERS and try each permutation.
			String result = null;
			for (int i = 0; i < LETTERS.length(); i++) {
				// Recurse this method with this character.
				result = reverseHash(answer, str+LETTERS.charAt(i), maxLen);

				// If the result was not null, we found the string.
				if (result != null) break;
			}

			// Return the result.
			return result;
		}
	}

	public static void main(String[] args) {
		// Define the hash answer we need to find.
		final long answer = Long.parseLong("945924806726376");

		// We want to limit the result string to 9 characters.
		final int maxLen = 9;

		// Hold the threads.
		Thread[] threads = new Thread[LETTERS.length()];

		// Loop over the LETTERS string and start one thread for each character.
		System.out.println("Running...");
		for (int i = 0; i < LETTERS.length(); i++) {
			// Get the character to use for this thread.
			final String startChar = String.valueOf(LETTERS.charAt(i));

			// Create a thread for this character.
			threads[i] = new Thread(LETTERS.charAt(i)+"Thread") {
				@Override
				public void run() {
					// Create an instance of the app.
					WordHash app = new WordHash();

					// Run the reverseHash with this starting character and max length.
					String result = app.reverseHash(answer, startChar, maxLen);

					// Print out the result if there was one.
					if (result != null) System.out.println("Found a result: "+result);
				}
			};

			// Start the thread.
			threads[i].start();
		}

		// Wait for the threads to finish.
		for (int i = 0; i < threads.length; i++) {
			try { threads[i].join(); } catch(InterruptedException ex) {}
		}
		System.out.println("Finished.");
	}
}
