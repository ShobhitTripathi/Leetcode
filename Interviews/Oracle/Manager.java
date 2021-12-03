import java.util.*;


public class Manager {
//o/p: 0 1 1 2 3 5

    public static List<Integer> printFibonacci(int n) {
        int a = 0;
        int b = 1;
        List<Integer> res = new ArrayList<>();
        res.add(a);
        res.add(b);
        int i = 2;

        int c;
        while (i < n) {
            c = a + b;
            res.add(c);
            a = b;
            b = c;
            i++;
        }
        return res;
    }

    public static int[] generateRandomArray() {
        int[] arr = new int[10];
        Random r = new Random();
        for (int i = 0;i < 10;i++) {
            arr[i] = (int)Math.random();
        }
        return arr;
    }

    public static void main(String[] args) {
//        int val = 10;
//        List<Integer> res = new ArrayList<>();
//        res = printFibonacci(val);
//        for (int n : res) {
//            System.out.print(" " + n);
//        }

        int[] arr = {10, 2 ,5 , 7, 11, 3, 1, 8, 9, 6};
        System.out.println("Input arr: ");
        for (int n : arr) {
            System.out.print(" " + n);
        }
        System.out.println();

        mergeSort(arr, 0, arr.length -1);

        System.out.println("o/p array:");
        for (int n : arr) {
            System.out.print(" " + n);
        }
    }

    static void merge(int arr[], int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            }
            else {
                arr[k] = R[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    static void mergeSort(int arr[], int l, int r)
    {
        if (l < r) {
            // Find the middle point
            int m =l+ (r-l)/2;

            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);

            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}