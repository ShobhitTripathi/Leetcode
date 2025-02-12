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

// ChatGpt Solution
package atlassian.codeDS;

import java.util.*;

// Define the Group class
class Group {
    String name;
    List<Group> subGroups;
    List<String> employees;

    public Group(String name) {
        this.name = name;
        this.subGroups = new ArrayList<>();
        this.employees = new ArrayList<>();
    }
}

public class CommonGroupFinder {

    public static String getCommonGroupForEmployees(Group currentGroup, Set<String> targetEmployees) {
        if (currentGroup == null) {
            return null;
        }

        // Check how many target employees are in the current group
        int countInCurrentGroup = 0;
        for (String employee : targetEmployees) {
            if (currentGroup.employees.contains(employee)) {
                countInCurrentGroup++;
            }
        }

        // If all target employees are in the current group, it is the common group
        if (countInCurrentGroup == targetEmployees.size()) {
            return currentGroup.name;
        }

        // Check subgroups
        String commonSubGroup = null;
        int matchedSubGroups = 0;

        for (Group subGroup : currentGroup.subGroups) {
            String result = getCommonGroupForEmployees(subGroup, targetEmployees);
            if (result != null) {
                matchedSubGroups++;
                commonSubGroup = result;
            }
        }

        // If exactly one subgroup contains all the target employees, return that subgroup
        if (matchedSubGroups == 1) {
            return commonSubGroup;
        }

        // If multiple subgroups or no subgroups match, the current group is the lowest common group
        if (countInCurrentGroup + matchedSubGroups > 0) {
            return currentGroup.name;
        }

        return null;
    }

    public static void printGroup(Group group, int level) {
        if (group == null) {
            return;
        }

        // Indent to show hierarchy level
        System.out.println("  ".repeat(level) + "Group: " + group.name);

        // Print employees in this group
        if (!group.employees.isEmpty()) {
            System.out.println("  ".repeat(level + 1) + "Employees: " + group.employees);
        }

        // Recursively print subgroups
        for (Group subGroup : group.subGroups) {
            printGroup(subGroup, level + 1);
        }
    }

    public static void main(String[] args) {
        // Create the hierarchy
        Group company = new Group("Company");

        Group hr = new Group("HR");
        Group engg = new Group("Engg");
        company.subGroups.add(hr);
        company.subGroups.add(engg);

        Group be = new Group("BE");
        Group fe = new Group("FE");
        engg.subGroups.add(be);
        engg.subGroups.add(fe);

//        Group mona = new Group("Mona");
//        Group springs = new Group("Springs");
//        hr.subGroups.add(mona);
//        hr.subGroups.add(springs);

        be.employees.add("Alice");
        be.employees.add("Bob");

        fe.employees.add("Lisa");
        fe.employees.add("Marley");

        hr.employees.add("Mona");
        hr.employees.add("Springs");

        // Print the hierarchy
        System.out.println("Company Hierarchy:");
        printGroup(company, 0);

        // Test the function
//        Set<String> targetEmployees1 = new HashSet<>(Arrays.asList("Lisa", "Marley"));
//        System.out.println("\nCommon group for Lisa and Marley: " + getCommonGroupForEmployees(company, targetEmployees1)); // Expected: FE
//
//        Set<String> targetEmployees2 = new HashSet<>(Arrays.asList("Alice", "Marley"));
//        System.out.println("Common group for Alice and Marley: " + getCommonGroupForEmployees(company, targetEmployees2)); // Expected: Engg

        Set<String> targetEmployees3 = new HashSet<>(Arrays.asList("Mona", "Lisa"));
        System.out.println("Common group for Mona and Lisa: " + getCommonGroupForEmployees(company, targetEmployees3)); // Expected: Company
    }
}





// My Attemp to solution
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
