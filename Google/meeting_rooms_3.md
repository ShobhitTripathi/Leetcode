# 2402. Meeting Rooms III

You are given an integer n. There are n rooms numbered from 0 to n - 1.

You are given a 2D integer array meetings where meetings[i] = [starti, endi] means that a meeting will be held during the half-closed time interval [starti, endi). 
All the values of starti are unique.

Meetings are allocated to rooms in the following manner:

Each meeting will take place in the unused room with the lowest number.
If there are no available rooms, the meeting will be delayed until a room becomes free.The delayed meeting should have the same duration as the original meeting.
When a room becomes unused, meetings that have an earlier original start time should be given the room.
Return the number of the room that held the most meetings. If there are multiple rooms, return the room with the lowest number.

A half-closed interval [a, b) is the interval between a and b including a and not including b.

Example 1:
```
Input: n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]
Output: 0
Explanation:
- At time 0, both rooms are not being used. The first meeting starts in room 0.
- At time 1, only room 1 is not being used. The second meeting starts in room 1.
- At time 2, both rooms are being used. The third meeting is delayed.
- At time 3, both rooms are being used. The fourth meeting is delayed.
- At time 5, the meeting in room 1 finishes. The third meeting starts in room 1 for the time period [5,10).
- At time 10, the meetings in both rooms finish. The fourth meeting starts in room 0 for the time period [10,11).
Both rooms 0 and 1 held 2 meetings, so we return 0. 
```
Example 2:
```
Input: n = 3, meetings = [[1,20],[2,10],[3,5],[4,9],[6,8]]
Output: 1
Explanation:
- At time 1, all three rooms are not being used. The first meeting starts in room 0.
- At time 2, rooms 1 and 2 are not being used. The second meeting starts in room 1.
- At time 3, only room 2 is not being used. The third meeting starts in room 2.
- At time 4, all three rooms are being used. The fourth meeting is delayed.
- At time 5, the meeting in room 2 finishes. The fourth meeting starts in room 2 for the time period [5,10).
- At time 6, all three rooms are being used. The fifth meeting is delayed.
- At time 10, the meetings in rooms 1 and 2 finish. The fifth meeting starts in room 1 for the time period [10,12).
Room 0 held 1 meeting while rooms 1 and 2 each held 2 meetings, so we return 1. 
 ```

Constraints:
```
1 <= n <= 100
1 <= meetings.length <= 105
meetings[i].length == 2
0 <= starti < endi <= 5 * 105
All the values of starti are unique.
```

Approach
```
Use two min-heaps: one for free rooms and one for occupied rooms by earliest end time; 
assign or delay meetings accordingly and count room usage.

```


Complexity Analysis
```
Time Complexity : O(M log N) 
M = number of meetings 
N = number of rooms
Each meeting involves heap operations.

Space Complexity: O(N)
```

Solution
```java

class Solution {
    public int mostBooked(int n, int[][] meetings) {

        // Count of meetings handled by each room
        int[] meetingCount = new int[n];

        // Min-heap of occupied rooms:
        // [endTime, roomIndex] → earliest finishing room first
        PriorityQueue<int[]> usedRooms =
                new PriorityQueue<>((a, b) ->
                        a[0] != b[0] ? Integer.compare(a[0], b[0])
                                     : Integer.compare(a[1], b[1]));

        // Min-heap of free room indices (smallest index first)
        PriorityQueue<Integer> unusedRooms = new PriorityQueue<>();

        // Initially all rooms are free
        for (int i = 0; i < n; i++) {
            unusedRooms.offer(i);
        }

        // Sort meetings by start time (and end time as tie-breaker)
        Arrays.sort(meetings, (a, b) ->
                a[0] != b[0] ? Integer.compare(a[0], b[0])
                             : Integer.compare(a[1], b[1]));

        // Process meetings one by one
        for (int[] meeting : meetings) {
            int start = meeting[0], end = meeting[1];

            // Free up rooms whose meetings have ended before current start
            while (!usedRooms.isEmpty() && usedRooms.peek()[0] <= start) {
                int room = usedRooms.poll()[1];
                unusedRooms.offer(room);
            }

            if (!unusedRooms.isEmpty()) {
                // Assign meeting to the smallest-index available room
                int room = unusedRooms.poll();
                usedRooms.offer(new int[]{end, room});
                meetingCount[room]++;
            } else {
                // No room available → delay meeting until earliest room frees up
                int roomAvailabilityTime = usedRooms.peek()[0];
                int room = usedRooms.poll()[1];

                // New end time = availability + meeting duration
                usedRooms.offer(new int[]{roomAvailabilityTime + (end - start), room});
                meetingCount[room]++;
            }
        }

        // Find the room with the maximum number of meetings
        int maxMeetingCount = 0, maxMeetingCountRoom = 0;
        for (int i = 0; i < n; i++) {
            if (meetingCount[i] > maxMeetingCount) {
                maxMeetingCount = meetingCount[i];
                maxMeetingCountRoom = i;
            }
        }

        return maxMeetingCountRoom;
    }
}


```
