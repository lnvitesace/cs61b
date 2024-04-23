import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            frequencyTable.put(c, frequencyTable.getOrDefault(c, 0) + 1);
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] chars = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(chars);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(chars.length);
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> bList = new ArrayList<>();

        for (char c : chars) {
            BitSequence bits = lookupTable.get(c);
            bList.add(bits);
        }
        BitSequence finalSequence = BitSequence.assemble(bList);
        ow.writeObject(finalSequence);
    }
}
