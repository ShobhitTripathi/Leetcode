# 4. Median of Two Sorted Arrays

Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.

The overall run time complexity should be O(log (m+n)).

Example 1:
```
Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000
Explanation: merged array = [1,2,3] and median is 2.
```
Example 2:
```
Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000
Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
```
 

Constraints:
```
nums1.length == m
nums2.length == n
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-106 <= nums1[i], nums2[i] <= 106
```

Approach
```
Key Intuition-:
Instead of merging arrays (which costs linear time),
you find a partition that divides both arrays into two halves where:
Left half contains smaller elements,
Right half contains larger elements.
Once such a partition is found, the median can be derived directly from the border elements.


Complexity Analysis-:
Time	O(log(min(m, n)))	Binary search is performed on the smaller array only
Space	O(1)	Only constant extra variables used

```

Solution
```java
class Solution {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // Always binary search on the smaller array
        // This guarantees O(log(min(m, n))) time
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;

        // Binary search boundaries on nums1
        int left = 0;
        int right = m;

        while (left <= right) {

            // Partition index for nums1
            int partitionA = (left + right) / 2;

            // Partition index for nums2 such that
            // left partitions contain half of total elements
            int partitionB = (m + n + 1) / 2 - partitionA;

            // Handle edge cases using sentinel values
            // If partition is at start, there is nothing on left side
            int maxLeftA = (partitionA == 0)
                    ? Integer.MIN_VALUE
                    : nums1[partitionA - 1];

            // If partition is at end, there is nothing on right side
            int minRightA = (partitionA == m)
                    ? Integer.MAX_VALUE
                    : nums1[partitionA];

            int maxLeftB = (partitionB == 0)
                    ? Integer.MIN_VALUE
                    : nums2[partitionB - 1];

            int minRightB = (partitionB == n)
                    ? Integer.MAX_VALUE
                    : nums2[partitionB];

            // Check if we have found the correct partition
            // All left elements <= all right elements
            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {

                // If total number of elements is even
                if ((m + n) % 2 == 0) {
                    return (Math.max(maxLeftA, maxLeftB)
                            + Math.min(minRightA, minRightB)) / 2.0;
                }
                // If total number of elements is odd
                else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            }

            // If maxLeftA is too big, move partitionA left
            else if (maxLeftA > minRightB) {
                right = partitionA - 1;
            }

            // Else move partitionA right
            else {
                left = partitionA + 1;
            }
        }

        // This line is never reached for valid input
        return 0.0;
    }
}

```
