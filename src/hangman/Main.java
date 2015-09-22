package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Main {

	public static void main(String[] args) throws IOException, GuessAlreadyMadeException {
		
		File dictionary = new File(args[0]);
		int wordLength = Integer.parseInt(args[1]);
		int guesses = Integer.parseInt(args[2]);
		Set<Character> usedLetters = new TreeSet<Character>();
		Character[] evilWord = new Character[wordLength];
		EvilHangmanGame game = new EvilHangmanGame();
		Set<String> newSet = new HashSet<String>();
		
		game.startGame(dictionary, wordLength);
		
		while(guesses != 0) {
			
			System.out.println("You have " + guesses + " guesses left");
			
			System.out.println("Used letters: " + game.toString(usedLetters));
			
			System.out.println("Word: " + game.toString(evilWord));
			
			System.out.print("Enter guess: ");
			Scanner user_input = new Scanner(System.in);
			String guess = user_input.next().toLowerCase();
			char c = ' ';
			if(guess.length() > 1) {
				System.out.println("Invalid Input: Try Again\n");
				continue;
			}
			if(guess.length() > 0)
				c = guess.charAt(0);
			if(!Character.isLetter(c)) {
				System.out.println("Invalid Input: Try Again\n");
				continue;
			}
			newSet = game.makeGuess(c);
			String example = newSet.iterator().next();
			ArrayList<Integer> locations = game.getLocations(c, example);
			if(locations.size() > 0) {
				game.addChars(evilWord, locations, c);
				System.out.println("Yes, there are " + locations.size() + " " + c + "'s");
			}
			if(locations.size() == 0) {
				System.out.println("Sorry, there are no " + c + "'s");
				guesses--;
			}
			if(game.guessedIt(evilWord)) {
				System.out.println("You Win! Correct Word: " + game.toString(evilWord));
				break;
			}
		}
		
		if(!game.guessedIt(evilWord)) {
		System.out.println("You lose!");
		System.out.println("The word was: " + newSet.iterator().next().toString());
		}
	}
	
}
