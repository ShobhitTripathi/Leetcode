package ServiceNow;

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

public class Round1 {
    private static void sortColors(int[] arr) {
        int i = 0;
        int j = 0;
        int k = arr.length - 1;

        while (j <= k) {
            if  (arr[j] < 1) {
                swap(arr, i, j);
                i++;
                j++;
            } else if (arr[j] > 1) {
                swap(arr, k, j);
                k--;
            } else {
                j++;
            }

        }
    }

    private static void swap(int[] arr, int curr, int right) {
        int temp = arr[curr];
        arr[curr] = arr[right];
        arr[right] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {2, 2, 1, 1, 1, 2, 0, 1, 0, 0};
        for (int i : arr) {
            System.out.print(" " + i);
        }
        sortColors(arr);
        System.out.println();
        System.out.println("After sort");
        for (int i : arr) {
            System.out.print(" " + i);
        }
    }
}

