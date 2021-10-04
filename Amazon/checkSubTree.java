package Amazon;

import TreesAndGraphs.BinaryTreeNode;
import TreesAndGraphs.TreeOperation;


public class checkSubTree {

    private static boolean containsTree(BinaryTreeNode root, BinaryTreeNode subRoot) {
        if (subRoot == null) {
            return false;
        }
        return isSubTree(root, subRoot);
    }

    private static boolean isSubTree(BinaryTreeNode root, BinaryTreeNode subRoot) {
        if (root == null) {
            return false;
        }
        if (root.val == subRoot.val && isSameTree(root, subRoot)) {
            return true;
        }
        return isSubTree(root.left, subRoot) || isSubTree(root.right, subRoot);
    }

    private static boolean isSameTree(BinaryTreeNode a, BinaryTreeNode b) {
        if (a == null  && b == null) {
            return true;
        }
        else if (a == null || b == null) {
            return false;
        }
        else if (a.val != b.val) {
            return false;
        }
        return isSameTree(a.left, b.left) && isSameTree(a.right, b.right);
    }


    public static void main (String[] args) {
        /*    4
            2    7
          1  3  5  6
         */
        BinaryTreeNode root1 = new BinaryTreeNode(4);
        root1.left = new BinaryTreeNode(2);
        root1.left.left = new BinaryTreeNode(1);
        root1.left.right = new BinaryTreeNode(3);
        root1.right = new BinaryTreeNode(7);
        root1.right.left = new BinaryTreeNode(5);
        root1.right.right = new BinaryTreeNode(8);

        TreeOperation to = new TreeOperation();
        to.show(root1);

        System.out.println("method1:");
        BinaryTreeNode temp1 = root1.left;
        System.out.println("check for subtree: " + temp1.val);
        System.out.println("containsTree(" + root1.val + ", " + temp1.val + ") : " + containsTree(root1, root1.left));
//        System.out.println();
//        System.out.println("method2:");
//        BinaryTreeNode temp2 = root1.right;
//        System.out.println("check for subtree: " + temp2.val);
//        System.out.println("isSubTree(" + root1.val + ", " + temp2.val + ") : "+ isSubTree(root1, root1.right));
    }



}
