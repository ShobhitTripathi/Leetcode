/*
    Array
    arr = 2 3 5 7
    arr = 2,3,5,7,2,3,5,3,5,3

    2 2
    3 4
    5 3
    7 1

 */

/*
2d matrix 5 * 3

a b c
d e f
g h i
j k l
m n o

 [1 5 3 4 3]

 user
 [2 3 4 5]

 d e f
 g h i


[
[d e f]
[g h i]
[j k l]
[m n o]
]

d g j m


0 1 2
0 1 2
0 1 2
0 1 2

size = 3
0 to 3
get 0 , get 0 get 0
 */

import java.util.*;

public class Round2 {

    public static void main(String[] args) {
        char[][] arr = {{'a', 'b', 'c'},
                        {'d', 'e', 'f'},
                        {'g', 'h', 'i'},
                        {'j', 'k', 'l'},
                        {'m', 'n', 'o'}};

        int[] input = {1 ,2};

        printAllPermuations(arr, input);
    }

    private static void printAllPermuations(char[][] arr, int[] input) {
        StringBuilder sb = new StringBuilder();
        for (int n : input) {
            for (int i = 0;i < 3;i++) {
                sb.append(arr[n][i]);
            }
        }
        String str = sb.toString();
        printPermutation(str, "");
    }

    private static void printPermutation(String str, String s) {
        if (str.length() == 0) {
            return;
        }

        for (int i = 0;i < str.length();i++) {
            char ch = str.charAt(i);
            String add = str.substring(0, i) + str.substring(i + 1);
            if (!(add.length() < 2))
                System.out.print(add + "    ");
            printPermutation(add, s + ch);
        }
    }
}
