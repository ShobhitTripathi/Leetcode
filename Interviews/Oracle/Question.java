package oracle;

public class Question {

    public static final int INVALID_DATA = -1;
    public static int maxSumOfNonAdjacentIndices(int[] arr) {
        if (!isValid(arr)) {
            return INVALID_DATA;
        }
        int max = 0;
        int sumOfOdd = 0, sumOfEven = 0;
        for (int i = 0;i < arr.length;i++) {
            if (i % 2 == 0) {
                sumOfEven += arr[i];
            } else {
                sumOfOdd += arr[i];
            }
        }
        max = Math.max(sumOfEven, sumOfOdd);
        if (sumOfEven == sumOfOdd) {
            System.out.println("Both even and odd adjacent have same sum.");
        } else if (sumOfEven > sumOfOdd) {
            System.out.println("Even adjacent have the maximum sum.");
        } else {
            System.out.println("Odd adjacent have the maximum sum.");
        }

        return max;
    }

    public static boolean isValid(int[] arr) {
        if (arr.length <= 2) {
            System.out.println("The length of input should be more than 2");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 7};
        int res = maxSumOfNonAdjacentIndices(arr);
        if (res != INVALID_DATA)
            System.out.println("Maximum sum is : " + res);
    }
}
