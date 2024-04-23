public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        BinaryTrie trie = (BinaryTrie) or.readObject();
        int size = (int) or.readObject();
        BitSequence bs = (BitSequence) or.readObject();
        char[] chars = new char[size];

        for (int i = 0; i < size; i++) {
            Match ch = trie.longestPrefixMatch(bs);
            chars[i] = ch.getSymbol();
            bs = bs.allButFirstNBits(ch.getSequence().length());
        }

        FileUtils.writeCharArray(args[1], chars);
    }
}
