package hw3.hash;


import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int N = oomages.size();
        HashMap<Integer, Integer> buckets = new HashMap<>();

        for (Oomage o : oomages) {
            int bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            if (!buckets.containsKey(bucketNum)) {
                buckets.put(bucketNum, 1);
                continue;
            }
            int oldNum = buckets.get(bucketNum);
            buckets.put(bucketNum, oldNum + 1);
        }

        Set<Integer> keySet = buckets.keySet();
        for (int key : keySet) {
            int num = buckets.get(key);
            if (num <= N / 50 || num >= N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
