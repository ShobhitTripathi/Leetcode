/*
File System
    * Design a file system with createPath() and get() methods.
    * Handle wildcard characters like * or ? in paths.


Key Points of the Design:

Directory and File Structure:
Represent the file system as a tree structure where each directory or file is a node.
A Trie-like approach works well for managing paths.

createPath():
Create a file or directory at a given path.
Split the path by / and traverse or create nodes as needed.

get():
Retrieve files/directories matching a given path, supporting wildcard characters:
*: Matches zero or more characters.
?: Matches exactly one character.

Wildcard Matching:
Use regex-like matching for paths with * or ?.

Classes in Design:

Node:
Represents a directory or file in the file system.
Stores its name and children nodes (if it’s a directory).

FileSystem:
Manages createPath() and get() operations.
Uses a Node to represent the root directory.

*/


import java.util.*;

public class FileSystem {
    // Node class representing each file or directory
    static class Node {
        String name;
        Map<String, Node> children;
        boolean isFile; // true if this node is a file

        public Node(String name) {
            this.name = name;
            this.children = new HashMap<>();
            this.isFile = false;
        }
    }

    private final Node root;

    public FileSystem() {
        this.root = new Node(""); // Root node
    }

    // Method to create a file or directory at a given path
    public boolean createPath(String path) {
        String[] parts = path.split("/");
        Node current = root;

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];

            // If it's the last part, create the file or directory
            if (i == parts.length - 1) {
                if (current.children.containsKey(part)) {
                    return false; // Path already exists
                }
                Node newNode = new Node(part);
                current.children.put(part, newNode);
                if (part.contains(".")) { // If it looks like a file (e.g., file.txt)
                    newNode.isFile = true;
                }
                return true;
            }

            // Traverse to the next level, creating directories if needed
            if (!current.children.containsKey(part)) {
                current.children.put(part, new Node(part));
            }
            current = current.children.get(part);
        }

        return false; // Path creation failed
    }

    // Method to get files or directories matching a given path with wildcards
    public List<String> get(String path) {
        List<String> result = new ArrayList<>();
        String[] parts = path.split("/");
        search(root, parts, 0, "", result);
        return result;
    }

    private void search(Node node, String[] parts, int index, String currentPath, List<String> result) {
        if (node == null) return;

        // If we've matched the entire path
        if (index == parts.length) {
            result.add(currentPath);
            return;
        }

        String part = parts[index];

        if (part.equals("*")) {
            // Match zero or more directories/files
            for (Node child : node.children.values()) {
                search(child, parts, index + 1, currentPath + "/" + child.name, result);
            }
        } else if (part.contains("?")) {
            // Match exactly one character (e.g., "fi?e" -> matches "file" or "fire")
            for (String childName : node.children.keySet()) {
                if (matchesWildcard(part, childName)) {
                    search(node.children.get(childName), parts, index + 1, currentPath + "/" + childName, result);
                }
            }
        } else {
            // Exact match
            if (node.children.containsKey(part)) {
                search(node.children.get(part), parts, index + 1, currentPath + "/" + part, result);
            }
        }
    }

    // Helper method to match wildcard patterns
    private boolean matchesWildcard(String pattern, String name) {
        int p = 0, n = 0;
        while (p < pattern.length() && n < name.length()) {
            if (pattern.charAt(p) == '?' || pattern.charAt(p) == name.charAt(n)) {
                p++;
                n++;
            } else {
                return false;
            }
        }
        return p == pattern.length() && n == name.length();
    }

    public static void main(String[] args) {
        FileSystem fs = new FileSystem();

        // Create paths
        System.out.println(fs.createPath("/a/b/c")); // true
        System.out.println(fs.createPath("/a/b/d")); // true
        System.out.println(fs.createPath("/a/b/file.txt")); // true

        // Get paths
        System.out.println(fs.get("/a/b/*")); // ["/a/b/c", "/a/b/d", "/a/b/file.txt"]
        System.out.println(fs.get("/a/b/fi?e.txt")); // ["/a/b/file.txt"]
        System.out.println(fs.get("/a/*")); // ["/a/b"]
    }
}
