import java.util.PriorityQueue;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedList;

public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static PriorityQueue<SearchString> strings;
    private static Trie dictionary;
    private static char[][] board;

    public static class SearchString implements Comparable<SearchString> {
        private final String string;
        Set<Position> marked;

        public SearchString(String string, Set<Position> marked) {
            this.string = string;
            this.marked = marked;
        }

        @Override
        public String toString() {
            return string;
        }

        @Override
        public int compareTo(SearchString that) {
            if (this.string.length() > that.string.length()) {
                return -1;
            } else if (this.string.length() < that.string.length()) {
                return 1;
            } else {
                return this.string.compareTo(that.string);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SearchString that = (SearchString) o;
            return string.equals(that.string);
        }

        @Override
        public int hashCode() {
            return string.hashCode();
        }
    }

    public static class Position {
        private final int i;
        private final int j;

        public Position(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || getClass() != o.getClass()) {
                return false;
            }
            return i == ((Position) o).i && j == ((Position) o).j;
        }

        @Override
        public int hashCode() {
            return i * board.length + j;
        }
    }

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        validateArgument(k, boardFilePath);

        In boardIn = new In(boardFilePath);
        In dictIn = new In(dictPath);
        String[] dictArray = dictIn.readAllLines();

        dictionary = getDictionary(dictArray);
        board = makeBoard(boardIn.readAllLines());
        strings = new PriorityQueue<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                addValidString(i, j);
            }
        }

        return getResult(k, strings);
    }

    private static Trie getDictionary(String[] dictArray) {
        Trie dict = new Trie();
        for (String s : dictArray) {
            dict.put(s);
        }
        return dict;
    }

    private static char[][] makeBoard(String[] boardString) {
        int height = boardString.length;
        int width = boardString[0].length();
        char[][] b = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                b[i][j] = boardString[i].charAt(j);
            }
        }
        return b;
    }

    private static void addValidString(int i, int j) {
        addHelper(i, j, new SearchString("", new HashSet<>()));
    }

    private static void addHelper(int i, int j, SearchString prevNode) {
        String prev = prevNode.string;
        String curr = prev + board[i][j];

        if (!dictionary.hasPrefix(curr)) {
            return;
        }

        Set<Position> newMarked = new HashSet<>(prevNode.marked);
        newMarked.add(new Position(i, j));
        SearchString currNode = new SearchString(curr, newMarked);

        if (curr.length() >= 3 && dictionary.contains(curr) && !strings.contains(currNode)) {
            strings.add(currNode);
        }

        Set<Position> surroundings = getSurrounding(i, j);
        for (Position p : surroundings) {
            if (!currNode.marked.contains(p)) {
                addHelper(p.i, p.j, currNode);
            }
        }
    }

    private static Set<Position> getSurrounding(int i, int j) {
        Set<Position> surrounding = new HashSet<>();
        addSurrounding(i - 1, j - 1, surrounding);
        addSurrounding(i - 1, j, surrounding);
        addSurrounding(i - 1, j + 1, surrounding);
        addSurrounding(i, j - 1, surrounding);
        addSurrounding(i, j + 1, surrounding);
        addSurrounding(i + 1, j - 1, surrounding);
        addSurrounding(i + 1, j, surrounding);
        addSurrounding(i + 1, j + 1, surrounding);
        return surrounding;
    }

    private static void addSurrounding(int i, int j, Set<Position> surrounding) {
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length) {
            return;
        }
        surrounding.add(new Position(i, j));
    }

    private static List<String> getResult(int k, PriorityQueue<SearchString> pq) {
        List<String> result = new LinkedList<>();
        for (int i = 0; i < k; i++) {
            SearchString string = pq.poll();
            if (string == null) {
                return result;
            }
            result.add(string.toString());
        }
        return result;
    }

    private static void validateArgument(int k, String boardFilePath) {
        In b = new In(boardFilePath);
        String[] boardArray = b.readAllLines();
        int length = boardArray[0].length();
        for (String s : boardArray) {
            if (s.length() != length) {
                throw new IllegalArgumentException("board is not a rectangular");
            }
        }

        In dict = new In(dictPath);
        if (!dict.exists()) {
            throw new IllegalArgumentException("dict file not found");
        }

        if (k <= 0) {
            throw new IllegalArgumentException("k must be greater than 0");
        }
    }
}
