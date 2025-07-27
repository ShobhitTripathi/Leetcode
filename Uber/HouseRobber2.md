# 337. House Robber III

The thief has found himself a new place for his thievery again. There is only one entrance to this area, called root.

Besides the root, each house has one and only one parent house. After a tour, the smart thief realized that all houses in this place form a binary tree. It will automatically contact the police if two directly-linked houses were broken into on the same night.

Given the root of the binary tree, return the maximum amount of money the thief can rob without alerting the police.

 Example 1:
Input: root = [3,2,3,null,3,null,1]
Output: 7
Explanation: Maximum amount of money the thief can rob = 3 + 3 + 1 = 7.

Example 2:
Input: root = [3,4,5,1,3,null,1]
Output: 9
Explanation: Maximum amount of money the thief can rob = 4 + 5 = 9.
 

Constraints:
The number of nodes in the tree is in the range [1, 104].
0 <= Node.val <= 104

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
    public int rob(TreeNode root) {
        int[] result = robSubtree(root);
        return Math.max(result[0], result[1]);
    }

        private int[] robSubtree(TreeNode node) {
        if (node == null) return new int[2];

        int[] left = robSubtree(node.left);
        int[] right = robSubtree(node.right);

        // If we rob current node, we cannot rob children
        int rob = node.val + left[0] + right[0];

        // If we don't rob current node, we take max of robbing or not robbing children
        int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);

        return new int[]{notRob, rob};
    }
}


```
