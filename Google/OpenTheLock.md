752. Open the Lock

You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'.
The wheels can rotate freely and wrap around: for example we can turn '9' to be '0', or '0' to be '9'. 
Each move consists of turning one wheel one slot.
The lock initially starts at '0000', a string representing the state of the 4 wheels.

You are given a list of deadends dead ends, meaning if the lock displays any of these codes, the wheels of the lock will stop turning and you will be unable to open it.

Given a target representing the value of the wheels that will unlock the lock, return the minimum total number of turns required to open the lock, or -1 if it is impossible.

 Example 1:
```
Input: deadends = ["0201","0101","0102","1212","2002"], target = "0202"
Output: 6
Explanation: 
A sequence of valid moves would be "0000" -> "1000" -> "1100" -> "1200" -> "1201" -> "1202" -> "0202".
Note that a sequence like "0000" -> "0001" -> "0002" -> "0102" -> "0202" would be invalid,
because the wheels of the lock become stuck after the display becomes the dead end "0102".
```
Example 2:
```
Input: deadends = ["8888"], target = "0009"
Output: 1
Explanation: We can turn the last wheel in reverse to move from "0000" -> "0009".
```
Example 3:
```
Input: deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
Output: -1
Explanation: We cannot reach the target without getting stuck.
```

Constraints:
```
1 <= deadends.length <= 500
deadends[i].length == 4
target.length == 4
target will not be in the list deadends.
target and deadends[i] consist of digits only.
```



```java
class Solution {
    public int openLock(String[] deadends, String target) {
                // Store deadends for quick lookup
        Set<String> dead = new HashSet<>(Arrays.asList(deadends));
        if (dead.contains("0000")) return -1;  // Cannot even start
        if (target.equals("0000")) return 0;   // Already unlocked

        // BFS queue starting from "0000"
        Queue<String> queue = new LinkedList<>();
        queue.offer("0000");

        // Visited set to avoid revisiting states
        Set<String> visited = new HashSet<>();
        visited.add("0000");

        int steps = 0; // Tracks number of turns

        // Standard BFS loop
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String curr = queue.poll();

                // Found the target
                if (curr.equals(target)) return steps;

                // Generate all possible next states (neighbors)
                for (String next : getNeighbors(curr)) {
                    // Add to queue if not a deadend and not visited
                    if (!dead.contains(next) && visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }
            steps++; // Increment step after exploring one BFS layer
        }

        return -1; // Target not reachable
    }

    // Generate all 8 possible states by turning each wheel +1 or -1
    private List<String> getNeighbors(String s) {
        List<String> res = new ArrayList<>();
        char[] chars = s.toCharArray();

        for (int i = 0; i < 4; i++) {
            char c = chars[i];

            // Turn wheel forward
            chars[i] = (char)((c - '0' + 1) % 10 + '0');
            res.add(new String(chars));

            // Turn wheel backward
            chars[i] = (char)((c - '0' + 9) % 10 + '0');
            res.add(new String(chars));

            // Reset to original for next iteration
            chars[i] = c;
        }
        return res;
    }
}

```


Approach (Brief)
```
Treat each lock configuration ("0000" → "9999") as a node in a graph.
Each move (turning one wheel up or down) generates 8 neighbors.

Use BFS from the starting state "0000":
BFS ensures the shortest number of moves is found.

Keep track of visited states to avoid cycles.
Skip over deadends.
Return the number of steps when the target is reached, or -1 if unreachable.
```

Dry Run Example
```
Input:
deadends = ["0201","0101","0102","1212","2002"], target = "0202"

Start: "0000", steps = 0

Neighbors of "0000": "1000","9000","0100","0900","0010","0090","0001","0009"
(Add them to queue if not deadends)

Step 1 → Explore "1000"
Neighbors: "2000","0000","1100","1900","1010","1090","1001","1009"
Keep exploring…

After BFS expansion, one valid shortest path:

"0000" → "1000" → "1100" → "1200" → "1201" → "1202" → "0202"


Steps = 6
```

Complexity
```
States: 10,000 max (0000–9999).
Neighbors per state: 8.
Time: O(10⁴) (feasible).
Space: O(10⁴) for visited + queue.
```
