package Practise;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Optimal Binary Search Tree Construction Problem
 * 
 * Problem Statement:
 * Given an array of frequencies where freq[i] represents how many times key 'i' appears,
 * find the minimum cost to construct a BST where the cost of accessing a key is:
 * frequency × level (root is at level 1)
 * 
 * Example: freq[] = {25, 10, 20}
 * - Key 0 appears 25 times
 * - Key 1 appears 10 times  
 * - Key 2 appears 20 times
 * 
 * Optimal BST structure:
 *       1 (level 1)
 *      / \
 *     0   2 (level 2)
 * 
 * Cost = 10×1 + 25×2 + 20×2 = 10 + 50 + 40 = 100
 * 
 * Link: https://www.techiedelight.com/find-optimal-cost-to-construct-binary-search-tree/
 */
public class OptimalBSTCost {

    /**
     * APPROACH 1: NAIVE RECURSIVE SOLUTION
     * 
     * Time Complexity: O(n × 2^n) - Exponential
     * Space Complexity: O(n) - Recursion call stack depth
     * 
     * Algorithm:
     * 1. Try each key as root (from i to j)
     * 2. Recursively calculate optimal cost for left subtree (i to k-1)
     * 3. Recursively calculate optimal cost for right subtree (k+1 to j)
     * 4. Current cost = freq[k] × level + leftCost + rightCost
     * 5. Return minimum cost among all choices
     * 
     * Why exponential?
     * - For each range [i,j], we try all possible roots
     * - Each root choice creates 2 subproblems
     * - Many overlapping subproblems are recalculated
     */
    public static int findOptimalCostRecursive(int[] freq, int i, int j, int level) {
        // Base case: empty range has zero cost
        if (j < i) {
            return 0;
        }

        int optimalCost = Integer.MAX_VALUE;

        // Try each key in range [i, j] as root
        for (int k = i; k <= j; k++) {
            // Cost of left subtree (keys i to k-1)
            int leftOptimalCost = findOptimalCostRecursive(freq, i, k - 1, level + 1);

            // Cost of right subtree (keys k+1 to j)
            int rightOptimalCost = findOptimalCostRecursive(freq, k + 1, j, level + 1);

            // Current node cost: frequency × current level
            int cost = freq[k] * level + leftOptimalCost + rightOptimalCost;

            // Track minimum cost
            optimalCost = Math.min(optimalCost, cost);
        }

        return optimalCost;
    }

    /**
     * APPROACH 2: MEMOIZATION (TOP-DOWN DYNAMIC PROGRAMMING)
     * 
     * Time Complexity: O(n^4)
     *   - Unique subproblems: O(n^2 × n) = O(n^3) [i, j, level combinations]
     *   - For each subproblem: O(n) iterations to try each root
     *   - Total: O(n^3) × O(n) = O(n^4)
     * 
     * Space Complexity: O(n^3)
     *   - HashMap stores O(n^3) unique states
     *   - Recursion depth: O(n)
     * 
     * Key Insight:
     * The problem has:
     * 1. Optimal Substructure: Optimal solution contains optimal solutions to subproblems
     * 2. Overlapping Subproblems: Same subproblems are solved multiple times
     * 
     * Memoization Strategy:
     * - Store results of subproblems in a HashMap
     * - Key format: "i|j|level" uniquely identifies each subproblem
     * - Before computing, check if result already exists
     */
    public static int findOptimalCostMemoized(int[] freq, int i, int j, int level,
                                              Map<String, Integer> lookup) {
        // Base case
        if (j < i) {
            return 0;
        }

        // Create unique key for this subproblem
        String key = i + "|" + j + "|" + level;

        // Check if already computed
        if (!lookup.containsKey(key)) {
            lookup.put(key, Integer.MAX_VALUE);

            // Try each key as root
            for (int k = i; k <= j; k++) {
                // Recursively solve left subtree
                int leftOptimalCost = findOptimalCostMemoized(freq, i, k - 1, level + 1, lookup);

                // Recursively solve right subtree
                int rightOptimalCost = findOptimalCostMemoized(freq, k + 1, j, level + 1, lookup);

                // Calculate total cost with k as root
                int cost = freq[k] * level + leftOptimalCost + rightOptimalCost;

                // Update optimal cost for this subproblem
                lookup.put(key, Math.min(lookup.get(key), cost));
            }
        }

        // Return memoized result
        return lookup.get(key);
    }

