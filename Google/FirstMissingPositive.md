Question
```
41. First Missing Positive

Given an unsorted integer array nums. Return the smallest positive integer that is not present in nums.

You must implement an algorithm that runs in O(n) time and uses O(1) auxiliary space.

Example 1:
Input: nums = [1,2,0]
Output: 3
Explanation: The numbers in the range [1,2] are all in the array.

Example 2:
Input: nums = [3,4,-1,1]
Output: 2
Explanation: 1 is in the array but 2 is missing.

Example 3:
Input: nums = [7,8,9,11,12]
Output: 1
Explanation: The smallest positive integer 1 is missing.
 

Constraints:
1 <= nums.length <= 105
-231 <= nums[i] <= 231 - 1

```

Approach 1: Boolean Array (extra space)
```
Idea: Use an extra array seen[n+1] to mark which numbers 1..n appear.

Steps:
  Mark seen[num] = true if 1 <= num <= n.
  Scan from 1..n → return the first i where seen[i] == false.
  If all exist, return n+1.

Complexity:
  Time: O(n)
  Space: O(n) (not meeting constant space requirement)
```
```java
class Solution {

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        boolean[] seen = new boolean[n + 1]; // Array for lookup

        // Mark the elements from nums in the lookup array
        for (int num : nums) {
            if (num > 0 && num <= n) {
                seen[num] = true;
            }
        }

        // Iterate through integers 1 to n
        // return smallest missing positive integer
        for (int i = 1; i <= n; i++) {
            if (!seen[i]) {
                return i;
            }
        }

        // If seen contains all elements 1 to n
        // the smallest missing positive number is n + 1
        return n + 1;
    }
}
```


Approach 2: Index as Hash Key (In-place Marking)
```
Idea: Use the array itself as a hash. Replace invalid values, then use index sign marking.

Steps:
  Replace all <=0 or >n with 1. Track if 1 exists; if not, return 1.
  For each value val, mark presence by making nums[val] negative (handle duplicates).
  Special case: use nums[0] to track presence of n.
  Scan from 1..n-1: the first positive index i → missing = i.
  If all marked, check nums[0] → missing = n.
  Otherwise return n+1.

Complexity:
  Time: O(n)
  Space: O(1) 
```

```java
class Solution {

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        boolean contains1 = false;

        // Replace negative numbers, zeros,
        // and numbers larger than n with 1s.
        // After this nums contains only positive numbers.
        for (int i = 0; i < n; i++) {
            // Check whether 1 is in the original array
            if (nums[i] == 1) {
                contains1 = true;
            }
            if (nums[i] <= 0 || nums[i] > n) {
                nums[i] = 1;
            }
        }

        if (!contains1) return 1;

        // Mark whether integers 1 to n are in nums
        // Use index as a hash key and negative sign as a presence detector.
        for (int i = 0; i < n; i++) {
            int value = Math.abs(nums[i]);
            if (value == n) {
                nums[0] = -Math.abs(nums[0]);
            } else {
                nums[value] = -Math.abs(nums[value]);
            }
        }

        // First positive in nums is smallest missing positive integer
        for (int i = 1; i < n; i++) {
            if (nums[i] > 0) return i;
        }

        // nums[0] stores whether n is in nums
        if (nums[0] > 0) {
            return n;
        }

        // If nums contains all elements 1 to n
        // the smallest missing positive number is n + 1
        return n + 1;
    }
}
```

Approach 3: Cycle Sort (Index Placement)
```
Idea: Put each number in its “correct” index (x → nums[x-1]) via swaps.

Steps:
  For each i, while 1 <= nums[i] <= n and nums[i] != nums[nums[i]-1], swap into place.
  After rearrangement, scan: first i where nums[i] != i+1 → return i+1.
  If all in place, return n+1.

Complexity:
  Time: O(n)
  Space: O(1) 
```

```java
class Solution {

    public int firstMissingPositive(int[] nums) {
        int n = nums.length;

        // Use cycle sort to place positive elements smaller than n
        // at the correct index
        int i = 0;
        while (i < n) {
            int correctIdx = nums[i] - 1;
            if (nums[i] > 0 && nums[i] <= n && nums[i] != nums[correctIdx]) {
                swap(nums, i, correctIdx);
            } else {
                i++;
            }
        }

        // Iterate through nums
        // return smallest missing positive integer
        for (i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        // If all elements are at the correct index
        // the smallest missing positive number is n + 1
        return n + 1;
    }

    // Swaps two elements in nums
    private void swap(int[] nums, int index1, int index2) {
        int temp = nums[index1];
        nums[index1] = nums[index2];
        nums[index2] = temp;
    }
}
```


Notes 
```
Best interview choice: Approach 3 (Cycle Sort) — clean, in-place, and straightforward.
Approach 2 is also valid (sign marking).
```
