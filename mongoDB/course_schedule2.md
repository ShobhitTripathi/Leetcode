# [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)
Medium


There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return the ordering of courses you should take to finish all courses. If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.

 

Example 1:
```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: [0,1]
Explanation: There are a total of 2 courses to take. To take course 1 you should have finished course 0. So the correct course order is [0,1].
```
Example 2:
```
Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
Output: [0,2,1,3]
Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3].
```
Example 3:
```
Input: numCourses = 1, prerequisites = []
Output: [0]
 ```

Constraints:

- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= numCourses * (numCourses - 1)
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- ai != bi
- All the pairs [ai, bi] are distinct.

## Aprroach
Ref: [8. Course Schedule](https://github.com/dipjul/NeetCode-150/blob/1db1597fe0d82d4741ecd5ee3600aea518824bb1/11.%20Graphs/8.CourseSchedule.md)

Graph Representation:
 Represent the courses and their prerequisites as a directed graph using an adjacency list. Each course points to its prerequisites.
 
DFS with Cycle Detection:
 Use DFS to traverse the graph and check for cycles. If a cycle exists, return an empty array as no valid course order is possible.
 Use a cycle set to track nodes in the current recursion stack.

Topological Sorting:
 If a course is fully processed (all its prerequisites have been satisfied), add it to the output list.
 This ensures that prerequisites are added before dependent courses.

Final Output:
 Convert the output list to an array in reverse order to represent the topological sort.
```
Topologicalo Sort
```

## Solution
```java
class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> map = new HashMap<>(); // Adjacency list to represent the graph

        // Build the graph by mapping each course to its prerequisites
        for (int[] pair : prerequisites) {
            map.computeIfAbsent(pair[0], k -> new ArrayList<>()).add(pair[1]);
        }

        List<Integer> output = new ArrayList<>(); // Stores the course order
        Set<Integer> visit = new HashSet<>(); // Tracks visited nodes
        Set<Integer> cycle = new HashSet<>(); // Tracks nodes in the current recursion stack to detect cycles

        // Perform DFS for each course
        for (int course = 0; course < numCourses; course++) {
            if (!dfs(course, map, visit, cycle, output)) {
                return new int[0]; // Return an empty array if a cycle is detected
            }
        }

        // Convert the output list to an array in reverse order (topological order)
        int[] result = new int[numCourses];
        for (int i = 0; i < output.size(); i++) {
            result[i] = output.get(i);
        }
        return result;
    }

    private boolean dfs(int course, Map<Integer, List<Integer>> map, Set<Integer> visit,
                        Set<Integer> cycle, List<Integer> output) {
        if (cycle.contains(course)) { // Cycle detected
            return false;
        }
        if (visit.contains(course)) { // Already processed
            return true;
        }

        cycle.add(course); // Mark the course as being processed

        // Process all prerequisites for the current course
        for (int pre : map.getOrDefault(course, Collections.emptyList())) {
            if (!dfs(pre, map, visit, cycle, output)) {
                return false; // Return false if a cycle is detected in prerequisites
            }
        }

        cycle.remove(course); // Remove from recursion stack
        visit.add(course); // Mark the course as fully processed
        output.add(course); // Add the course to the output list
        return true; // No cycle detected
    }
}

```

## Complexity Analysis
```
- Time Complexity: O(n*m)
- Space Complexity: O(n*m)
```
