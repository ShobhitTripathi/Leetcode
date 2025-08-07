# 169. Majority Element
Easy

Given an array nums of size n, return the majority element.

The majority element is the element that appears more than ⌊n / 2⌋ times. You may assume that the majority element always exists in the array.

Example 1:
Input: nums = [3,2,3]
Output: 3

Example 2:
Input: nums = [2,2,1,1,1,2,2]
Output: 2

```approach
Approach: Boyer-Moore Voting Algorithm
Goal:
Find the element that appears more than ⌊n/2⌋ times in the array (i.e., the majority element).

How it works:
We vote for elements. If an element is the same as our current candidate, it gets +1 vote.
If it's different, it gets -1 (like cancelling the vote).
When votes reach zero, we change the candidate.
Because the majority element appears more than n/2 times, it will survive all the vote cancellations.

Why it works:
Think of it as cancelling pairs of different elements.
Only the majority element can remain after all these cancellations.

```


```java

// O(n) and O(1)
class Solution {
    public int majorityElement(int[] nums) {
        int count = 0;              // To track the count of the current candidate
        Integer candidate = null;   // Stores the potential majority element

        for (int num : nums) {
            if (count == 0) {
                candidate = num;    // Reset candidate when count is zero
            }
            // Increase count if same as candidate, else decrease
            count += (num == candidate) ? 1 : -1;
        }

        return candidate;           // The majority element (guaranteed to exist)
    }
}
```
