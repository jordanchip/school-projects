package spell;

public class Trie implements ITrie{

	Node root;
	int nodeCount;
	int wordCount;
	
	public Trie() {
		root = new Node();
		nodeCount = 1;
		wordCount = 0;
	}
	
	
	@Override
	public void add(String word) {
		addRecursively(word, 0, root);
	}
	
	public Node addRecursively(String word, int pos, Node current) {
		if (pos == word.length()) {
			if (current.count == 0)
				wordCount++;
			current.count++;
			return current;
		}
		int index = word.charAt(pos)-'a';
		if (index < 0)
			index = 25;
		if (current.accessNode(index) == null)
		{
			current.addNode(index);
			nodeCount++;
		}
		return addRecursively(word, pos+1, current.accessNode(index));
	}

	@Override
	public INode find(String word) {
		return findRecursively(word, 0, root);
	}
	
	public INode findRecursively(String word, int pos, Node current) {
		if (pos == word.length()) {
			if (current.count > 0)
				return current;
			return null;
		}
		int index = word.charAt(pos) - 'a';
		if (index < 0)
			index = 25;
		if (current.accessNode(index) == null) {
			return null;
		}
		return findRecursively(word, pos + 1, current.accessNode(index));
	}

	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}
	
	@Override
	public String toString() {
		StringBuilder total = new StringBuilder();
		StringBuilder word = new StringBuilder();
		return toStringRecursive(total, word, root).toString();
	}
	
	public StringBuilder toStringRecursive(StringBuilder total, StringBuilder word, Node current) {
		if (current.count > 0)
			total.append(word + "\n");
		for (int i = 0; i < 26; i++) {
			if (current.accessNode(i) != null) {
				word.append((char) (i + 'a'));
				toStringRecursive(total, word, current.accessNode(i));
			}
		}
		if (word.length() > 0)
			word.deleteCharAt(word.length()-1);
		return total;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeCount;
		result = prime * result + wordCount;
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
		Trie other = (Trie) obj;
		if (nodeCount != other.nodeCount)
			return false;
		if (wordCount != other.wordCount)
			return false;
		//Add tree check here!
		return checkEquality(root, other.root);
	}
	
	public boolean checkEquality(Node n1, Node n2) {
		for (int i = 0; i < 26; i++) {
			if (n1.accessNode(i) != null) {
				if (n2.accessNode(i) == null)
					return false;
				if (n1.count != n2.count)
					return false;
				if (!checkEquality(n1.accessNode(i), n2.accessNode(i)))
					return false;
			}
			else if (n2.accessNode(i) != null)
				return false;
		}
		return true;
	}


	public class Node implements ITrie.INode {

		int count;
		Node[] nodes;
		
		public Node() {
			nodes = new Node[26];
			count = 0;
		}
		
		public void addNode(int pos) {
			nodes[pos] = new Node();
		}
		
		public Node accessNode(int pos) {
			return nodes[pos];
		}
		@Override
		public int getValue() {
			// TODO Auto-generated method stub
			return count;
		}
		
	}

}
