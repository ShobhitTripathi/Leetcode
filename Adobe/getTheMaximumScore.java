/*
1537. Get the Maximum Score
Hard

You are given two sorted arrays of distinct integers nums1 and nums2.

A valid path is defined as follows:

Choose array nums1 or nums2 to traverse (from index-0).
Traverse the current array from left to right.
If you are reading any value that is present in nums1 and nums2 you are allowed to change your path to the other array. (Only one repeated value is considered in the valid path).
The score is defined as the sum of uniques values in a valid path.

Return the maximum score you can obtain of all possible valid paths. Since the answer may be too large, return it modulo 109 + 7.
*/
class Solution {
    public int maxSum(int[] nums1, int[] nums2) {
        long sum1 = 0, sum2 = 0, max = 0;
        int i = 0, j = 0, n = nums1.length, m = nums2.length;
        while (i < n && j < m) {
            if (nums1[i] == nums2[j]) {
                max += Math.max(sum1, sum2) + nums1[i];
                sum1 = 0;
                sum2 = 0;
                i++;
                j++;
            } else if (nums1[i] < nums2[j]) {
                sum1 += nums1[i++];
            } else {
                sum2 += nums2[j++];
            }
        }

        while(i < n){
            sum1 += nums1[i++];
        }
        while(j < m){
            sum2 += nums2[j++];
        }
        max += Math.max(sum1, sum2);
        // since we have to return 32-bit integer so take modulo and return it.
        return (int)(max % 1000000007);
    }
}

/*
Solution: To avoid this, the maximum value than it can reach is 10^12. Taking long long int will solve the problem. And at the end, since we have to return 32-bit integer so take modulo and return it.
Rest, i think it's simple to understand how dp is working.
1 Taking maximum of the sums at each intersection point and at the end will give the answer.
2 To get that, take two pointers eah for an array and start traversing.
3 Check sum and take maximum at every intersection point or at the end and add it to the answer.
This will require 1-Pass of both the arrays.
Code will clear any remaining doubts.
*/
