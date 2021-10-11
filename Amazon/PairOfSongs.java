package Amazon;

import java.util.HashMap;
import java.util.Map;

public class PairOfSongs {

    public static int getPairOfSons(int[] time) {
        int res = 0;
        Map<Integer, Integer> count = new HashMap<>();

        for (int t : time) {
            int theOther = (60 - (t % 60)) % 60;
            res += count.getOrDefault(theOther, 0);
            count.put(t % 60, count.getOrDefault(t % 60, 0) + 1);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {30,20,150,100,40};
        for (int n : arr)
            System.out.print(" " + n);
        System.out.println();
        int res = getPairOfSons(arr);
        System.out.println("Pairs of Songs With Total Durations Divisible by 60 " +  res);
    }
}
