Problem Statement
```
1644. Lowest Common Ancestor of a Binary Tree II
Medium
Topics
conpanies icon
Companies
Hint
Given the root of a binary tree, return the lowest common ancestor (LCA) of two given nodes, p and q. If either node p or q does not exist in the tree, return null. All values of the nodes in the tree are unique.

According to the definition of LCA on Wikipedia: "The lowest common ancestor of two nodes p and q in a binary tree T is the lowest node that has both p and q as descendants (where we allow a node to be a descendant of itself)". A descendant of a node x is a node y that is on the path from node x to some leaf node.

 

Example 1:


Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
Output: 3
Explanation: The LCA of nodes 5 and 1 is 3.
Example 2:



Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
Output: 5
Explanation: The LCA of nodes 5 and 4 is 5. A node can be a descendant of itself according to the definition of LCA.
Example 3:



Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 10
Output: null
Explanation: Node 10 does not exist in the tree, so return null.
```

Solution
```java


class Solution {

    class Result {
        TreeNode node;
        int count;

        Result(TreeNode node, int count) {
            this.node = node;
            this.count = count;
        }
    }

    public TreeNode lowestCommonAncestor(
            TreeNode root, TreeNode p, TreeNode q) {

        Result result = dfs(root, p, q);

        return result.count == 2 ? result.node : null;
    }

    private Result dfs(TreeNode root, TreeNode p, TreeNode q) {

        // Base case
        if (root == null) {
            return new Result(null, 0);
        }

        // Search left and right subtrees
        Result left = dfs(root.left, p, q);
        Result right = dfs(root.right, p, q);

        // Count target nodes found
        int count = left.count + right.count;

        if (root == p || root == q) {
            count++;
        }

        // Determine LCA
        TreeNode lca;

        if (left.node != null && right.node != null) {
            lca = root;
        } else if (root == p || root == q) {
            lca = root;
        } else {
            lca = left.node != null ? left.node : right.node;
        }

        return new Result(lca, count);
    }
}


```
