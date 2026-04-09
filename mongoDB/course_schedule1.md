# [207. Course Schedule](https://leetcode.com/problems/course-schedule/)
Medium


There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return true if you can finish all courses. Otherwise, return false.

 

Example 1:
```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: true
Explanation: There are a total of 2 courses to take. 
To take course 1 you should have finished course 0. So it is possible.
```
Example 2:
```
Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false
Explanation: There are a total of 2 courses to take. 
To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.
``` 

Constraints:

- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= 5000
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- All the pairs prerequisites[i] are unique.

## Approach
```
Graph Representation:
 Represent the courses and prerequisites as a directed graph using an adjacency list. Each course points to its prerequisites.

Cycle Detection:
 Use DFS to detect cycles. If a cycle exists, it’s impossible to finish all courses.
 Maintain a visiting set to track nodes in the current recursion stack.

DFS Logic:
 If a node is already in the visiting set, a cycle is detected.
 If a node has no prerequisites (empty list in the map), it's safe to proceed.

Optimization:
 Once a course is processed without finding a cycle, mark it as "no prerequisites" (empty list) to avoid reprocessing.

Result:
 Return true if all courses are processed without cycles; otherwise, return false.
```

## Solution
```java
class Solution {
    // Adjacency list to represent course prerequisites
    Map<Integer, List<Integer>> graph = new HashMap<>();
    
    // Set to track nodes in the current DFS recursion stack (used to detect cycles)
    Set<Integer> visiting = new HashSet<>();
    
    // Set to track nodes that are fully processed (no cycle detected from them)
    Set<Integer> visited = new HashSet<>();

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Build the graph from prerequisites
        for (int[] pre : prerequisites) {
            // pre[0] depends on pre[1], so add an edge from pre[0] -> pre[1]
            graph.computeIfAbsent(pre[0], k -> new ArrayList<>()).add(pre[1]);
        }

        // Check for cycles starting from every course (handles disconnected components)
        for (int course = 0; course < numCourses; course++) {
            // If a cycle is detected, return false
            if (!dfs(course)) {
                return false;
            }
        }

        // If no cycle is found in any DFS traversal, return true
        return true;
    }

    // Helper method to perform DFS and detect cycles
    private boolean dfs(int course) {
        // If this course was already fully processed, skip it
        if (visited.contains(course)) return true;

        // If this course is already in the current path, a cycle is detected
        if (visiting.contains(course)) return false;

        // Mark this course as being visited in the current path
        visiting.add(course);

        // Traverse all prerequisites (dependencies) of the current course
        for (int prereq : graph.getOrDefault(course, Collections.emptyList())) {
            // If any dependency leads to a cycle, return false
            if (!dfs(prereq)) {
                return false;
            }
        }

        // Remove from current path and mark as fully processed
        visiting.remove(course);
        visited.add(course);

        // No cycle detected from this node
        return true;
    }
}


```

## Complexity Analysis
```
- Time Complexity: O(m*n)
- Space Complexity: O(m*n)
```
