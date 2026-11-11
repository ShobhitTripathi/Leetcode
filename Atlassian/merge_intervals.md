# [56. Merge Intervals](https://leetcode.com/problems/merge-intervals/)
Medium


Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

 

Example 1:
```
Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
```
Example 2:
```
Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 ```

Constraints:

- 1 <= intervals.length <= 10<sup>4</sup>
- intervals[i].length == 2
- 0 <= starti <= endi <= 10<sup>4</sup>

## Approach
```
- Sort the intervals by start time
- Use arr to store the merged list
- Start by inserting the first element
- looping through all the other intervals
  - check if last interval inserted into arr overlapped with current interval
  - if yes then create a new interval and push to arr
  - else insert the current interval to arr
```
## Solution
```java
class Solution {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparing(a -> a[0]));
        List<int[]> list = new ArrayList<>();

        for (int[] interval : intervals) {
            if (list.size() >= 1 && interval[0] <= list.getLast()[1]) {
                list.getLast()[1] = Math.max(interval[1], list.getLast()[1]);
            } else {
                list.add(interval);
            }
        }

        return list.toArray(new int[list.size()][]);
    }
}
```
## Complexity Analysis
```
- Time Complexity: O(nlogn)
- Space Complexity: O(n), for arr
```
