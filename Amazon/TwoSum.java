package Amazon;

import java.util.HashMap;
import java.util.Map;

/*
Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.



Example 1:

Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Output: Because nums[0] + nums[1] == 9, we return [0, 1].
Example 2:

Input: nums = [3,2,4], target = 6
Output: [1,2]
 */
public class TwoSum {

    private static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] res = new int[2];

        for (int index = 0;index < nums.length;index++) {
            int temp = target - nums[index];
            if (!map.containsKey(temp)) {
                map.put(nums[index], index);
            } else {
                res[0] = map.get(temp);
                res[1] = index;
                break;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {2,7,11,15};
        for (int n : arr)
            System.out.print(" " + n);
        System.out.println();
        int target = 9;
        int[] res = twoSum(arr, target);
        System.out.println("Indices for values adding up to " +  target + ": " +  res[0] + ", "+  res[1]);
    }

}
