package StackAndQueue;

public class Quolom {
    private static int VAR = 1;
    private int x = 1;

    public static int findMaxProfit (int[] arr) {
        int curr_sum = 0;
        int max_so_far = 0;
        int[] diff = new int[arr.length - 1];
        for (int i = 0;i < arr.length - 1;i++) {
            diff[i] = arr[i + 1] - arr[i];
        }

        for (int i = 0;i < diff.length;i++) {
            curr_sum += diff[i];
            if (curr_sum > max_so_far)
                max_so_far = curr_sum;
            if (curr_sum < 0)
                curr_sum = 0;
        }
        return max_so_far;
    }

    public void printVariable () {
        System.out.println("var: " + (++VAR));
        System.out.println("x: " + (++x));
    }

    public static void main(String[] args) {
//        System.out.println("Enter the length of the array: ");
//        int[] arr = {8, 2, 5, 10, 7, 6, 1, 10};
//        int res = findMaxProfit(arr);
//        System.out.println(res);
         Quolom obj1 = new Quolom();
         Quolom obj2 = new Quolom();

        obj1.printVariable();
        obj2.printVariable ();
    }
}
