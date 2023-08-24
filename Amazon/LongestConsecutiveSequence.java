package Amazon;

import java.util.HashMap;
import java.util.Map;

public class LongestConsecutiveSequence {

    public static void main(String[] args) {
        int[] arr = {1, 100, 2, 400, 3, 4};
        int res = getLongestConsecutiveSequence(arr);
        System.out.println("LongestConsecutiveSequence is : " +  res);
    }


    /*
    We will use HashMap.
    The key thing is to keep track of the sequence length and store that in the boundary points of the sequence.
    For example, as a result, for sequence {1, 2, 3, 4, 5}, map.get(1) and map.get(5) should both return 5.

    Whenever a new element n is inserted into the map, do two things:

    See if n - 1 and n + 1 exist in the map, and if so, it means there is an existing sequence next to n.
    Variables left and right will be the length of those two sequences,
    while 0 means there is no sequence and n will be the boundary point later.
    Store (left + right + 1) as the associated value to key n into the map.

    Use left and right to locate the other end of the sequences to the left and right of n respectively,
    and replace the value with the new length.
    Everything inside the for loop is O(1) so the total time is O(n).
    */
    private static int getLongestConsecutiveSequence(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int max = 0;

        for (int num : nums) {
            if (map.containsKey(num)) {
                continue;
            }
            //1. find left and the right sum
            int left = map.getOrDefault(num - 1, 0);
            int right = map.getOrDefault(num + 1, 0);
            int sum = left + right + 1 ;
            max = Math.max(max, sum);

            // 2. Union by only updating boundary
            // Leave middle k-v dirty to avoid cascading update
            if (left > 0) map.put(num - left, sum);
            if (right > 0) map.put(num + right, sum);
            // Keep each number in Map to de-duplicate
            map.put(num, sum);
        }
        return max;
    }



    // using Hashset
        private static int largestSubsequenceCount(int[] arr) {
        Set<Integer> set = new HashSet<>();
        int max = 0;

        // add everything to the set
        for (int n : arr)
            set.add(n);

        for (int n : arr) {
            if (!set.contains(n - 1)) {
                int curr_max = 0;
                int temp = n;
                while (set.contains(temp)) {
                    curr_max++;
                    temp++;
                }
                max = Math.max(curr_max, max);
            }
        }
        return max;
    }
}
