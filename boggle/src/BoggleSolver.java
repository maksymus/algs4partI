import edu.princeton.cs.algs4.In;

import java.util.*;

class Trie {
    private class TrieNode {
        private Map<Character, TrieNode> leaves = new HashMap<Character, TrieNode>();
        private boolean terminal;
        public Character data;

        TrieNode(Character data, boolean terminal) {
            this.data = data;
            this.terminal = terminal;
        }

        public void addNode(char c, boolean terminal) {
            leaves.put(c, new TrieNode(c, terminal));
        }

        public boolean isTerminal() {
            return terminal;
        }

        public void setTerminal(boolean terminal) {
            this.terminal = terminal;
        }
    }

    public enum State { MISSING, CONTAINS, TERMINAL }

    private TrieNode root = new TrieNode((char) 0, false);

    public void insert(String key) {
        TrieNode current = root;

        for (char c : key.toCharArray()) {
            Map<Character, TrieNode> leaves = current.leaves;

            if (!leaves.containsKey(c)) {
                current.addNode(c, false);
            }

            current = leaves.get(c);
        }

        current.setTerminal(true);
    }

    public State contains(String key) {
        TrieNode current = root;

        for (char c : key.toCharArray()) {
            Map<Character, TrieNode> leaves = current.leaves;

            if (!leaves.containsKey(c)) {
                return State.MISSING;
            }

            current = leaves.get(c);
        }

        if (current.isTerminal())
            return State.TERMINAL;
        else
            return State.CONTAINS;
    }
}

public class BoggleSolver {

    private Trie dict = new Trie();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String word : dictionary) {
            dict.insert(word);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> words = new HashSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                boolean[][] visited = new boolean[board.rows()][board.cols()];
                dfs(board, i, j, visited, words, "");
            }
        }

        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (dict.contains(word) == Trie.State.TERMINAL) {
            if (word.length() < 3) {
                return 0;
            } else if (word.length() < 5) {
                return 1;
            } else if (word.length() < 6) {
                return 2;
            } else if (word.length() < 7) {
                return 3;
            } else if (word.length() < 8) {
                return 5;
            } else {
                return 11;
            }
        }
        return 0;
    }

    private void dfs(BoggleBoard board, int i, int j, boolean[][] visited, Set<String> words, String prefix) {
        if (i < 0 || j < 0 || i >= board.rows() || j >= board.cols() || visited[i][j])
            return;

        char letter = board.getLetter(i, j);

        String word = null;
        if (letter == 'Q') {
            word = prefix + "QU";
        } else {
            word = prefix + letter;
        }

        Trie.State state = dict.contains(word);

        if (state == Trie.State.MISSING)
            return;

        visited[i][j] = true;

        if (state == Trie.State.TERMINAL && word.length() > 2)
            words.add(word);

        dfs(board, i - 1, j, visited, words, word);
        dfs(board, i + 1, j, visited, words, word);
        dfs(board, i, j - 1, visited, words, word);
        dfs(board, i, j + 1, visited, words, word);
        dfs(board, i - 1, j - 1, visited, words, word);
        dfs(board, i - 1, j + 1, visited, words, word);
        dfs(board, i + 1, j - 1, visited, words, word);
        dfs(board, i + 1, j + 1, visited, words, word);

        visited[i][j] = false;
    }

    public static void main(String[] args) {
        In in3 = new In("/home/maksym/Projects/home/algs4partI/boggle/resources/dictionary-algs4.txt");
        List<String> commonDictionary = new ArrayList<String>();
        for (String s : in3.readAllStrings())
            commonDictionary.add(s);

        BoggleSolver solver = new BoggleSolver(commonDictionary.toArray(new String[commonDictionary.size()]));
        Iterable<String> allValidWords = solver.getAllValidWords(new BoggleBoard(
                "/home/maksym/Projects/home/algs4partI/boggle/resources/board-q.txt"));

        int score = 0, cnt = 0;
        for (String allValidWord : allValidWords) {
            cnt++;
            System.out.println(allValidWord);
            score += solver.scoreOf(allValidWord);
        }
        System.out.printf("Word count: %d\n", cnt);
        System.out.printf("Score: %d\n", score);
    }
}
