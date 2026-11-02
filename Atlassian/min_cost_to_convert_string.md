Problem Statement
```
2976. Minimum Cost to Convert String I

You are given two 0-indexed strings source and target, both of length n and consisting of lowercase English letters. You are also given two 0-indexed character arrays original and changed, and an integer array cost, where cost[i] represents the cost of changing the character original[i] to the character changed[i].

You start with the string source. In one operation, you can pick a character x from the string and change it to the character y at a cost of z if there exists any index j such that cost[j] == z, original[j] == x, and changed[j] == y.

Return the minimum cost to convert the string source to the string target using any number of operations. If it is impossible to convert source to target, return -1.

Note that there may exist indices i, j such that original[j] == original[i] and changed[j] == changed[i].

 

Example 1:

Input: source = "abcd", target = "acbe", original = ["a","b","c","c","e","d"], changed = ["b","c","b","e","b","e"], cost = [2,5,5,1,2,20]
Output: 28
Explanation: To convert the string "abcd" to string "acbe":
- Change value at index 1 from 'b' to 'c' at a cost of 5.
- Change value at index 2 from 'c' to 'e' at a cost of 1.
- Change value at index 2 from 'e' to 'b' at a cost of 2.
- Change value at index 3 from 'd' to 'e' at a cost of 20.
The total cost incurred is 5 + 1 + 2 + 20 = 28.
It can be shown that this is the minimum possible cost.
Example 2:

Input: source = "aaaa", target = "bbbb", original = ["a","c"], changed = ["c","b"], cost = [1,2]
Output: 12
Explanation: To change the character 'a' to 'b' change the character 'a' to 'c' at a cost of 1, followed by changing the character 'c' to 'b' at a cost of 2, for a total cost of 1 + 2 = 3. To change all occurrences of 'a' to 'b', a total cost of 3 * 4 = 12 is incurred.
Example 3:

Input: source = "abcd", target = "abce", original = ["a"], changed = ["e"], cost = [10000]
Output: -1
Explanation: It is impossible to convert source to target because the value at index 3 cannot be changed from 'd' to 'e'.

```

Algorithm
```
Main method minimumCost:

Create an adjacencyList with 26 entries (one for each lowercase letter).
Iterate through the original array: For each index i:
Add an edge to adjacencyList from original[i] to changed[i], with the corresponding cost[i].
For each of the 26 characters, call dijkstra to find the shortest path from this character to all other characters.
Store the results in a 2D array minConversionCosts of size 26×26.
Initialize a variable totalCost to 0.
Iterate through the length of source:
If the character at the current position differs from target:
Look up the conversion cost in minConversionCosts:
If the conversion is impossible (cost is -1), return -1.
Else, add the cost to totalCost.
Return totalCost as the answer.

Helper method dijkstra:

Define a method dijkstra with parameters: startChar and adjacencyList.
Create a priority queue priorityQueue with each element as a pair of (cost, character). Sort the queue by cost (lowest first).
Initialize an array minCosts of size 26 with all values set to -1 (representing unreachable positions).
Add startChar to priorityQueue with a cost of 0.
While priorityQueue is not empty:
Poll a pair (currentCost, currentChar) from the queue.
Loop over all possible conversions from currentChar using the adjacencyList. For each conversion to targetChar:
Find the newTotalCost to do the conversion as currentCost + conversionCost.
If the conversion hasn't been reached yet minCosts[targetChar] == -1, or newTotalCost is less than the previous cost in minCosts[targetChar]:
Set minCosts[targetChar] as newTotalCost.
Add the pair (newTotalCost, targetChar) to the priority queue.
Return minCosts.
```

Solution

```java
// Time: O(m+n)  Space: O(m)

class Solution {

    public long minimumCost(
        String source,
        String target,
        char[] original,
        char[] changed,
        int[] cost
    ) {
        // Create a graph representation of character conversions
        List<int[]>[] adjacencyList = new List[26];
        for (int i = 0; i < 26; i++) {
            adjacencyList[i] = new ArrayList<>();
        }

        // Populate the adjacency list with character conversions
        int conversionCount = original.length;
        for (int i = 0; i < conversionCount; i++) {
            adjacencyList[original[i] - 'a'].add(
                    new int[] { changed[i] - 'a', cost[i] }
                );
        }

        // Calculate shortest paths for all possible character conversions
        long[][] minConversionCosts = new long[26][26];
        for (int i = 0; i < 26; i++) {
            minConversionCosts[i] = dijkstra(i, adjacencyList);
        }

        // Calculate the total cost of converting source to target
        long totalCost = 0;
        int stringLength = source.length();
        for (int i = 0; i < stringLength; i++) {
            if (source.charAt(i) != target.charAt(i)) {
                long charConversionCost =
                    minConversionCosts[source.charAt(i) - 'a'][target.charAt(
                            i
                        ) -
                        'a'];
                if (charConversionCost == -1) {
                    return -1; // Conversion not possible
                }
                totalCost += charConversionCost;
            }
        }
        return totalCost;
    }

    // Find minimum conversion costs from a starting character to all other characters
    private long[] dijkstra(int startChar, List<int[]>[] adjacencyList) {
        // Priority queue to store characters with their conversion cost, sorted by cost
        PriorityQueue<Pair<Long, Integer>> priorityQueue = new PriorityQueue<>(
            (e1, e2) -> Long.compare(e1.getKey(), e2.getKey())
        );

        // Initialize the starting character with cost 0
        priorityQueue.add(new Pair<>(0L, startChar));

        // Array to store the minimum conversion cost to each character
        long[] minCosts = new long[26];
        // Initialize all costs to -1 (unreachable)
        Arrays.fill(minCosts, -1L);

        while (!priorityQueue.isEmpty()) {
            Pair<Long, Integer> currentPair = priorityQueue.poll();
            long currentCost = currentPair.getKey();
            int currentChar = currentPair.getValue();

            if (
                minCosts[currentChar] != -1L &&
                minCosts[currentChar] < currentCost
            ) continue;

            // Explore all possible conversions from the current character
            for (int[] conversion : adjacencyList[currentChar]) {
                int targetChar = conversion[0];
                long conversionCost = conversion[1];
                long newTotalCost = currentCost + conversionCost;

                // If we found a cheaper conversion, update its cost
                if (
                    minCosts[targetChar] == -1L ||
                    newTotalCost < minCosts[targetChar]
                ) {
                    minCosts[targetChar] = newTotalCost;
                    // Add the updated conversion to the queue for further exploration
                    priorityQueue.add(new Pair<>(newTotalCost, targetChar));
                }
            }
        }
        // Return the array of minimum conversion costs from the starting character to all others
        return minCosts;
    }
}

```