    /**
     * APPROACH 3: BOTTOM-UP DYNAMIC PROGRAMMING (TABULATION)
     * 
     * Time Complexity: O(n^3)
     *   - Three nested loops: size (n) × starting point (n) × root choice (n)
     *   - IntStream.sum() adds O(n) but can be optimized with prefix sums
     *   - Actual: O(n^4) due to sum calculation, but can be reduced to O(n^3)
     * 
     * Space Complexity: O(n^2)
     *   - 2D DP table: cost[n+1][n+1]
     *   - No recursion stack needed
     * 
     * DP Table Definition:
     * cost[i][j] = minimum cost to construct BST from keys i to j
     * 
     * Recurrence Relation:
     * cost[i][j] = min(sum(freq[i..j]) + cost[i][r-1] + cost[r+1][j]) for all r in [i,j]
     * 
     * Key Observation:
     * When a subtree becomes a child of another node, ALL nodes in that subtree
     * go one level deeper. So we add sum of all frequencies in the range.
     * 
     * Building Order:
     * 1. Start with single keys (size = 1): cost[i][i] = freq[i]
     * 2. Build up to larger ranges (size = 2, 3, ..., n)
     * 3. For each range, try all possible roots
     */
    public static int findOptimalCostDP(int[] freq) {
        int n = freq.length;

        // DP table: cost[i][j] = optimal cost for keys i to j
        int[][] cost = new int[n + 1][n + 1];

        // Base case: single key has cost equal to its frequency (at level 1)
        for (int i = 0; i < n; i++) {
            cost[i][i] = freq[i];
        }

        // Build solution for increasing subsequence sizes
        for (int size = 2; size <= n; size++) {
            // Try all starting positions for current size
            for (int i = 0; i <= n - size; i++) {
                int j = i + size - 1; // Ending position
                cost[i][j] = Integer.MAX_VALUE;

                // Try each key in range [i,j] as root
                for (int r = i; r <= j; r++) {
                    // Sum of all frequencies in range [i,j]
                    // This represents the cost when all these nodes go one level deeper
                    int total = IntStream.rangeClosed(i, j)
                                        .map(k -> freq[k])
                                        .sum();

                    // Add optimal cost of left subtree (if exists)
                    if (r != i) {
                        total += cost[i][r - 1];
                    }

                    // Add optimal cost of right subtree (if exists)
                    if (r != j) {
                        total += cost[r + 1][j];
                    }

                    // Update minimum cost
                    cost[i][j] = Math.min(total, cost[i][j]);
                }
            }
        }

        // Return optimal cost for entire range [0, n-1]
        return cost[0][n - 1];
    }

    /**
     * OPTIMIZED BOTTOM-UP DP WITH PREFIX SUMS
     * 
     * Time Complexity: O(n^3) - Truly cubic
     * Space Complexity: O(n^2)
     * 
     * Optimization: Use prefix sums to calculate range sum in O(1)
     */
    public static int findOptimalCostDPOptimized(int[] freq) {
        int n = freq.length;
        int[][] cost = new int[n][n];

        // Precompute prefix sums for O(1) range sum queries
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + freq[i];
        }

        // Base case: single keys
        for (int i = 0; i < n; i++) {
            cost[i][i] = freq[i];
        }

        // Build for increasing sizes
        for (int size = 2; size <= n; size++) {
            for (int i = 0; i <= n - size; i++) {
                int j = i + size - 1;
                cost[i][j] = Integer.MAX_VALUE;

                // Range sum in O(1)
                int rangeSum = prefixSum[j + 1] - prefixSum[i];

                // Try each root
                for (int r = i; r <= j; r++) {
                    int leftCost = (r > i) ? cost[i][r - 1] : 0;
                    int rightCost = (r < j) ? cost[r + 1][j] : 0;
                    int total = rangeSum + leftCost + rightCost;
                    cost[i][j] = Math.min(cost[i][j], total);
                }
            }
        }

