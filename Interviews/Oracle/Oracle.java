package oracle;

public class Oracle {

    public static final int INVALID_DATA = -1;
    //sum logic
    public static int missingNumberSumAlgo(int[] arr, int x) {
        if (!isValid(arr, x)) {
            return INVALID_DATA;
        }
        return sum(x) - sum(arr);
    }

    //sum till x
    public static int sum(int x) {
        int sumTillX = 0;
        for(int i = 1;i <= x;i++) {
            sumTillX += i;
        }
        return sumTillX;
    }

    //sum of array values
    public static int sum(int[] arr) {
        int sumOfArr = 0;
        for (int val : arr) {
            sumOfArr += val;
        }
        return sumOfArr;
    }

    public static boolean isValid (int[] arr, int x) {
        if (arr.length >= x) {
            System.out.println("Invalid Packet length");
            return false;
        }
        for (int val : arr) {
            if (val > x) {
                System.out.println("Invalid array input");
                return false;
            }
            if (val <= 0) {
                System.out.println("Invalid array input: values should lie from 1 to " + x);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int [] arr = {2, 1};
        int x = 3;
        int res = missingNumberSumAlgo(arr, x);
        if (res != INVALID_DATA)
            System.out.println("Missing number in the array is : " + res);
    }
}
