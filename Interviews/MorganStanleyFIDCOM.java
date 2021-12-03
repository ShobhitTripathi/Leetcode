package StackAndQueue;

import java.util.ArrayList;
import java.util.List;

public class MorganStanleyFIDCOM {

    static int StrictlyIncreasing(List<Integer> list, int k) {
        int count = 0;
        boolean flag = false;
        for (int i = 0; i < list.size() - k; i++) {
            System.out.println("Starting point i:" + list.get(i));
            for (int j = i + 1; j < i + k; j++) {
                if (list.get(j - 1) < list.get(j)) {
                    flag = true;
                    System.out.println("1st:" + list.get(j - 1) + " 2nd: " + list.get(j));
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag == true) {

                count++;
            }
        }
        return count;
    }


    static int numOfIncSubseqOfSizeK(int arr[], int n, int k) {
        int dp[][] = new int[k][n], sum = 0;

        // count of increasing subsequences of size 1
        // ending at each arr[i]
        for (int i = 0; i < n; i++) {
            dp[0][i] = 1;
        }

        // building up the matrix dp[][]
        // Here 'l' signifies the size of
        // increassing subsequence of size (l+1).
        for (int l = 1; l < k; l++) {

            // for each increasing subsequence of size 'l'
            // ending with element arr[i]
            for (int i = l; i < n; i++) {

                // count of increasing subsequences of size 'l'
                // ending with element arr[i]
                dp[l][i] = 0;
                for (int j = l - 1; j < i; j++) {
                    if (arr[j] < arr[i]) {
                        dp[l][i] += dp[l - 1][j];
                    }
                }
            }
        }

        // sum up the count of increasing subsequences of
        // size 'k' ending at each element arr[i]
        for (int i = k - 1; i < n; i++) {
            sum += dp[k - 1][i];
        }

        // required number of increasing
        // subsequences of size k
        return sum;
    }


    public static void main (String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        int[] arr = {1,2,3,4,5,6};
        int k = 3;
        int result = numOfIncSubseqOfSizeK (arr,arr.length, k);
        System.out.println("count: " + result);

    }
}
