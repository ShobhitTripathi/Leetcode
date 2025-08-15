# 1944. Number of Visible People in a Queue

There are n people standing in a queue, and they numbered from 0 to n - 1 in left to right order. 
You are given an array heights of distinct integers where heights[i] represents the height of the ith person.

A person can see another person to their right in the queue if everybody in between is shorter than both of them. 
More formally, the ith person can see the jth person if i < j and min(heights[i], heights[j]) > max(heights[i+1], heights[i+2], ..., heights[j-1]).

Return an array answer of length n where answer[i] is the number of people the ith person can see to their right in the queue.

 

Example 1:
```
Input: heights = [10,6,8,5,11,9]
Output: [3,1,2,1,1,0]
Explanation:
Person 0 can see person 1, 2, and 4.
Person 1 can see person 2.
Person 2 can see person 3 and 4.
Person 3 can see person 4.
Person 4 can see person 5.
Person 5 can see no one since nobody is to the right of them.
```

Example 2:
```
Input: heights = [5,1,2,3,10]
Output: [4,1,1,1,0]
```
 

Constraints:
```
n == heights.length
1 <= n <= 105
1 <= heights[i] <= 105
All the values of heights are unique.
```


```java
// time: O(N) space: O(N), Monotonic decreasing order stack
class Solution {
    public int[] canSeePersonsCount(int[] heights) {
        int n = heights.length;
        int[] result = new int[n];     // result[i] will store number of people person i can see
        Stack<Integer> stack = new Stack<>(); // monotonic decreasing stack (stores heights to the right of current person)

        // Process from right to left
        for (int i = n - 1; i >= 0; i--) {
            int count = 0;

            // Pop all people shorter than current person
            // Each popped person is visible because they are not blocked
            while (!stack.isEmpty() && stack.peek() < heights[i]) {
                stack.pop();
                count++;
            }

            // If stack still has someone, it means the next taller person is also visible
            if (!stack.isEmpty()) {
                count++;
            }
            
            // Store the count for this person
            result[i] = count;

            // Push current person's height into the stack
            // They might block future people on the left
            stack.push(heights[i]);
        }
        
        return result;
    }
}


```

```approach
Approach:
    ----------
    - We process the queue from right to left using a monotonic decreasing stack.
    - For each person:
        1. Pop all shorter people → all are visible to the current person.
        2. If a taller person remains → they are also visible (+1 count).
        3. Push the current person's height on the stack for future visibility checks.
    - Each height is pushed/popped at most once → O(n) time complexity.

    Dry Run Example: heights = [10, 6, 8, 5, 11, 9]
    -------------------------------------------------
    i=5 (9): stack=[] → count=0 → push 9 → stack=[9]
    i=4 (11): pop 9 (count=1), stack empty → push 11 → stack=[11]
    i=3 (5): top 11 taller → count=1 → push 5 → stack=[5, 11]
    i=2 (8): pop 5 (count=1), top 11 taller → count=2 → push 8 → stack=[8, 11]
    i=1 (6): top 8 taller → count=1 → push 6 → stack=[6, 8, 11]
    i=0 (10): pop 6 (count=1), pop 8 (count=2), top 11 taller → count=3 → push 10 → stack=[10, 11]
    Result = [3, 1, 2, 1, 1, 0]
```
