package hangman;

import hangman.IEvilHangmanGame.GuessAlreadyMadeException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class EvilHangmanGame implements IEvilHangmanGame {

	private Set<String> set;
	
	public EvilHangmanGame() {
		set = new HashSet<String>();
	}
	
	@Override
	public void startGame(File dictionary, int wordLength) {
		
		try {
		Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(dictionary)));		
		while(sc.hasNext()) {			
			String next = sc.next().toLowerCase();
			if(next.length() == wordLength) {
				set.add(next);
			}
		}		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		Set<String> newSet = new HashSet<String>();
		String keyTemp = "";
		int wordLength = set.iterator().next().length();
		for(String s : set) {
			
			ArrayList<Integer> locations = getLocations(guess, s);
			String key = makeKey(wordLength, locations, guess);
			
			if(map.get(key) == null) {
				Set<String> value = new HashSet<String>();
				map.put(key, value);
			}
			Set<String> value = map.get(key);
			value.add(s);
			map.put(key, value);
		}
		
		for(Map.Entry<String, Set<String>> entry : map.entrySet()) {
			String key = entry.getKey();
			Set<String> value = entry.getValue();
			int keyLength = wordLength;
			
			if(value.size() > newSet.size()) {
				newSet = value;
				keyTemp = key;
			}
			else if(value.size() == newSet.size()) {
				
				if(getQuantity(keyTemp) == 0)
					continue;
				else if(getQuantity(key) == 0) {
					newSet = value;
					keyTemp = key;
					continue;
				}
				else if(getQuantity(keyTemp) > getQuantity(key))
					continue;
				else if(getQuantity(key) > getQuantity(keyTemp)) {
					newSet = value;
					keyTemp = key;
					continue;
				}
				else if(getQuantity(key) == getQuantity(keyTemp)) {
					while(keyLength != 0) {
						
						if(key.charAt(keyLength-1) != keyTemp.charAt(keyLength-1)) {
							if(keyTemp.charAt(keyLength-1) != '-')
								break;
							if(key.charAt(keyLength-1) != '-') {
								newSet = value;
								keyTemp = key;
								break;
							}
						}
						keyLength--;
					}					
				}
			}
		}
		set = newSet;
		return newSet;
	}
	
	public ArrayList<Integer> getLocations(char guess, String s) {
		
		ArrayList<Integer> locations = new ArrayList<>();	
		for(int i = 0; i < s.length(); i++) {			
			if(s.charAt(i) == guess) {
				locations.add(i);
			}
		}	
		return locations;
	}
	
	public String makeKey(int wordLength, ArrayList<Integer> locations, char guess) {
		
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < wordLength; i++) {
			
			if(locations.contains(i)) {
				sb.append(guess);
			}
			else
				sb.append('-');
		}	
		return sb.toString();
	}
	
	public int getQuantity(String key) {
		
		int quantity = 0;
		for(int i = 0; i < key.length(); i++) {
			if(key.charAt(i) != '-')
				quantity++;			
		}		
		return quantity;
	}
	
	public String toString(Set<Character> usedLetters) {
		
		StringBuilder sb = new StringBuilder("");
		for(Character c : usedLetters) {
			sb.append(c + " ");		
		}		
		return sb.toString();
	}

	public String toString(Character[] evilWord) {
		
		StringBuilder sb = new StringBuilder("");
		for(Character c : evilWord) {			
			if(c == null)
				sb.append('-');
			else
				sb.append(c);
		}	
		return sb.toString();
	}
	
	public Character[] addChars(Character[] evilWord, ArrayList<Integer> locations, char c) {
		
		for(Integer i : locations) {	
			evilWord[i] = c;
		}
		return evilWord;
	}
	
	public boolean guessedIt(Character[] evilWord) {
		
		for(Character c : evilWord) {
			if(c == null)
				return false;
		}	
		return true;
	}
	
}
