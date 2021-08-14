package ArraysAndString;

import java.util.Arrays;

public class LongestCommonPrefix {

    public static String longestCommonPrefixCompute (String[] arr) {
        Arrays.sort(arr);

        String first = arr[0];
        String last = arr[arr.length - 1];

        int index = 0;
        while (index < arr[0].length()) {
            if (first.charAt(index) == last.charAt(index)) {
                index++;
            } else {
                break;
            }
        }

        return index == 0 ? "" : first.substring(0, index);
    }

    /*---------Driver Code--------------*/
    public static void main(String[] args) {
        String[] arr = {"flower", "flow","flight"};
        String ans = longestCommonPrefixCompute(arr);
        System.out.println(ans);
        //o/p : "fl"
    }
}
