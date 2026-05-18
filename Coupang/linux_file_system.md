# Problem Statement

Design a very simple in-memory Linux/File System simulator that supports the following commands:

```text
mkdir <directory_name>   -> create directory
ls                       -> list files/directories
cat <file_name>          -> print file content
```

You need to write Java code that mimics the behavior of these Linux commands.

Assume:

* Everything is maintained in memory.
* No actual OS file system interaction is needed.
* We can have files and directories.
* `cat` works only on files.

---

# Approach

Use Object-Oriented Design.

We create:

## 1. Base Class: `FileSystemNode`

Represents both:

* File
* Directory

Common property:

```java
name
```

---

## 2. File Class

Contains:

```java
content
```

---

## 3. Directory Class

Contains:

```java
Map<String, FileSystemNode>
```

This stores all files/subdirectories inside it.

---

# Operations

## mkdir

Create a new directory and store it inside current directory.

---

## ls

Print all files/directories present.

---

## cat

If the file exists and is a file, print its content.

---

# Java Solution

```java
import java.util.*;

abstract class FileSystemNode {
    String name;

    FileSystemNode(String name) {
        this.name = name;
    }
}

// Represents a file
class FileNode extends FileSystemNode {
    String content;

    FileNode(String name, String content) {
        super(name);
        this.content = content;
    }
}

// Represents a directory
class DirectoryNode extends FileSystemNode {

    // Stores files/directories inside current directory
    Map<String, FileSystemNode> children;

    DirectoryNode(String name) {
        super(name);
        children = new HashMap<>();
    }
}

class FileSystem {

    // Root directory
    private DirectoryNode root;

    public FileSystem() {
        root = new DirectoryNode("/");
    }

    // mkdir command
    public void mkdir(String dirName) {

        if (root.children.containsKey(dirName)) {
            System.out.println("Directory already exists");
            return;
        }

        root.children.put(dirName, new DirectoryNode(dirName));
    }

    // create file helper
    public void createFile(String fileName, String content) {

        if (root.children.containsKey(fileName)) {
            System.out.println("File already exists");
            return;
        }

        root.children.put(fileName, new FileNode(fileName, content));
    }

    // ls command
    public void ls() {

        if (root.children.isEmpty()) {
            System.out.println("Empty directory");
            return;
        }

        for (String name : root.children.keySet()) {
            System.out.println(name);
        }
    }

    // cat command
    public void cat(String fileName) {

        if (!root.children.containsKey(fileName)) {
            System.out.println("File not found");
            return;
        }

        FileSystemNode node = root.children.get(fileName);

        // cat works only for files
        if (node instanceof FileNode) {
            FileNode file = (FileNode) node;
            System.out.println(file.content);
        } else {
            System.out.println(fileName + " is a directory");
        }
    }

    public static void main(String[] args) {

        FileSystem fs = new FileSystem();

        fs.mkdir("docs");
        fs.mkdir("images");

        fs.createFile("readme.txt", "Welcome to the file system");

        System.out.println("LS Output:");
        fs.ls();

        System.out.println("\nCAT Output:");
        fs.cat("readme.txt");
    }
}
```

---

# Sample Output

```text
LS Output:
docs
images
readme.txt

CAT Output:
Welcome to the file system
```

---

# Time Complexity

## mkdir

```text
O(1)
```

(HashMap insertion)

---

## ls

```text
O(N)
```

where `N` is number of files/directories.

---

## cat

```text
O(1)
```

(HashMap lookup)

---

# Follow-up Interview Extensions

Interviewers may ask:

## 1. Support nested paths

Example:

```text
mkdir /a/b/c
cat /docs/readme.txt
```

Use Trie/Tree traversal.

---

## 2. Add commands

```text
pwd
cd
rm
touch
echo
```

---

## 3. Sort ls output

Use:

```java
TreeMap
```

instead of `HashMap`.

---

# Interview Tip

While explaining:

1. Start with tree structure
2. Explain why directory naturally forms a tree
3. Mention O(1) lookup using HashMap
4. Then discuss extensibility (nested paths, permissions, etc.)

That usually creates a strong system-design impression even for a coding round.
