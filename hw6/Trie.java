import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Trie {
    private static class Node {
        boolean exists;
        private final Map<Character, Node> links;

        public Node() {
            links = new TreeMap<>();
            exists = false;
        }
    }

    private Node root;

    public void put(String key) {
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }

        if (d == key.length()) {
            x.exists = true;
            return x;
        }

        char c = key.charAt(d);
        x.links.put(c, put(x.links.get(c), key, d + 1));
        return x;
    }

    public boolean hasPrefix(String prefix) {
        Node t = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (!t.links.containsKey(prefix.charAt(i))) {
                return false;
            }
            t = t.links.get(prefix.charAt(i));
        }
        return true;
    }

    public boolean contains(String key) {
        Node t = root;
        for (int i = 0; i < key.length(); i++) {
            if (!t.links.containsKey(key.charAt(i))) {
                return false;
            }
            t = t.links.get(key.charAt(i));
        }
        return t.exists;
    }
}
