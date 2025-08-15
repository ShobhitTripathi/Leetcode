# [84. Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/)
Hard


Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

 

Example 1:
```
Input: heights = [2,1,5,6,2,3]
Output: 10
Explanation: The above is a histogram where width of each bar is 1.
The largest rectangle is shown in the red area, which has an area = 10 units.
```
Example 2:
```
Input: heights = [2,4]
Output: 4
 ```

Constraints:

- 1 <= heights.length <= 105
- 0 <= heights[i] <= 104

## Approach
```
- Monotonic Stack
- store index and height in the stack
- while the top element is greater in height than the new element, 
  - keep poping the top element and compare the area with maxArea
  - start will have the index of the poped element(new element can be extended towards the left)
- push start and height into the stack
- while stack is not empty,
  - keep poping and compare the area with maxArea
- return maxArea
```

## Solution
```java

class Solution {
    public int largestRectangleArea(int[] heights) {
        int maxArea = 0; // Variable to keep track of the maximum area
        Stack<int[]> st = new Stack<>(); // Stack to store pairs of {start index, height}

        // Iterate through each bar in the histogram
        for (int i = 0; i < heights.length; i++) {
            int start = i; // Start index of the rectangle

            // Adjust the stack for any bars taller than the current height
            while (!st.isEmpty() && st.peek()[1] > heights[i]) {
                // Pop the taller bar from the stack
                int[] pair = st.pop();

                // Calculate the area of the rectangle formed by the popped bar
                int width = i - pair[0]; // Current index minus the start index
                int height = pair[1];   // Height of the popped bar
                maxArea = Math.max(maxArea, height * width); // Update the maximum area

                // Update the start index for the next rectangle
                start = pair[0];
            }

            // Push the current bar into the stack with the updated start index
            st.push(new int[]{start, heights[i]});
        }

        // Process any remaining bars in the stack
        while (!st.isEmpty()) {
            int[] pair = st.pop();

            // Calculate the area for the remaining bars in the stack
            int width = heights.length - pair[0]; // Full width from the start index to the end
            int height = pair[1];                // Height of the remaining bar
            maxArea = Math.max(maxArea, height * width); // Update the maximum area
        }

        return maxArea; // Return the largest rectangle area found
    }
}
```

## Complexity Analysis
```
- Time Complexity: O(N)
- Space Complexity: O(N)
```
