# 253. Meeting Rooms II

Given an array of meeting time intervals intervals where intervals[i] = [starti, endi], return the minimum number of conference rooms required.

Example 1:
```
Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2
```

Example 2:
```
Input: intervals = [[7,10],[2,4]]
Output: 1
```
Constraints:
```
1 <= intervals.length <= 104
0 <= starti < endi <= 106
```

Approach
```
Maintain a min-heap of ongoing meetings’ end times and reuse rooms whenever the earliest one frees up.
```


Solution
```java

class Solution {
    // O(N log N) time because of sorting + heap operations
    public int minMeetingRooms(int[][] intervals) {

        // Check for the base case. If there are no intervals, return 0
        if (intervals.length == 0) {
            return 0;
        }

        // Sort the intervals by start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        // Min heap to track the earliest end time of all allocated rooms
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // Add the first meeting's end time
        minHeap.offer(intervals[0][1]);

        // Iterate over remaining intervals
        for (int i = 1; i < intervals.length; i++) {

            // If the room due to free up the earliest is free, assign that room to this meeting.
            if (intervals[i][0] >= minHeap.peek()) {
                minHeap.poll();
            }

            // If a new room is to be assigned, then also we add to the heap,
            // If an old room is allocated, then also we add to the heap with updated end time.
            minHeap.offer(intervals[i][1]);
        }

        // The size of the heap tells us the minimum rooms required for all the meetings.
        return minHeap.size();
    }
}


```
