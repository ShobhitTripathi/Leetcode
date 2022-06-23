// Minimum number of cameras required to monitor all nodes of a Binary Tree


/*
Approach: The given problem can be solved by storing the states of the nodes whether the camera has been placed or not or the node is monitored by any other node having the camera or not. The idea is to perform the DFS Traversal on the given tree and return the states of each node in each recursive call. Consider the following conversion as the states returned by the function:

If the value is 1, the node is monitored.
If the value is 2, the node is not monitored.
If the value is 3, the node has the camera.

Follow the steps below to solve the problem:

Initialize a variable, say count to store the minimum number of the camera required to monitor all the nodes of the given tree.
Create a function, say dfs(root) that takes the root of the given tree and returns the status of each node whether the camera has been placed or not, or the node is monitored by any other node having the camera and perform the following steps:
If the value of the node is NULL, then return 1 as the NULL node is always monitored.
Recursively call for the left and the right subtree and store the value return by them in the variables L and R.
If the value of L and R is 1 then return 2 from the current recursive call as the current root node is not monitored.
If the value of L or R is 2 then increment the value of count by 1 as one of the left and the right node is not monitored and return 3.
Otherwise, return 1.
Call the above recursive function from the root and if the value returned by it is 2, then increment the value of count by 1.
After completing the above steps, print the value of count as the resultant number of cameras.
*/


public class GFG {
    // TreeNode class
    static class Node {
        public int key;
        public Node left, right;
    };
 
    static Node newNode(int key)
    {
        Node temp = new Node();
        temp.key = key;
        temp.left = temp.right = null;
        return temp;
    }
 
    // Stores the minimum number of
    // cameras required
    static int cnt = 0;
 
    // Utility function to find minimum
    // number of cameras required to
    // monitor the entire tree
    static int minCameraSetupUtil(Node root)
    {
        // If root is NULL
        if (root == null)
            return 1;
 
        int L = minCameraSetupUtil(root.left);
        int R = minCameraSetupUtil(root.right);
 
        // Both the nodes are monitored
        if (L == 1 && R == 1)
            return 2;
 
        // If one of the left and the
        // right subtree is not monitored
        else if (L == 2 || R == 2) {
            cnt++;
            return 3;
        }
 
        // If the root node is monitored
        return 1;
    }
 
    // Function to find the minimum number
    // of cameras required to monitor
    // entire tree
    static void minCameraSetup(Node root)
    {
        int value = minCameraSetupUtil(root);
 
        // Print the count of cameras
        System.out.println(cnt + (value == 2 ? 1 : 0));
    }
 
    // Driver code
    public static void main(String[] args)
    {
        // Given Binary Tree
        Node root = newNode(0);
        root.left = newNode(0);
        root.left.left = newNode(0);
        root.left.left.left = newNode(0);
        root.left.left.left.right = newNode(0);
 
        minCameraSetup(root);
    }
}
