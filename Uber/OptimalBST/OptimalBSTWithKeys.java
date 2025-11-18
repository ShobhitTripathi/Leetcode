package Practise;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Optimal Binary Search Tree with Actual Keys
 * 
 * VARIATION: Instead of using indices as keys, we have actual key values.
 * 
 * Problem Statement:
 * Given two arrays:
 * - keys[]: Actual key values in sorted order (e.g., {10, 20, 30})
 * - freq[]: Frequency of each key (e.g., {25, 10, 20})
 * 
 * Find the minimum cost BST where:
 * - Cost = frequency × level (root at level 1)
 * - Keys maintain BST property (left < root < right)
 * 
 * Example:
 * keys[] = {10, 20, 30}
 * freq[] = {25, 10, 20}
 * 
 * Optimal BST:
 *       10 (freq=25, level=1)
 *        \
 *        30 (freq=20, level=2)
 *        /
 *       20 (freq=10, level=3)
 * 
 * Cost = 25×1 + 20×2 + 10×3 = 25 + 40 + 30 = 95
 * 
 * Key Insight:
 * The algorithm remains the same! We just need to:
 * 1. Ensure keys are sorted
 * 2. Use indices for DP computation
 * 3. Map back to actual keys when reconstructing tree
 */
public class OptimalBSTWithKeys {

    /**
     * TreeNode class to represent the actual BST structure
     */
    static class TreeNode {
        int key;
        int frequency;
        int level;
        TreeNode left;
        TreeNode right;

        TreeNode(int key, int frequency, int level) {
            this.key = key;
            this.frequency = frequency;
            this.level = level;
        }

        @Override
        public String toString() {
            return String.format("Key=%d, Freq=%d, Level=%d", key, frequency, level);
        }
    }

    /**
     * Result class containing cost and tree structure
     */
    static class OptimalBSTResult {
        int minCost;
        TreeNode root;
        int[][] costTable;
        int[][] rootTable;

        OptimalBSTResult(int minCost, TreeNode root, int[][] costTable, int[][] rootTable) {
            this.minCost = minCost;
            this.root = root;
            this.costTable = costTable;
            this.rootTable = rootTable;
        }
    }

    /**
     * Main method to find optimal BST with actual keys
     * 
     * Time Complexity: O(n^3) with prefix sum optimization
     * Space Complexity: O(n^2) for DP tables
     * 
     * @param keys Array of actual key values (must be sorted)
     * @param freq Array of frequencies for each key
     * @return OptimalBSTResult containing cost and tree structure
     */
    public static OptimalBSTResult findOptimalBST(int[] keys, int[] freq) {
        // Validate input
        if (keys == null || freq == null || keys.length != freq.length) {
            throw new IllegalArgumentException("Invalid input: keys and freq must have same length");
        }

        // Validate keys are sorted
        for (int i = 1; i < keys.length; i++) {
            if (keys[i] <= keys[i - 1]) {
                throw new IllegalArgumentException("Keys must be in strictly increasing order");
            }
        }

        int n = keys.length;

        // DP tables
        int[][] cost = new int[n][n];
        int[][] root = new int[n][n];

        // Prefix sum for O(1) range sum calculation
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + freq[i];
        }

        // Base case: single keys
        for (int i = 0; i < n; i++) {
            cost[i][i] = freq[i];
            root[i][i] = i;
        }

