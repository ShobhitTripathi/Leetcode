# 156. Binary Tree Upside Down

Given the root of a binary tree, turn the tree upside down and return the new root.
You can turn a binary tree upside down with the following steps:

The original left child becomes the new root.
The original root becomes the new right child.
The original right child becomes the new left child.

The mentioned steps are done level by level. It is guaranteed that every right node has a sibling (a left node with the same parent) and has no children.

 
Example 1:
Input: root = [1,2,3,4,5]
Output: [4,5,2,null,null,3,1]

Example 2:
Input: root = []
Output: []

Example 3:
Input: root = [1]
Output: [1]
 
Constraints:
The number of nodes in the tree will be in the range [0, 10].
1 <= Node.val <= 10
Every right node in the tree has a sibling (a left node that shares the same parent).
Every right node in the tree has no children.




```java

public TreeNode upsideDownBinaryTree(TreeNode root) {
    // Base case: if tree is empty or has no left child, return as is
    if (root == null || (root.left == null && root.right == null))
        return root;

    // Recursively process the left subtree; newRoot will be the final root
    TreeNode newRoot = upsideDownBinaryTree(root.left);
    
    // Rewire pointers:
    // Left child's left pointer becomes current node's right child
    root.left.left = root.right;
    // Left child's right pointer becomes the current node
    root.left.right = root;
    
    // Break the original links to avoid cycles
    root.left = null;
    root.right = null;
    
    // Return the new root from recursion
    return newRoot;
}


// solution with matrix iself O(N)
import java.util.Stack;

class GfG {

    static int celebrity(int[][] mat) {
        int n = mat.length;
        Stack<Integer> st = new Stack<>();

        // Step 1: Push all people (0 to n-1) into the stack
        for (int i = 0; i < n; i++)
            st.push(i);

        // Step 2: Find a potential celebrity
        // Keep comparing two people at a time until only one candidate remains
        while (st.size() > 1) {

            int a = st.pop(); // person A
            int b = st.pop(); // person B

            // If A knows B → A cannot be a celebrity, push B back
            if (mat[a][b] != 0) {
                st.push(b);
            }
            // Else, B knows A → B cannot be a celebrity, push A back
            else {
                st.push(a);
            }
        }

        // Step 3: Potential celebrity candidate
        int c = st.pop();

        // Step 4: Verify the candidate
        for (int i = 0; i < n; i++) {
            if (i == c) continue;

            // Celebrity must know NO ONE (row values = 0)
            // and EVERYONE must know the celebrity (column values = 1)
            if (mat[c][i] != 0 || mat[i][c] == 0)
                return -1; // Not a celebrity
        }

        // Step 5: Candidate passes verification → celebrity found
        return c;
    }

    public static void main(String[] args) {
        int[][] mat = {
            { 1, 1, 0 },
            { 0, 1, 0 },
            { 0, 1, 1 }
        };
        System.out.println(celebrity(mat)); // Output: 1
    }
}



```
