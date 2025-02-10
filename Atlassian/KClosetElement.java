/*
Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result should also be sorted in ascending order.

An integer a is closer to x than an integer b if:

|a - x| < |b - x|, or
|a - x| == |b - x| and a < b
 

Example 1:

Input: arr = [1,2,3,4,5], k = 4, x = 3

Output: [1,2,3,4]

Example 2:

Input: arr = [1,1,2,3,4,5], k = 4, x = -1


*/

import java.util.*;

public class KClosestElements {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        int left = 0;
        int right = arr.length - k;
        
        // Binary search to find the starting point of the k closest elements
        while (left < right) {
            int mid = left + (right - left) / 2;
            
            // Compare the distances to decide whether to move left or right
            if (x - arr[mid] > arr[mid + k] - x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        // Create a list of the k closest elements starting from index 'left'
        List<Integer> result = new ArrayList<>();
        for (int i = left; i < left + k; i++) {
            result.add(arr[i]);
        }
        
        return result;
    }

    public static void main(String[] args) {
        KClosestElements obj = new KClosestElements();
        
        // Example 1
        int[] arr1 = {1, 2, 3, 4, 5};
        int k1 = 4;
        int x1 = 3;
        System.out.println(obj.findClosestElements(arr1, k1, x1)); // Output: [1, 2, 3, 4]
        
        // Example 2
        int[] arr2 = {1, 1, 2, 3, 4, 5};
        int k2 = 4;
        int x2 = -1;
        System.out.println(obj.findClosestElements(arr2, k2, x2)); // Output: [1, 1, 2, 3]
    }
}
