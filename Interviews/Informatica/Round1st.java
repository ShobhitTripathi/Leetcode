package informatica;


public class Round1st {




    public static void main(String[] args) {
        int[] arr = {11, 5, 3, 6, 9, 11, 8, -2, -2, -2, 12, 5, 4, 2, 4, 0, 0};
        int target = 6;
        ContigousSubArrays(arr, target);
    }

    private static void ContigousSubArrays(int[] arr, int target) {
        int  j = 0, i = 0;

        while (j < arr.length) {
            int sum = getSum(arr, i, j);
            if (sum == target) {
                System.out.println();
                printArray(arr, i, j);
                j++;
            } else if (sum > target) {
                i++;
            } else {
                j++;
            }
        }
    }

    private static void printArray(int[] arr, int i, int j) {
        for (int k = i;k <= j;k++) {
            System.out.print(arr[k] + " ");
        }
    }

    private static int getSum(int[] arr, int i, int j) {
        int sum = arr[i];
        if (i == j) {
            return sum;
        }
        for (int k = i + 1;k <= j;k++) {
            sum += arr[k];
        }
        return sum;
    }
}
