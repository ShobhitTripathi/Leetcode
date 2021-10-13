package Amazon;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class PartitionLabels {

    //traverse the string record the last index of each char.
    //using pointer to record end of the current sub string.
    public static List<Integer> partitionLabel(String S) {
        int[] chars = new int[26];
        List<Integer> result = new ArrayList<>();
        if(S == null || S.length() == 0){
            return result;
        }

        for (int i = 0;i < S.length();i++) {
            chars[S.charAt(i) - 'a'] = i;
        }

        // record the end index of the current sub string
        int start = 0;
        int end = 0;
        for (int i = 0;i < S.length();i++) {
            end = Math.max(end, chars[S.charAt(i) - 'a']);
            if (end == i) {
                result.add(end - start + 1);
                start = end +  1;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String s = "ababcbacadefegdehijhklij";
        List<Integer> res = new ArrayList<>();
        res = partitionLabel(s);
        System.out.println(res.toString());
    }

    //Imagine a bus moving forward, and imagine each char as a person yelling "I need to go that far!".
    // If a newcomer yelled a further position, we extend our expected ending position to that position.
    // Eventually, if we reached a position that satisfied everybody in the bus at the moment,
    // we partition and clear the bus.
}