        // Build DP table for increasing subsequence sizes
        for (int size = 2; size <= n; size++) {
            for (int i = 0; i <= n - size; i++) {
                int j = i + size - 1;
                cost[i][j] = Integer.MAX_VALUE;

                // Range sum in O(1)
                int rangeSum = prefixSum[j + 1] - prefixSum[i];

                // Try each key in range as root
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

        // Reconstruct the actual tree
        TreeNode treeRoot = reconstructTree(keys, freq, root, 0, n - 1, 1);

        return new OptimalBSTResult(cost[0][n - 1], treeRoot, cost, root);
    }

    /**
     * Reconstruct the actual BST from the root table
     * 
     * @param keys Array of key values
     * @param freq Array of frequencies
     * @param rootTable Table storing optimal root choices
     * @param i Start index
     * @param j End index
     * @param level Current level in tree
     * @return Root of the reconstructed tree
     */
    private static TreeNode reconstructTree(int[] keys, int[] freq, int[][] rootTable,
                                           int i, int j, int level) {
        if (j < i) {
            return null;
        }

        // Get the optimal root for this range
        int rootIndex = rootTable[i][j];
        TreeNode node = new TreeNode(keys[rootIndex], freq[rootIndex], level);

        // Recursively build left and right subtrees
        node.left = reconstructTree(keys, freq, rootTable, i, rootIndex - 1, level + 1);
        node.right = reconstructTree(keys, freq, rootTable, rootIndex + 1, j, level + 1);

        return node;
    }

    /**
     * Calculate the actual cost of a tree (for verification)
     */
    public static int calculateTreeCost(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return root.frequency * root.level + 
               calculateTreeCost(root.left) + 
               calculateTreeCost(root.right);
    }

    /**
     * Print the tree structure in a readable format
     */
    public static void printTree(TreeNode root, String prefix, boolean isLeft) {
        if (root == null) {
            return;
        }

        System.out.println(prefix + (isLeft ? "├── " : "└── ") + 
                          "Key: " + root.key + 
                          " (freq=" + root.frequency + 
                          ", level=" + root.level + 
                          ", cost=" + (root.frequency * root.level) + ")");

        if (root.left != null || root.right != null) {
            if (root.left != null) {
                printTree(root.left, prefix + (isLeft ? "│   " : "    "), true);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "├── (null)");
            }

            if (root.right != null) {
                printTree(root.right, prefix + (isLeft ? "│   " : "    "), false);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "└── (null)");
            }
        }
    }

    /**
     * Print tree in level-order (BFS)
     */
    public static void printTreeLevelOrder(TreeNode root) {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int currentLevel = 1;

        System.out.println("\nLevel-Order Traversal:");
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            System.out.print("Level " + currentLevel + ": ");

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                System.out.print("Key=" + node.key + "(freq=" + node.frequency + ") ");

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            System.out.println();
            currentLevel++;
        }
    }

    /**
     * Verify BST property
     */
    public static boolean isBST(TreeNode root, Integer min, Integer max) {
        if (root == null) {
            return true;
        }

        if ((min != null && root.key <= min) || (max != null && root.key >= max)) {
            return false;
        }

        return isBST(root.left, min, root.key) && isBST(root.right, root.key, max);
    }

    /**
     * Print DP tables for visualization
     */
    public static void printDPTables(int[] keys, OptimalBSTResult result) {
        int n = keys.length;

        System.out.println("\n" + "=".repeat(80));
        System.out.println("DP COST TABLE");
        System.out.println("=".repeat(80));
        
        // Header
        System.out.print("       ");
        for (int j = 0; j < n; j++) {
            System.out.printf("Key%-3d ", keys[j]);
        }
        System.out.println();

        // Rows
        for (int i = 0; i < n; i++) {
            System.out.printf("Key%-3d ", keys[i]);
            for (int j = 0; j < n; j++) {
                if (j >= i) {
                    System.out.printf("%6d ", result.costTable[i][j]);
                } else {
                    System.out.print("     - ");
                }
            }
            System.out.println();
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("DP ROOT TABLE (Optimal Root Choices)");
        System.out.println("=".repeat(80));
        
        // Header
        System.out.print("       ");
        for (int j = 0; j < n; j++) {
            System.out.printf("Key%-3d ", keys[j]);
        }
        System.out.println();

        // Rows
        for (int i = 0; i < n; i++) {
            System.out.printf("Key%-3d ", keys[i]);
            for (int j = 0; j < n; j++) {
                if (j >= i) {
                    System.out.printf("Key%-3d ", keys[result.rootTable[i][j]]);
                } else {
                    System.out.print("     - ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Compare with index-based approach
     */
    public static void compareWithIndexBased(int[] keys, int[] freq) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPARISON: Keys vs Indices");
        System.out.println("=".repeat(80));

        // With actual keys
        OptimalBSTResult result1 = findOptimalBST(keys, freq);
        
        // With indices (from original implementation)
        int result2 = OptimalBSTCost.findOptimalCostDPOptimized(freq);

        System.out.println("\nWith Actual Keys:");
        System.out.println("  Keys: " + Arrays.toString(keys));
        System.out.println("  Freq: " + Arrays.toString(freq));
        System.out.println("  Optimal Cost: " + result1.minCost);

        System.out.println("\nWith Indices as Keys:");
        System.out.println("  Keys: [0, 1, 2, ...] (implicit)");
        System.out.println("  Freq: " + Arrays.toString(freq));
        System.out.println("  Optimal Cost: " + result2);

        System.out.println("\n✅ Costs are identical: " + (result1.minCost == result2));
        System.out.println("   (The algorithm is the same, only key labels differ)");
    }

    /**
     * Main method with comprehensive examples
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("OPTIMAL BST WITH ACTUAL KEYS");
        System.out.println("=".repeat(80));

        // Example 1: Basic example with actual keys
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EXAMPLE 1: Basic Keys");
        System.out.println("─".repeat(80));

        int[] keys1 = {10, 20, 30};
        int[] freq1 = {25, 10, 20};

        System.out.println("\nInput:");
        System.out.println("  Keys: " + Arrays.toString(keys1));
        System.out.println("  Freq: " + Arrays.toString(freq1));

        OptimalBSTResult result1 = findOptimalBST(keys1, freq1);

        System.out.println("\nResult:");
        System.out.println("  Minimum Cost: " + result1.minCost);
        System.out.println("  Verified Cost: " + calculateTreeCost(result1.root));
        System.out.println("  Is Valid BST: " + isBST(result1.root, null, null));

        System.out.println("\nOptimal Tree Structure:");
        printTree(result1.root, "", false);

        printTreeLevelOrder(result1.root);

        printDPTables(keys1, result1);

        // Example 2: Different key values
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EXAMPLE 2: Different Key Values");
        System.out.println("─".repeat(80));

        int[] keys2 = {5, 15, 25, 35};
        int[] freq2 = {10, 5, 20, 8};

        System.out.println("\nInput:");
        System.out.println("  Keys: " + Arrays.toString(keys2));
        System.out.println("  Freq: " + Arrays.toString(freq2));

        OptimalBSTResult result2 = findOptimalBST(keys2, freq2);

        System.out.println("\nResult:");
        System.out.println("  Minimum Cost: " + result2.minCost);
        System.out.println("  Verified Cost: " + calculateTreeCost(result2.root));
        System.out.println("  Is Valid BST: " + isBST(result2.root, null, null));

        System.out.println("\nOptimal Tree Structure:");
        printTree(result2.root, "", false);

        printTreeLevelOrder(result2.root);

        // Example 3: Large key values
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EXAMPLE 3: Large Key Values");
        System.out.println("─".repeat(80));

        int[] keys3 = {100, 200, 300};
        int[] freq3 = {25, 10, 20};

        System.out.println("\nInput:");
        System.out.println("  Keys: " + Arrays.toString(keys3));
        System.out.println("  Freq: " + Arrays.toString(freq3));

        OptimalBSTResult result3 = findOptimalBST(keys3, freq3);

        System.out.println("\nResult:");
        System.out.println("  Minimum Cost: " + result3.minCost);

        System.out.println("\nOptimal Tree Structure:");
        printTree(result3.root, "", false);

        // Comparison with index-based approach
        compareWithIndexBased(keys1, freq1);

        // Example 4: Non-sequential keys
        System.out.println("\n" + "─".repeat(80));
        System.out.println("EXAMPLE 4: Non-Sequential Keys");
        System.out.println("─".repeat(80));

        int[] keys4 = {7, 13, 19, 23, 31};
        int[] freq4 = {5, 10, 15, 20, 8};

        System.out.println("\nInput:");
        System.out.println("  Keys: " + Arrays.toString(keys4));
        System.out.println("  Freq: " + Arrays.toString(freq4));

        OptimalBSTResult result4 = findOptimalBST(keys4, freq4);

        System.out.println("\nResult:");
        System.out.println("  Minimum Cost: " + result4.minCost);
        System.out.println("  Is Valid BST: " + isBST(result4.root, null, null));

        System.out.println("\nOptimal Tree Structure:");
        printTree(result4.root, "", false);

        // Key Insight
        System.out.println("\n" + "=".repeat(80));
        System.out.println("KEY INSIGHT");
        System.out.println("=".repeat(80));
        System.out.println("\n✨ The actual key VALUES don't affect the optimal COST!");
        System.out.println("   Only the FREQUENCIES and their ORDER matter.");
        System.out.println("\n   Why? Because the DP algorithm works on indices,");
        System.out.println("   and the cost depends only on frequency × level.");
        System.out.println("\n   The actual keys only matter for:");
        System.out.println("   1. Maintaining BST property (left < root < right)");
        System.out.println("   2. Reconstructing the actual tree structure");
        System.out.println("   3. Search operations in the final BST");
        System.out.println("\n" + "=".repeat(80));
    }
}

