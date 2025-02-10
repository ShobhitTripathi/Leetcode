package atlassian.codeDS;
/*
        Company
      /           \\
    HR               Engg
  /   \\          /          \\
 Mona  Springs  BE          FE
                /  \\        / \\
              Alice Bob  Lisa Marley
Imagine you are the team that maintains the Atlassian employee directory.
At Atlassian - there are multiple groups, and each can have one or more groups. Every employee is part of a group.
You are tasked with designing a system that could find the closest common group given a target set of employees.

getCommonGroupForEmployees (
  currentGroup = Company,
  targetEmployees = listOf(Lisa,Marley)
) -> FE

getCommonGroupForEmployees (
  currentGroup = Company,
  targetEmployees = listOf(Alice, Marley)
) -> Engg

getCommonGroupForEmployees (
  currentGroup = Company,
  targetEmployees = listOf(Mona,Lisa)
) -> Compan
 */


import java.util.ArrayList;
import java.util.List;

public class Solution {

    private static TreeNode getCommonGroupForEmployees(TreeNode currentGroup, List<String> targetEmployees) {

        if (currentGroup == null) {
            return null;
        }

        // If the current node is one of the target employees, return it
        if (targetEmployees.contains(currentGroup.getName())) {
            return currentGroup;
        }

        int count = 0; // Count of target employees found in the children
        TreeNode tempResult = null;

        // Recurse for all children
        for (TreeNode child : currentGroup.getChildren()) {
            TreeNode result = getCommonGroupForEmployees(child, targetEmployees);
            if (result != null) {
                count++;
                tempResult = result; // Store the last found node
            }
        }

        // If more than one child returned a non-null result, this is the LCA
        if (count > 1) {
            return currentGroup;
        }

        // Otherwise, return the result from the recursive calls
        return tempResult;
    }
    
    public static void main(String[] args) {
        // Build the tree structure
        TreeNode company = new TreeNode("Company");

        TreeNode hr = new TreeNode("HR");
        TreeNode engg = new TreeNode("Engg");

        TreeNode mona = new TreeNode("Mona");
        TreeNode springs = new TreeNode("Springs");

        TreeNode be = new TreeNode("BE");
        TreeNode fe = new TreeNode("FE");

        TreeNode alice = new TreeNode("Alice");
        TreeNode bob = new TreeNode("Bob");
        TreeNode lisa = new TreeNode("Lisa");
        TreeNode marley = new TreeNode("Marley");

        company.setChildren(List.of(hr, engg));
        hr.setChildren(List.of(mona, springs));
        engg.setChildren(List.of(be, fe));
        be.setChildren(List.of(alice, bob));
        fe.setChildren(List.of(lisa, marley));

        // Test cases
        List<String> targetEmployees1 = List.of("Lisa", "Marley");
        System.out.println(getCommonGroupForEmployees(company, targetEmployees1).getName()); // Output: FE

        List<String> targetEmployees2 = List.of("Alice", "Marley");
        System.out.println(getCommonGroupForEmployees(company, targetEmployees2).getName()); // Output: Engg

        List<String> targetEmployees3 = List.of("Mona", "Lisa");
        System.out.println(getCommonGroupForEmployees(company, targetEmployees3).getName()); // Output: Company
    }

    // This fucntion is not protper
    private static void getAllParents(String name, TreeNode root, List<String> list) {
        if (root == null) {
            return;
        }
//        TreeNode current = root;
        if (root.getName().equals(name)) {
            return;
        }
        // traverse for all the childs
        if (root.getChildren().size() != 0) {
            for (int i = 0; i< root.getChildren().size(); i++) {
                list.add(root.getName());
                getAllParents(name, root.getChildren().get(i), list);
            }

        }
        list.remove(root.getName());
    }
}
