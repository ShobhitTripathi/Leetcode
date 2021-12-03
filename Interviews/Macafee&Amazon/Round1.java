/*
    N number of functions
    call these functions based on the some input

    eg 8 functions
    3 switches
    000
    001
    010
    011
    100
    101
    110
    111

    0000

 */

/*
Problem statement:

array elements: 0 1 2
   i/p: 0 2 2 1 2 2 0 1 2 0
sort the array in one pass (without using predefined functions)
   o/p: 0 0 0 1 1 2 2 2 2 2
*/

import java.util.HashMap;
import java.util.Map;

public class Round1 {
    public static void main(String[] args) {
        int[] arr = {6, 4, 12, 10, 22, 54, 32, 42, 21, 11};
        int target = 16;
//        printValuesSum(arr, target);
//        printAllPairsSum(arr);
        int res = findMax(arr);
        System.out.println("Max values in arr is : " + res);
    }

    private static int findMax(int[] arr) {
        int max = Integer.MIN_VALUE;

        for (int n : arr) {
            max = Math.max(n, max);
        }
        return max;
    }

    private static void printAllPairsSum(int[] arr) {

        for (int i = 0; i < arr.length;i++) {
            for (int j = i + 1; j < arr.length; j++) {
                System.out.print("(" + arr[i] + ", " + arr[j] + ") = " + (arr[i] + arr[j]) +  ",");
            }
            System.out.println();
        }
    }




    private static void printValuesSum(int[] arr, int target) {
        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < arr.length;i++) {
            int key = target - arr[i];
            if (map.containsKey(key)) {
                System.out.print("(" + arr[i] + ", " + key + ") ");
            } else {
                map.put(arr[i], key);
            }
            System.out.println();
        }
    }
}