        return cost[0][n - 1];
    }

    /**
     * Helper method to visualize the DP table
     */
    public static void printDPTable(int[] freq) {
        int n = freq.length;
        int[][] cost = new int[n][n];
        int[][] root = new int[n][n]; // Track which key was chosen as root

        // Compute DP with root tracking
        for (int i = 0; i < n; i++) {
            cost[i][i] = freq[i];
            root[i][i] = i;
        }

        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + freq[i];
        }

        for (int size = 2; size <= n; size++) {
            for (int i = 0; i <= n - size; i++) {
                int j = i + size - 1;
                cost[i][j] = Integer.MAX_VALUE;
                int rangeSum = prefixSum[j + 1] - prefixSum[i];

                for (int r = i; r <= j; r++) {
                    int leftCost = (r > i) ? cost[i][r - 1] : 0;
                    int rightCost = (r < j) ? cost[r + 1][j] : 0;
                    int total = rangeSum + leftCost + rightCost;
                    
                    if (total < cost[i][j]) {
                        cost[i][j] = total;
                        root[i][j] = r;
                    }
                }
            }
        }

        // Print DP table
        System.out.println("\nDP Cost Table:");
        System.out.print("    ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%4d ", j);
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < n; j++) {
                if (j >= i) {
                    System.out.printf("%4d ", cost[i][j]);
                } else {
                    System.out.print("   - ");
                }
            }
            System.out.println();
        }

        System.out.println("\nOptimal Root Choices:");
        System.out.print("    ");
        for (int j = 0; j < n; j++) {
            System.out.printf("%4d ", j);
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.printf("%2d: ", i);
            for (int j = 0; j < n; j++) {
                if (j >= i) {
                    System.out.printf("%4d ", root[i][j]);
                } else {
                    System.out.print("   - ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Main method with comprehensive testing and explanations
     */
    public static void main(String[] args) {
        int[] freq = {25, 10, 20};

        System.out.println("=".repeat(80));
        System.out.println("OPTIMAL BINARY SEARCH TREE CONSTRUCTION");
        System.out.println("=".repeat(80));
        System.out.println("\nInput Frequencies: ");
        for (int i = 0; i < freq.length; i++) {
            System.out.printf("  Key %d: frequency = %d\n", i, freq[i]);
        }

        // Approach 1: Naive Recursive
        System.out.println("\n" + "-".repeat(80));
        System.out.println("APPROACH 1: Naive Recursive Solution");
        System.out.println("-".repeat(80));
        long startTime = System.nanoTime();
        int result1 = findOptimalCostRecursive(freq, 0, freq.length - 1, 1);
        long endTime = System.nanoTime();
        System.out.printf("Result: %d\n", result1);
        System.out.printf("Time: %.6f ms\n", (endTime - startTime) / 1_000_000.0);
        System.out.println("Complexity: O(n × 2^n) time, O(n) space");

        // Approach 2: Memoization
        System.out.println("\n" + "-".repeat(80));
        System.out.println("APPROACH 2: Memoization (Top-Down DP)");
        System.out.println("-".repeat(80));
        Map<String, Integer> lookup = new HashMap<>();
        startTime = System.nanoTime();
        int result2 = findOptimalCostMemoized(freq, 0, freq.length - 1, 1, lookup);
        endTime = System.nanoTime();
        System.out.printf("Result: %d\n", result2);
        System.out.printf("Time: %.6f ms\n", (endTime - startTime) / 1_000_000.0);
        System.out.printf("Subproblems solved: %d\n", lookup.size());
        System.out.println("Complexity: O(n^4) time, O(n^3) space");

        // Approach 3: Bottom-Up DP
        System.out.println("\n" + "-".repeat(80));
        System.out.println("APPROACH 3: Bottom-Up DP (Tabulation)");
        System.out.println("-".repeat(80));
        startTime = System.nanoTime();
        int result3 = findOptimalCostDP(freq);
        endTime = System.nanoTime();
        System.out.printf("Result: %d\n", result3);
        System.out.printf("Time: %.6f ms\n", (endTime - startTime) / 1_000_000.0);
        System.out.println("Complexity: O(n^4) time, O(n^2) space");

        // Approach 4: Optimized DP
        System.out.println("\n" + "-".repeat(80));
        System.out.println("APPROACH 4: Optimized Bottom-Up DP");
        System.out.println("-".repeat(80));
        startTime = System.nanoTime();
        int result4 = findOptimalCostDPOptimized(freq);
        endTime = System.nanoTime();
        System.out.printf("Result: %d\n", result4);
        System.out.printf("Time: %.6f ms\n", (endTime - startTime) / 1_000_000.0);
        System.out.println("Complexity: O(n^3) time, O(n^2) space");

        // Visualize DP table
        System.out.println("\n" + "-".repeat(80));
        System.out.println("DP TABLE VISUALIZATION");
        System.out.println("-".repeat(80));
        printDPTable(freq);

        // Explanation of the optimal solution
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EXPLANATION OF OPTIMAL SOLUTION");
        System.out.println("=".repeat(80));
        System.out.println("\nFor freq[] = {25, 10, 20}, the optimal BST structure is:");
        System.out.println("\n       1 (freq=10, level=1)");
        System.out.println("      / \\");
        System.out.println("     0   2 (freq=25,20, level=2)");
        System.out.println("\nCost Calculation:");
        System.out.println("  Key 1 at level 1: 10 × 1 = 10");
        System.out.println("  Key 0 at level 2: 25 × 2 = 50");
        System.out.println("  Key 2 at level 2: 20 × 2 = 40");
        System.out.println("  Total Cost: 10 + 50 + 40 = 100");
        System.out.println("\nWhy is this optimal?");
        System.out.println("  - Key 1 (freq=10) is at the root (cheapest level)");
        System.out.println("  - Keys 0 and 2 with higher frequencies are at level 2");
        System.out.println("  - Any other arrangement would increase total cost");
        System.out.println("\n" + "=".repeat(80));
    }
}

