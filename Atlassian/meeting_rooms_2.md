Problem Statement
```
253. Meeting Rooms II

Given an array of meeting time intervals intervals where intervals[i] = [starti, endi], return the minimum number of conference rooms required.


Example 1:

Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2
Example 2:

Input: intervals = [[7,10],[2,4]]
Output: 1
```

Solution
```java
class Solution {
    // O (NlogN)
    public int minMeetingRooms(int[][] intervals) {
        // Sort the intervals by start time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        
        // Min heap
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        // Add the first meeting
        pq.offer(intervals[0][1]);
        
        // Iterate over remaining intervals
        for (int i = 1;i < intervals.length;i++) {

            // If the room due to free up the earliest is free, assign that room to this meeting.
            if (intervals[i][0] >= pq.peek()) {
                pq.poll();
            }
            
            // If a new room is to be assigned, then also we add to the heap,
            // If an old room is allocated, then also we have to add to the heap with updated end time.
            pq.offer(intervals[i][1]);
        }
        
        // The size of the heap tells us the minimum rooms required for all the meetings.
        return pq.size();
    }
}
```
