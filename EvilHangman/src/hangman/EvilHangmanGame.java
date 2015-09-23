package hangman;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class EvilHangmanGame implements IEvilHangmanGame{

	Set<String> currentDict;
	Set<String> usedLetters;
	String currentPattern;
	
	
	public EvilHangmanGame() {
		currentDict = new TreeSet<String>();
		usedLetters = new TreeSet<String>();
	}
	
	/**
	 * 
	 * @return All the used letters in alphabetical order
	 */
	public String usedLetters() {
		StringBuilder returnSequence = new StringBuilder();
		for (String letter : usedLetters) {
			returnSequence.append(letter + " ");
		}
		return returnSequence.toString();
	}
	
	public String getCurrentPattern() {
		return currentPattern;
	}
	
	/**
	 * @param dictionary The file that contains the dictionary
	 * @param wordLength The length of word that the user must guess
	 */
	@Override
	public void startGame(File dictionary, int wordLength) {
		try {
			Scanner scan = new Scanner(new BufferedInputStream(new FileInputStream(dictionary)));
			while (scan.hasNext()) {
				String nextWord = scan.next();
				if (nextWord.length() == wordLength) {
					currentDict.add(nextWord);
				}
			}
			initializePattern(wordLength);
			scan.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
		
		if (usedLetters.contains(Character.toString(guess)))
			throw new GuessAlreadyMadeException();
		usedLetters.add(Character.toString(guess));
		Map<String, Set<String>> partitionSets = partitionWords(guess);
		return biggestPartitionFromMap(partitionSets, guess);
	}
	
	public void setDict(Set<String> dict) {
		currentDict = dict;
	}
	/**
	 * 
	 * @return the dictionary in String format
	 */
	public String dictToString() {
		StringBuilder sb = new StringBuilder();
		for (String str : currentDict) {
			sb.append(str + "\n");
		}
		return sb.toString();
	}
	
	public int numSuccessfulGuesses(char guess, String word) {
		//String word = currentDict.iterator().next();
		int successfulGuesses = 0;
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == guess)
				successfulGuesses++;
		}
		return successfulGuesses;
	}
	
	public boolean hasWon() {
		for (int i = 0; i < currentPattern.length(); i++) {
			if (currentPattern.charAt(i) == '-')
				return false;
		}
		return true;
	}
	
	public String getWinningWord() {
		return currentDict.iterator().next();
	}
	
	private void initializePattern(int wordLength) {
		StringBuilder patternBuilder = new StringBuilder();
		for (int i = 0; i < wordLength; i++) {
			patternBuilder.append("-"); 
		}
		currentPattern = patternBuilder.toString();
	}
	
	private Map<String, Set<String>> partitionWords(char guess) {
		Map<String, Set<String>> partitionSets = new HashMap<String, Set<String>>();
		
		for (String word : currentDict) {
			
			String pattern = getPatternOfWord(word, guess);
			Set<String> currentSet = partitionSets.get(pattern);
			if (currentSet == null) {
				currentSet = new TreeSet<String>();
			}
			currentSet.add(word);
			partitionSets.put(pattern, currentSet);
			
		}
		
		return partitionSets;
	}
	
	private String getPatternOfWord(String word, char guess) {
		
		StringBuilder pattern = new StringBuilder();
		for (int i = 0; i < word.length(); i++) {
			if (currentPattern.charAt(i) == '-') {
				if (word.charAt(i) == guess) {
					pattern.append(guess);
				}
				else
					pattern.append("-");
			}
			else
				pattern.append(currentPattern.charAt(i));
		}
		return pattern.toString();
	}
	
	private Set<String> biggestPartitionFromMap(Map<String, Set<String>> partitionSets, char guess) {
		Set<String> biggestSet = new TreeSet<String>();
		int maxSize = 0;
		
		for (Map.Entry<String, Set<String>> iter : partitionSets.entrySet()) {
			int currentSize = iter.getValue().size();
			if (currentSize > maxSize) {
				maxSize = currentSize;
				biggestSet = iter.getValue();
				currentPattern = iter.getKey();
			}
			else if (currentSize == maxSize) {
				if (hasPrecedence(iter.getKey(), currentPattern, guess)) {
					maxSize = currentSize;
					biggestSet = iter.getValue();
					currentPattern = iter.getKey();
				}
			}
		}
		currentDict = biggestSet;
		return biggestSet;
	}
	
	private boolean hasPrecedence(String pattern1, String pattern2, char guess) {
		int numRightGuesses1 = numSuccessfulGuesses(guess, pattern1);
		int numRightGuesses2 = numSuccessfulGuesses(guess, pattern2);
		if (numRightGuesses1 < numRightGuesses2)
			return true;
		else if (numRightGuesses1 == numRightGuesses2) {
			return hasRightMostPrecedence(pattern1, pattern2, guess);
		}
		return false;
	}
	
	private boolean hasRightMostPrecedence(String pattern1, String pattern2, char guess) {
		int indexOfRightGuessedLetter1 = -1;
		int indexOfRightGuessedLetter2 = -1;
		while (indexOfRightGuessedLetter1 == indexOfRightGuessedLetter2) {
			for (int i = indexOfRightGuessedLetter1 + 1; i < pattern1.length(); i++) {
				if (pattern1.charAt(i) == guess) {
					indexOfRightGuessedLetter1 = i;
					break;
				}
			}
			for (int i = indexOfRightGuessedLetter2 + 1; i < pattern2.length(); i++) {
				if (pattern2.charAt(i) == guess) {
					indexOfRightGuessedLetter2 = i;
					break;
				}
			}
			if (indexOfRightGuessedLetter1 > indexOfRightGuessedLetter2)
				return true;
		}
		return false;
	}

}
