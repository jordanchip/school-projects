package spell;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import spell.ITrie.INode;

public class SpellCorrector implements ISpellCorrector {

	ITrie dict;
	List<String> oneDistWords;
	List<String> twoDistWords;
	
	public SpellCorrector() {
		oneDistWords = new ArrayList<String>();
		twoDistWords = new ArrayList<String>();
		dict = new Trie();
	}
	@Override
	public void useDictionary(URL dictionaryFileName) throws IOException {
		try {
			Scanner sc = new Scanner(new InputStreamReader(dictionaryFileName.openStream()));
			sc.useDelimiter(",");
			while (sc.hasNext()) {
				dict.add(sc.next().toLowerCase());
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void useDictionary(String dictionaryFileName) throws IOException {
		try {
			Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(dictionaryFileName)));
			while (sc.hasNext()) {
				dict.add(sc.next().toLowerCase());
			}
			sc.close();
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<String> suggestSimilarWords(String inputWord)
			throws NoSimilarWordFoundException {
		inputWord = inputWord.toLowerCase();
		Set<String> foundWords = new TreeSet<String>();
		INode foundNode = dict.find(inputWord);
		if (foundNode != null) {
			foundWords.add(inputWord);
			return foundWords;
		}
		oneDistWords.clear();
		twoDistWords.clear();
		createDistWords(inputWord, oneDistWords);
		findWords(oneDistWords, foundWords);
		for (String w : oneDistWords) {
			createDistWords(w, twoDistWords);
		}
		findWords(twoDistWords, foundWords);
		return foundWords;
	}
	public void clear() {
		dict = new Trie();
	}
	
	private void createDistWords(String word, List<String> list) {
		addDeletion(word, list);
		addTransposition(word, list);
		addAlteration(word, list);
		addInsertion(word, list);
	}
	
	private void addDeletion(String word, List<String> list) {
		//StringBuilder original = new StringBuilder(word);
		for (int i = 0; i < word.length(); i++) {
			StringBuilder sb = new StringBuilder(word);
			list.add(sb.deleteCharAt(i).toString());
		}
	}
	
	private void addTransposition(String word, List<String> list) {
		//StringBuilder original = new StringBuilder(word);
		for (int i = 0; i < word.length() - 1; i++) {
			StringBuilder sb = new StringBuilder(word);
			char temp = sb.charAt(i);
			sb.deleteCharAt(i);
			sb.insert(i+1, temp);
			list.add(sb.toString());
		}
	}
	
	private void addAlteration(String word, List<String> list) {
		//StringBuilder original = new StringBuilder(word);
		for (int i = 0; i < word.length(); i++) {
			StringBuilder sb = new StringBuilder(word);
			for (int j = 0; j < 26; j++) {
				sb.insert(i, (char) (j+'a'));
				sb.deleteCharAt(i+1);
				list.add(sb.toString());
			}
		}
	}
	
	private void addInsertion(String word, List<String> list) {
		//StringBuilder original = new StringBuilder(word);
		for (int i = 0; i < word.length()+1; i++) {
			for (int j = 0; j < 26; j++) {
				StringBuilder sb = new StringBuilder(word);
				sb.insert(i, (char) (j+'a'));
				list.add(sb.toString());
			}
		}
	}
	
	private Set<String> findWords(List<String> list, Set<String> bestWords) {
		for (String s : list) {
			INode temp = dict.find(s);
			if (temp != null && temp.getValue() > 0)
				bestWords.add(s);
		}
		return bestWords;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dict == null) ? 0 : dict.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpellCorrector other = (SpellCorrector) obj;
		if (dict == null) {
			if (other.dict != null)
				return false;
		} else if (!dict.equals(other.dict))
			return false;
		return true;
	}
	
	

}
