# 124. Binary Tree Maximum Path Sum

A path in a binary tree is a sequence of nodes where each pair of adjacent nodes in the sequence has an edge connecting them. A node can only appear in the sequence at most once. Note that the path does not need to pass through the root.

The path sum of a path is the sum of the node's values in the path.

Given the root of a binary tree, return the maximum path sum of any non-empty path.

Example 1:
```
Input: root = [1,2,3]
Output: 6
Explanation: The optimal path is 2 -> 1 -> 3 with a path sum of 2 + 1 + 3 = 6.
```
Example 2:
```
Input: root = [-10,9,20,null,null,15,7]
Output: 42
Explanation: The optimal path is 15 -> 20 -> 7 with a path sum of 15 + 20 + 7 = 42.
```
 
Constraints:
```
The number of nodes in the tree is in the range [1, 3 * 104].
-1000 <= Node.val <= 1000
```


Approach
```
Approach (Post-order DFS)
Use DFS to compute the maximum gain a node can contribute to its parent.
At each node:
  Compute left gain = max(gain_from_left, 0)
  Compute right gain = max(gain_from_right, 0)
  Update global max_sum = max(max_sum, left_gain + right_gain + root.val)
  (this represents the best path through this node).
  Return to parent: root.val + max(left_gain, right_gain) (only one branch can be extended upward).

Algorithm
  Initialize max_sum = -∞.
  Run DFS (gain_from_subtree) starting at root.
  For each node: compute left/right gains, update max_sum, return single-branch gain upward.
  Final answer = max_sum.

Complexity
  Time: O(n) → visit each node once.
  Space: O(h) = O(n) in worst case (skewed tree, recursion stack).

In short: DFS with post-order traversal, track max path sum at each node, return max gain upward.
```

Solution
```java
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        gainFromSubtree(root);
        return maxSum;
    }

    private int maxSum;

    // post order traversal of subtree rooted at 'root'
    private int gainFromSubtree(TreeNode root) {
        if (root == null) {
            return 0;
        }

        // add the path sum from left subtree. Note that if the path
        // sum is negative, we can ignore it, or count it as 0.
        // This is the reason we use `Math.max` here.
        int gainFromLeft = Math.max(gainFromSubtree(root.left), 0);

        // add the path sum from right subtree. 0 if negative
        int gainFromRight = Math.max(gainFromSubtree(root.right), 0);

        // if left or right path sum are negative, they are counted
        // as 0, so this statement takes care of all four scenarios
        maxSum = Math.max(maxSum, gainFromLeft + gainFromRight + root.val);

        // return the max sum for the path starting at the root of subtree
        return Math.max(gainFromLeft + root.val, gainFromRight + root.val);
        
    }
}
```
