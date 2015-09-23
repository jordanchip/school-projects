package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.File;
import java.util.Scanner;

public class EvilHang {

	public static void main(String[] args) {
		
		File fileName = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		
		if (wordLength <= 1) {
			System.out.println("Incorrect word length (must be 2 or more");
			return;
		}
		if (guesses <= 0) {
			System.out.println("Incorrect guess number (must be 1 or more");
		}
		
		EvilHangmanGame game = new EvilHangmanGame();
		game.startGame(fileName, wordLength);
		Scanner inputScanner = new Scanner(System.in);
		
		while (guesses > 0) {
			if (game.hasWon()) {
				System.out.println("You won! Congradulations!\n" + game.getCurrentPattern());
				inputScanner.close();
				return;
			}
			
			System.out.println("You have " + guesses + " guesses left");
			System.out.println("Used letters: " + game.usedLetters());
			System.out.println("Word: " + game.getCurrentPattern());
			System.out.println("Enter guess: ");
			String input = inputScanner.nextLine();
			while (!input.matches("[a-zA-Z]")) {
				System.out.print("Invalid input.  Please try again: ");
				input = inputScanner.nextLine();
			}
			try {
				char guess = input.toLowerCase().charAt(0);
				//System.out.println(game.dictToString());
				game.setDict(game.makeGuess(guess));
				//System.out.println(game.dictToString());
				int successfulGuesses = game.numSuccessfulGuesses(guess, game.getCurrentPattern());
				if (successfulGuesses > 1) {
					System.out.println("Yes, there are " + successfulGuesses + 
										" " + guess + "'s");
				}
				else if (successfulGuesses == 1) {
					System.out.println("Yes, there is 1 " + guess);
				}
				else {
					System.out.println("Sorry, there are no " + guess + "'s");
					guesses--;
				}
			}
			catch (GuessAlreadyMadeException e) {
				System.out.println("That guess has already been made.  Please try again.");
			}
		}
		System.out.println("You lose! The correct word was: \n" + game.getWinningWord());
		inputScanner.close();
		
	}

}
