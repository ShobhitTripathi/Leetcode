# Find optimal cost to construct a binary search tree
Find the optimal cost to construct a binary search tree where each key can repeat several times. We are given each key’s frequency in the same order as corresponding keys in the inorder traversal of a binary search tree.

To construct a binary search tree, we have to determine if the key already exists in the BST or not for each given key. The cost of finding a BST key is equal to the level of the key (if present in the BST).

For example, consider the following frequency array freq[] = { 25, 10, 20 } As frequency follows inorder order (ascending keys), let’s consider the index of freq[] as corresponding keys, i.e., 
Key 0 occurs 25 times
Key 1 occurs 10 times
Key 2 occurs 20 times
 Output: The optimal cost of constructing BST is 95. Following is the optimum BST: 

Cost is 25 + 10×2 + 20×3 = 105which is more than the optimal cost 95 

Cost is 20 + 10×2 + 25×3 = 115which is more than the optimal cost 95         

Cost is 10 + 25×2 + 20×2 = 100which is more than the optimal cost 95                

Link -: https://www.techiedelight.com/find-optimal-cost-to-construct-binary-search-tree/

The idea is simple – consider each key as a root and find an optimal solution by recursively finding the optimal cost of left and right subtree and add left and right child cost to the current node’s price (frequency of that key × level of that node). If the current node’s cost is optimal, update the result.


```java
class Main
{
    // Find optimal cost to construct a binary search tree from keys
    // `i` to `j`, where each key `k` occurs `freq[k]` number of times
    public static int findOptimalCost(int[] freq, int i, int j, int level)
    {
        // base case
        if (j < i) {
            return 0;
        }
 
        int optimalCost = Integer.MAX_VALUE;
 
        // consider each key as a root and recursively find an optimal solution
        for (int k = i; k <= j; k++)
        {
            // recursively find the optimal cost of the left subtree
            int leftOptimalCost = findOptimalCost(freq, i, k - 1, level + 1);
 
            // recursively find the optimal cost of the right subtree
            int rightOptimalCost = findOptimalCost(freq, k + 1, j, level + 1);
 
            // current node's cost is `freq[k]×level`
            int cost = freq[k] * level + leftOptimalCost + rightOptimalCost;
 
            // update the optimal cost
            optimalCost = Integer.min(optimalCost, cost);
        }
 
        // Return minimum value
        return optimalCost;
    }
 
    public static void main(String[] args)
    {
        int[] freq = { 25, 10, 20 };
 
        System.out.println("The optimal cost of constructing BST is "
                        + findOptimalCost(freq, 0, freq.length - 1, 1));
    }
}
```

Output:The optimal cost of constructing BST is 95


The time complexity of the above solution is exponential and occupies space in the call stack.

 The problem has optimal substructure. We have seen that the problem can be broken down into smaller subproblems, which can further be broken down into yet smaller subproblems, and so on. The problem also exhibits overlapping subproblems, so we will end up solving the same subproblem over and over again. We know that problems with optimal substructure and overlapping subproblems can be solved by dynamic programming, where subproblem solutions are memoized rather than computed and again.

DP Solution

```java
import java.util.HashMap;
import java.util.Map;
 
class Main
{
    // Find optimal cost to construct a binary search tree from keys `i` to `j`
    // where each key `k` occurs `freq[k]` number of times
    public static int findOptimalCost(int[] freq, int i, int j, int level,
                                    Map<String, Integer> lookup)
    {
        // base case
        if (j < i) {
            return 0;
        }
 
        // construct a unique map key from dynamic elements of the input
        String key = i + "|" + j + "|" + level;
 
        // if the subproblem is seen for the first time, solve it and
        // store its result in a map
        if (!lookup.containsKey(key))
        {
            lookup.put(key, Integer.MAX_VALUE);
 
            int leftOptimalCost, rightOptimalCost;
 
            // consider each key as root and recursively find an optimal solution
            for (int k = i; k <= j; k++)
            {
                // recursively find the optimal cost of the left subtree
                leftOptimalCost = findOptimalCost(freq, i, k - 1, level + 1, lookup);
 
                // recursively find the optimal cost of the right subtree
                rightOptimalCost = findOptimalCost(freq, k + 1, j, level + 1, lookup);
 
                // current node's cost is `freq[k]×level`
                int cost = freq[k] * level + leftOptimalCost + rightOptimalCost;
 
                // update the optimal cost
                lookup.put(key, Integer.min (lookup.get(key), cost));
            }
        }
 
        // return the subproblem solution from the map
        return lookup.get(key);
    }
 
    public static void main(String[] args)
    {
        int[] freq = { 25, 10, 20 };
 
        // create a map to store solutions to subproblems
        Map<String, Integer> lookup = new HashMap<>();
 
        System.out.println("The optimal cost of constructing BST is "
                + findOptimalCost(freq, 0, freq.length - 1, 1, lookup));
    }
}
```

Output:The optimal cost of constructing BST is 95


The time complexity of the above solution is O(n^4), where n is the total number of keys. 
The auxiliary space required by the program is O(n3) for recursion (call stack).

 We can also implement the bottom-up version of the above memoized solution.

 Memoized Solution

```java
import java.util.stream.IntStream;
 
class Main
{
    // Function to find the optimal cost to construct a binary search tree
    public static int findOptimalCost(int[] freq)
    {
        int n = freq.length;
 
        // `cost[i][j]` stores the optimal cost to construct BST from keys `i` to `j`
        int cost[][] = new int[n + 1][n + 1];
 
        // base case: cost is equal to frequency for `i = j` (single key)
        for (int i = 0; i < n; i++) {
            cost[i][i] = freq[i];
        }
 
        // all sizes of sequences
        for (int size = 1; size <= n; size++)
        {
            // all starting points of sequences
            for (int i = 0; i <= n - size + 1; i++)
            {
                int j = Math.min(i + size - 1, n - 1);
                cost[i][j] = Integer.MAX_VALUE;
 
                // consider each key as root and calculate the optimal cost
                for (int r = i; r <= j; r++)
                {
                    // get the current node's cost
                    int total = IntStream.rangeClosed(i, j).map(k -> freq[k]).sum();
 
                    // add the optimal cost of the left subtree
                    if (r != i) {
                        total += cost[i][r - 1];
                    }
 
                    // add the optimal cost of the right subtree
                    if (r != j) {
                        total += cost[r + 1][j];
                    }
 
                    // update the cost matrix if needed
                    cost[i][j] = Math.min(total, cost[i][j]);
                }
            }
        }
 
        // return the resultant cost
        return cost[0][n - 1];
    }
 
    public static void main(String[] args)
    {
        int[] freq = { 25, 10, 20 };
 
        System.out.println("The optimal cost of constructing BST is "
                + findOptimalCost(freq));
    }
}
```

Output:The optimal cost of constructing BST is 95

The time complexity of the above solution is O(n4), where n is the total number of keys. 
The auxiliary space required by the program is O(n3) for recursion (call stack).
