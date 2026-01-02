# 2141. Maximum Running Time of N Computers

You have n computers. You are given the integer n and a 0-indexed integer array batteries where the ith battery can run a computer for batteries[i] minutes. You are interested in running all n computers simultaneously using the given batteries.

Initially, you can insert at most one battery into each computer. After that and at any integer time moment, you can remove a battery from a computer and insert another battery any number of times. The inserted battery can be a totally new battery or a battery from another computer. You may assume that the removing and inserting processes take no time.

Note that the batteries cannot be recharged.

Return the maximum number of minutes you can run all the n computers simultaneously.

Example 1:
```
Input: n = 2, batteries = [3,3,3]
Output: 4
Explanation: 
Initially, insert battery 0 into the first computer and battery 1 into the second computer.
After two minutes, remove battery 1 from the second computer and insert battery 2 instead. Note that battery 1 can still run for one minute.
At the end of the third minute, battery 0 is drained, and you need to remove it from the first computer and insert battery 1 instead.
By the end of the fourth minute, battery 1 is also drained, and the first computer is no longer running.
We can run the two computers simultaneously for at most 4 minutes, so we return 4.
```

Example 2:
```

Input: n = 2, batteries = [1,1,1,1]
Output: 2
Explanation: 
Initially, insert battery 0 into the first computer and battery 2 into the second computer. 
After one minute, battery 0 and battery 2 are drained so you need to remove them and insert battery 1 into the first computer and battery 3 into the second computer. 
After another minute, battery 1 and battery 3 are also drained so the first and second computers are no longer running.
We can run the two computers simultaneously for at most 2 minutes, so we return 2.
 ```

Constraints:
```
1 <= n <= batteries.length <= 105
1 <= batteries[i] <= 109
```


Approach
```
“We binary search the running time and check feasibility by summing each battery’s capped contribution.”

Key observation:
Batteries can be swapped at any time
What matters is total energy, not assignment order


Feasibility Check (Key Insight)
For a candidate time T:
  Each battery with power p can contribute:
    min(p, T)
  Total available power:
    Σ min(battery[i], T)

To run n computers for T minutes:
Required power = n × T

Feasible condition:
  Σ min(battery[i], T) ≥ n × T

This condition is monotonic:
  If T is feasible → all smaller times are feasible
  If T is not feasible → all larger times are not feasible



Why Binary Search Works

Search space:
T ∈ [1, sum / n]
1 is always possible
sum / n is the absolute upper bound
We binary search to find the maximum feasible T.


⚖️ Common Interview Follow-ups
Why min(battery, T)?
Because a battery cannot power more than one computer at a time beyond T.

Why use upper mid?
To avoid infinite loop when left + 1 == right.

Why swapping batteries doesn’t matter?
Because power is divisible over time; only total available energy matters.

```

Example Walkthrough
```
n = 2
batteries = [3, 3, 3]
sum = 9
right = 9 / 2 = 4


Check T = 4:

min(3,4) + min(3,4) + min(3,4) = 9
Required = 2 × 4 = 8
→ feasible


Check T = 5:

max possible = 9 < 10
→ not feasible
Answer = 4
```


Solution
```java
//Time Complexity: O(m × log(sum / n))
class Solution {

    public long maxRunTime(int n, int[] batteries) {

        // Total available battery power
        long sum = 0;
        for (int b : batteries) sum += b;

        // Minimum possible running time is 1
        long left = 1;

        // Maximum possible running time per computer
        // cannot exceed total power divided equally
        long right = sum / n;

        // Binary search for the maximum feasible running time
        while (left < right) {

            // Upper mid to avoid infinite loop
            long target = right - (right - left) / 2;

            long extra = 0;

            // Calculate how much total power can be supplied
            // if each computer runs for 'target' time
            for (int power : batteries) {
                // A battery can contribute at most 'target'
                extra += Math.min(power, target);
            }

            // If total power is sufficient for n computers
            if (extra >= n * target) {
                left = target;          // target is feasible, try higher
            } else {
                right = target - 1;     // target is not feasible
            }
        }

        return left;
    }
}

```
