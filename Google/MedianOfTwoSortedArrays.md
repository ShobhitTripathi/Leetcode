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
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length, n = nums2.length;
        int left = 0, right = m;

        while (left <= right) {
            int partitionA = (left + right) / 2;
            int partitionB = (m + n + 1) / 2 - partitionA;

            int maxLeftA = (partitionA == 0)
                ? Integer.MIN_VALUE
                : nums1[partitionA - 1];
            int minRightA = (partitionA == m)
                ? Integer.MAX_VALUE
                : nums1[partitionA];
            int maxLeftB = (partitionB == 0)
                ? Integer.MIN_VALUE
                : nums2[partitionB - 1];
            int minRightB = (partitionB == n)
                ? Integer.MAX_VALUE
                : nums2[partitionB];

            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                if ((m + n) % 2 == 0) {
                    return (
                        (Math.max(maxLeftA, maxLeftB) +
                            Math.min(minRightA, minRightB)) /
                        2.0
                    );
                } else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            } else if (maxLeftA > minRightB) {
                right = partitionA - 1;
            } else {
                left = partitionA + 1;
            }
        }
        return 0.0;
    }
}
```
