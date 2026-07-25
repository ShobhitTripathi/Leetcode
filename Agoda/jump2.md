# [45. Jump Game II](https://leetcode.com/problems/jump-game-ii/)
Medium


Given an array of non-negative integers nums, you are initially positioned at the first index of the array.

Each element in the array represents your maximum jump length at that position.

Your goal is to reach the last index in the minimum number of jumps.

You can assume that you can always reach the last index.

 

Example 1:
```
Input: nums = [2,3,1,1,4]
Output: 2
Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.
```
Example 2:
```
Input: nums = [2,3,0,1,4]
Output: 2
 ```

Constraints:
```
- 1 <= nums.length <= 104
- 0 <= nums[i] <= 1000
```

Intution
```
Think of the array as levels of a BFS.
Each index represents a point you can stand on, and nums[i] tells how far you can jump from that point.

You expand the current jump window [l, r] — all positions reachable with the current number of jumps.
While scanning this window, you compute the farthest index you can reach from any of these positions.

Once you finish scanning [l, r], you must take a jump (because the current window ends).
Then you shift to the next window [r+1, farthest], representing all points reachable in the next jump.

The number of times you shift windows = minimum jumps needed.
```

## Approach
```
Use a BFS-like greedy window:
expand the current jump range to the farthest reachable index;
when the range ends, make a jump and shift to the next range.

Time Complexity:
O(n) — each index is processed only once.

Space Complexity:
O(1) — constant extra space.
```
### TODO : Optimization

## Solution
```java
public class Solution {
    public int jump(int[] nums) {
        int res = 0, l = 0, r = 0;   // res = jumps, [l, r] = current jump range

        // Continue until the right boundary reaches or crosses the last index
        while (r < nums.length - 1) {

            int farthest = 0;        // farthest index reachable in the next jump

            // Check all positions in the current range [l, r]
            for (int i = l; i <= r; i++) {
                farthest = Math.max(farthest, i + nums[i]);
            }

            // Move the window to the next jump range
            l = r + 1;
            r = farthest;
            res++;                   // count one jump
        }
        return res;
    }
}

```

## Complexity Analysis
```
- Time Complexity: O(k*N)
- Space Complexity: O(N)
```
