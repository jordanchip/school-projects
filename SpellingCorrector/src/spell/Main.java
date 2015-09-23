package spell;

import java.io.IOException;

import spell.ISpellCorrector.NoSimilarWordFoundException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws NoSimilarWordFoundException, IOException {
		
		String dict = args[0];
		String word = args[1];
		
		ISpellCorrector corrector = new SpellCorrector();
		corrector.useDictionary(dict);
		
		System.out.println("Suggestion is: " + corrector.suggestSimilarWord(word));
	}

}
