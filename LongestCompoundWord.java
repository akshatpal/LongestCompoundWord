// package compound;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map.Entry;
class Pair<T> {
	
	private T first;
	private T second;
	
	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}
	
	// return first element
	public T first() {
		return first;
	}
	
	// return second element
	public T second() {
		return second;
	}
}
class Trie {
	
	// inner class, only for the use of Trie
	private class TrieNode {
		@SuppressWarnings("unused")
		private char val;			// character stored in the node
		private HashMap<Character, TrieNode> children;	// map name of string to the node
														// which has the string as value
		private boolean isWord;		// if the node is at the end of a word
		
		// constructor
		public TrieNode(char val) {
			this.val = val;
			children = new HashMap<Character, TrieNode>();
			isWord = false;
		}
		
		// add child to trienode
		public void addChild(char child) {
			children.put(child, new TrieNode(child));
		}
		
		// get child of trienode that has the same character as the give one
		public TrieNode getChild(char child) {
			if (!children.keySet().contains(child)) {
				return null;
			}
			
			return children.get(child);
		}
		
		// return true if child exists
		public boolean containsChild(char child) {
			return children.keySet().contains(child);
		}
	}
	
	private TrieNode root;
	private TrieNode curr;
	
	public Trie() {
		root = new TrieNode(' ');	// root
		curr = root;
	}
	
	// insert a word to trie
	public void insert(String s) {
		char letter;
		curr = root;
		
		// traverse every letter of a word
		// update trie if a letter is not in the structure
		for (int i = 0; i < s.length(); i++) {
			letter = s.charAt(i);
			
			if (!curr.containsChild(letter)) {
				curr.addChild(letter);
			} 
			
			curr = curr.getChild(letter);
		}
		
		// mark last letter as the end of a word
		curr.isWord = true;
	}
	
	// return starting indices of all suffixes of a word
	public List<Integer> getSuffixesStartIndices(String s) {
		List<Integer> indices = new LinkedList<Integer>();	// store indices
		char letter;
		curr = root;	// start from root
		
		for (int i = 0; i < s.length(); i++) {
			letter = s.charAt(i);
			
			// if the current letter doesn't have one letter as child
			// which means trie currently doesn't have the relationship
			// returns indices of suffixes
			if (!curr.containsChild(letter))
				return indices;
			
			// move on to the child node
			curr = curr.getChild(letter);
			
			// if the letter is an end to a word, it means after the letter is a suffix
			// update indices
			if (curr.isWord)
				indices.add(i + 1);
		}
		
		return indices;
	}
	
}

public class LongestCompoundWord {

	public static void main(String[] args) throws FileNotFoundException {
		
		// change file name accordingly
		File file = new File("Input_02.txt");  //Give your pathname accordingly

		// Trie
		Trie trie = new Trie();
		LinkedList<Pair<String>> queue = new LinkedList<Pair<String>>();
		
		// used to calculate the total amount of compound words
		HashSet<String> compoundWords = new HashSet<String>();
		
		// scan the file
		@SuppressWarnings("resource")
		Scanner s = new Scanner(file);

		String word;				// a word
		List<Integer> sufIndices;	// indices of suffixes of a word
		
		// read words from the file
		// fill up the queue with words which have suffixes, who are
		// candidates to be compound words
		// insert each word in trie
		while (s.hasNext()) {
			word = s.next();		
			sufIndices = trie.getSuffixesStartIndices(word);
		
			for (int i : sufIndices) {
				if (i >= word.length())		// if index is out of bound
					break;					// it means suffixes of the word has
											// been added to the queue if there is any
				queue.add(new Pair<String>(word, word.substring(i)));
			}
	
			trie.insert(word);
		}
		
		Pair<String> p;				// a pair of word and its remaining suffix
		int maxLength = 0;			// longest compound word length
		//int sec_maxLength = 0;		// second longest compound word length		
		String longest = "";		// longest compound word
		String sec_longest = "";	// second longest compound word

		while (!queue.isEmpty()) {
			p = queue.removeFirst();
			word = p.second();
			
			sufIndices = trie.getSuffixesStartIndices(word);
			
			// if no suffixes found, which means no prefixes found
			// discard the pair and check the next pair
			if (sufIndices.isEmpty()) {
				continue;
			}
			
			//System.out.println(word);
			for (int i : sufIndices) {
				if (i > word.length()) { // sanity check 
					break;
				}
				
				if (i == word.length()) { // no suffix, means it is a compound word
					// check if the compound word is the longest
					// if it is update both longest and second longest
					// words records
					if (p.first().length() > maxLength) {
						//sec_maxLength = maxLength;
						sec_longest = longest;
						maxLength = p.first().length();
						longest = p.first();
					}
			
					compoundWords.add(p.first());	// the word is compound word
					
				} else {
					queue.add(new Pair<String>(p.first(), word.substring(i)));
				}
			}
		}
		
		// print out the results
		System.out.println("Longest Compound Word: " + longest);
		System.out.println("Second Longest Compound Word: " + sec_longest);
		System.out.println("Total Number of Compound Words: " + compoundWords.size());
	}

}
