1326. Minimum Number of Taps to Open to Water a Garden

There is a one-dimensional garden on the x-axis. The garden starts at the point 0 and ends at the point n. 
(i.e., the length of the garden is n).

There are n + 1 taps located at points [0, 1, ..., n] in the garden.

Given an integer n and an integer array ranges of length n + 1 
where ranges[i] (0-indexed) means the i-th tap can water the area [i - ranges[i], i + ranges[i]] if it was open.

Return the minimum number of taps that should be open to water the whole garden, 
If the garden cannot be watered return -1.

 <img width="418" height="213" alt="image" src="https://github.com/user-attachments/assets/ce6ffd81-cae6-4631-af8c-6cbddc3d33db" />


Example 1:
```
Input: n = 5, ranges = [3,4,1,1,0,0]
Output: 1
Explanation: The tap at point 0 can cover the interval [-3,3]
The tap at point 1 can cover the interval [-3,5]
The tap at point 2 can cover the interval [1,3]
The tap at point 3 can cover the interval [2,4]
The tap at point 4 can cover the interval [4,4]
The tap at point 5 can cover the interval [5,5]
Opening Only the second tap will water the whole garden [0,5]
```
Example 2:
```
Input: n = 3, ranges = [0,0,0,0]
Output: -1
Explanation: Even if you activate all the four taps you cannot water the whole garden.
```
Constraints:
```
1 <= n <= 104
ranges.length == n + 1
0 <= ranges[i] <= 100
```

Approach 2: Greedy
```
We highly recommend you to solve the problem Jump Game II before reading this approach.

Intuition
Let the leftmost position of the tap's range be start and the rightmost position be end.

First, we compute an auxiliary array max_reach. Let max_reach[i] be the maximum end over all taps having start=i. We will use this array in our algorithm.

Let's reformulate our problem in a slightly different manner.
You start at the position 0. You can jump from position i to the right but not further that max_reach[i]. What is the minimum number of jumps to reach position n?
In this way, we reduce our problem to Jump Game II.

In the greedy approach, we follow an intuitive strategy. We start with taps=0 and a pointer curr_end. taps represents the number of taps used. The pointer curr_end represents the position such that we have currently watered the part of the garden from position 0 to curr_end. Initially curr_end=0 points to the start of the garden since we have not watered anything yet.

At each step, we select the tap that can water the furthest right in the garden (we denote this position as next_end) among the taps that can reach curr_end. Then we set curr_end to next_end and continue the process.

We can formulate the subproblem as follows: find next_end – the maximum end over the taps having start≤curr_end (covering the position curr_end).

The tap is interesting only if max_reach[start]=end because otherwise there exists another tap with the same start but with greater end and it covers a bigger range.

How can we rewrite the subproblem in terms of max_reach? We replace start with i and end with max_reach[i] and obtain: find next_end – the maximum max_reach[i] over the positions i≤curr_end.

After finding next_end, we treat it as our new current position in the garden and assign curr_end=next_end. This allows us to move forward and continue the iteration. We also increment taps, since we open one more tap ending at the position next_end.

By iterating through the taps in this manner and selecting the tap with the furthest reach at each step, we aim to maximize the coverage of the garden with each tap selection. This strategy helps ensure that we efficiently water as much of the garden as possible with the minimum number of taps.

This process continues until we reach the end of the garden. At that point, if we have successfully selected taps that cover the entire garden, we return the count of chosen taps as the minimum number required. However, if it is not possible to water the entire garden, we return −1 to indicate that it cannot be achieved.
```

Algorithm
```
Declare the array max_reach.
Iterate i from 0 to n (over the taps).
Calculate start – the leftmost and end – the rightmost positions the tap can reach.
Update max_reach[start] with end if it is larger.
Declare the variables taps – number of taps used, curr_end – current rightmost position reached, next_end – next rightmost position that can be reached. Initialize taps=0, curr_end=0, next_end=0.
Iterate i from 0 to n through the garden.
If i>next_end, it means that the current position cannot be reached and we return −1.
If i>curr_end, it means that we have to open a new tap ending at the position next_end.
Increment taps.
Set curr_end to next_end.
Update next_end with max_reach[i] if it is larger.
Return taps.
```

Implementation
```java
// O(n)
class Solution {
    public int minTaps(int n, int[] ranges) {
        // Create an array to track the maximum reach for each position
        int[] maxReach = new int[n + 1];

        // Calculate the maximum reach for each tap
        for (int i = 0; i < ranges.length; i++) {
            // Calculate the leftmost position the tap can reach
            int start = Math.max(0, i - ranges[i]);
            // Calculate the rightmost position the tap can reach
            int end = Math.min(n, i + ranges[i]);

            // Update the maximum reach for the leftmost position
            maxReach[start] = Math.max(maxReach[start], end);
        }
        
        // Number of taps used
        int taps = 0;
        // Current rightmost position reached
        int currEnd = 0;
        // Next rightmost position that can be reached
        int nextEnd = 0;

        // Iterate through the garden
        for (int i = 0; i <= n; i++) {
            // Current position cannot be reached
            if (i > nextEnd) {
                return -1;
            }

            // Increment taps when moving to a new tap
            if (i > currEnd) {
                taps++;
                // Move to the rightmost position that can be reached
                currEnd = nextEnd;
            }

            // Update the next rightmost position that can be reached
            nextEnd = Math.max(nextEnd, maxReach[i]);
        }

        // Return the minimum number of taps used
        return taps;
    }
}

```
