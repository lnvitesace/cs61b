import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTrie implements Serializable {
    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        @Override
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }

    private final Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (char c : frequencyTable.keySet()) {
            pq.add(new Node(c, frequencyTable.get(c), null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.remove();
            Node right = pq.remove();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        root = pq.remove();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        Node pointer = root;
        for (int i = 0; i < querySequence.length(); i++) {
            if (querySequence.bitAt(i) == 0) {
                pointer = pointer.left;
            } else {
                pointer = pointer.right;
            }
            if (pointer.isLeaf()) {
                return new Match(querySequence.firstNBits(i + 1), pointer.ch);
            }
        }
        throw new IllegalArgumentException("No match found");
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildHelper(root, lookupTable, new BitSequence());
        return lookupTable;
    }

    private void buildHelper(Node n, Map<Character, BitSequence> table, BitSequence prev) {
        if (n.isLeaf()) {
            table.putIfAbsent(n.ch, prev);
            return;
        }

        buildHelper(n.left, table, prev.appended(0));
        buildHelper(n.right, table, prev.appended(1));
    }
}
