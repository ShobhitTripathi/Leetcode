### **Problem: Evaluate an Expression Tree With Error Handling**

You are given a binary expression tree where each node contains either:

* A string representing an operator: `"+"`, `"-"`, `"*"`, `"/"`
* OR a string representing a value (integer)

Your task is to **evaluate the expression represented by the tree**.

However, unlike normal expression tree problems, the tree may contain **invalid data**, including:

1. **Invalid leaf values** (e.g., `"abc"`, `"@"`, `"3x"`)
2. **Invalid operators** (e.g., `"%"`, `"^"`, `"add"`)
3. **Missing children** for an operator node
4. **Division by zero**
5. **Null / empty tree**

### **Requirement**

Return one of the following:

* **The evaluated integer value**, if the tree is valid.
* **A descriptive error message**, if any part of the tree is invalid.

### **Rules**

Evaluation must follow standard arithmetic precedence through the tree structure:

* Each operator node must evaluate its left and right subtrees first (post-order).
* If any subtree returns an error, the evaluation must stop and bubble the error upwards.

### **Examples**

#### Example Tree

```
        (/)
       /   \
     (*)    4
    /   \
   2     (+)
        /   \
       3    (/)
           /   \
          6     2
```

Output:

```
Value = 1
```

---

Approach **

“We evaluate the tree using **post-order DFS**.
If at any node, the value/operator is invalid or children are missing, or division by zero occurs, we return an **error object** instead of a number.”

---

#**Time & Space Complexity**

### **Time Complexity → O(N)**

* Each node is visited exactly once in the DFS.
* Each operation (parse, compare, compute) is O(1).

Where **N = number of nodes** in the tree.

### **Space Complexity → O(H)**

* H = height of the tree (recursion call stack).
* Worst case (skewed tree): O(N)
* Best case (balanced tree): O(log N)

So overall:

```
Time:   O(N)
Space:  O(H)
```


Solution

```java

class Solution {

    // Tree node definition
    static class Node {
        String val;   // can be "+", "-", "*", "/", or number
        Node left;
        Node right;

        Node(String val) {
            this.val = val;
        }
    }

    // Wrapper to return either a value or an error reason
    static class EvalResult {
        boolean isError;
        int value;
        String error;

        static EvalResult success(int value) {
            EvalResult r = new EvalResult();
            r.value = value;
            return r;
        }

        static EvalResult failure(String err) {
            EvalResult r = new EvalResult();
            r.isError = true;
            r.error = err;
            return r;
        }
    }

    public EvalResult evaluate(Node root) {
        if (root == null) {
            return EvalResult.failure("Tree is empty");
        }
        return dfs(root);
    }

    private EvalResult dfs(Node node) {

        // Leaf node
        if (node.left == null && node.right == null) {
            try {
                int value = Integer.parseInt(node.val);
                return EvalResult.success(value);
            } catch (NumberFormatException e) {
                return EvalResult.failure("Invalid leaf value: " + node.val);
            }
        }

        // Non-leaf MUST have two children
        if (node.left == null || node.right == null) {
            return EvalResult.failure("Operator node missing children: " + node.val);
        }

        // Validate operator
        if (!isOperator(node.val)) {
            return EvalResult.failure("Invalid operator: " + node.val);
        }

        // Evaluate left subtree
        EvalResult leftRes = dfs(node.left);
        if (leftRes.isError) return leftRes;

        // Evaluate right subtree
        EvalResult rightRes = dfs(node.right);
        if (rightRes.isError) return rightRes;

        // Apply operator
        int a = leftRes.value;
        int b = rightRes.value;

        switch (node.val) {
            case "+":
                return EvalResult.success(a + b);
            case "-":
                return EvalResult.success(a - b);
            case "*":
                return EvalResult.success(a * b);
            case "/":
                if (b == 0) return EvalResult.failure("Division by zero");
                return EvalResult.success(a / b);
        }

        // Shouldn't reach here
        return EvalResult.failure("Unknown error at node: " + node.val);
    }

    private boolean isOperator(String s) {
        return s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/");
    }

    // --- Testing / example usage ---
    public static void main(String[] args) {
        Solution s = new Solution();

        // Example tree:
        //       (/)
        //      /   \
        //    (*)   (4)
        //   /   \
        // (2)   (+)
        //       / \
        //     (3) (/) 
        //         / \
        //       (6) (2)

        Node root = new Node("/");
        root.left = new Node("*");
        root.right = new Node("4");

        root.left.left = new Node("2");
        root.left.right = new Node("+");

        root.left.right.left = new Node("3");
        root.left.right.right = new Node("/");

        root.left.right.right.left = new Node("6");
        root.left.right.right.right = new Node("2");

        EvalResult ans = s.evaluate(root);

        if (ans.isError) {
            System.out.println("Error: " + ans.error);
        } else {
            System.out.println("Value = " + ans.value);
        }
    }
}


```
