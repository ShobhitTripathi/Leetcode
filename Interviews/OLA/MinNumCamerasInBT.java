// Minimum number of cameras required to monitor all nodes of a Binary Tree


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
