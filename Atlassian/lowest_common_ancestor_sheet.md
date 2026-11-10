# LCA (Lowest Common Ancestor) – Interview Cheat Sheet

The goal is to understand one core DFS algorithm and how to modify it for different LCA variations.

The most important thing to remember:

> LCA problems are generally solved using bottom-up DFS (postorder traversal). Each recursive call returns useful information to its parent.

# 1. The core LCA algorithm (LeetCode 236)

Must master this template

Problem: Given two nodes `p` and `q`, find their lowest common ancestor in a binary tree.

### Java template

```

public TreeNode lowestCommonAncestor(
        TreeNode root, TreeNode p, TreeNode q) {

    // 1. Base condition
    if (root == null || root == p || root == q) {
        return root;
    }

    // 2. Search left and right subtrees
    TreeNode left = lowestCommonAncestor(root.left, p, q);
    TreeNode right = lowestCommonAncestor(root.right, p, q);

    // 3. Both sides found a target
    if (left != null && right != null) {
        return root;
    }

    // 4. Propagate the result upwards
    return left != null ? left : right;
}

```

### Understand the 4 rules

\| Condition | Meaning | Return |\
\|---|---|---|\
\| `root == null` | Nothing found | `null` |\
\| `root == p || root == q` | Target found | `root` |\
\| `left != null && right != null` | Targets on both sides | `root` |\
\| Only one side non-null | Target/LCA found below | Non-null side |

### Visual intuition

[【怒刷力扣】二叉树的公共祖先类题目，搞懂搜索整棵树和搜索单条边的区别，轻松手撕算法题-腾讯云开发者社区-腾讯云](https://images.openai.com/static-rsc-4/x1XY0BsPIBVPNUoPDeK96NO6f5Ougwg04DmvLjoxF-xOXaMoA0QR362_fNTTLJ1h5lIq9Wc3UE474htnv9-Rrdv8Uj6paVbPAe-Ew_5R3DM-X-OOH900RLUgvGBImYlIbzaJIB24yNjwo3VEisiKVHKgBWiXwM-O5wrxnCGLnso?purpose=inline)

[cloud.tencent.com](https://cloud.tencent.com/developer/article/2604227?utm_source=chatgpt.com)

For `p = 6` and `q = 4`:

1. Node 6 returns itself.
2. Node 4 returns itself.
3. Node 2 receives `null` from left and `4` from right. It returns `4`.
4. Node 5 receives `6` from left and `4` from right. It returns `5`.

Therefore, LCA = `5`.

# 2. How the algorithm propagates (MOST IMPORTANT)

This is the concept that makes all LCA variations easier.

Think of recursion as a message-passing system.

Each node sends one result to its parent.

Bottom-up propagation

Node 6 returns `6`

Node 4 returns `4`

Node 2

Left = null, Right = 4

Returns `4`

Node 5

Left = 6, Right = 4

Both non-null → Returns `5` (LCA)

Remember: The recursive function does not necessarily return the current node. It returns either:

- A target node.
- The LCA already found in a subtree.
- `null` if nothing was found.

This is the fundamental concept.

# 3. How LCA propagates across different questions

This is your most useful interview reference.

| LeetCode | Variation                           | What changes from 236?                |
| -------- | ----------------------------------- | ------------------------------------- |
| 235      | LCA in BST                          | Use BST properties instead of DFS     |
| 236      | Normal binary tree                  | Original template                     |
| 1650     | Parent pointer available            | Two-pointer technique                 |
| 1676     | Multiple target nodes               | HashSet + same DFS                    |
| 1644     | Nodes may not exist                 | Track whether both targets were found |
| 1123     | Deepest leaves                      | Return node + depth                   |
| 865      | Smallest subtree with deepest nodes | Same node + depth pattern             |

Let's understand each modification.

## Pattern A: LCA in BST – LeetCode 235

Change: No need for DFS on both sides. Use BST properties.

```

public TreeNode lowestCommonAncestor(
        TreeNode root, TreeNode p, TreeNode q) {

    if (p.val < root.val && q.val < root.val) {
        return lowestCommonAncestor(root.left, p, q);
    }

    if (p.val > root.val && q.val > root.val) {
        return lowestCommonAncestor(root.right, p, q);
    }

    return root;
}

```

Remember:

- Both smaller → left.
- Both greater → right.
- Split or equal → current node is LCA.

Complexity: O(H) time, O(H) recursion space.

## Pattern B: Parent Pointer – LeetCode 1650

Change: Each node has a `parent` reference.

Use the linked-list intersection technique instead of recursive DFS.

```

public Node lowestCommonAncestor(Node p, Node q) {

    Node a = p;
    Node b = q;

    while (a != b) {
        a = (a == null) ? q : a.parent;
        b = (b == null) ? p : b.parent;
    }

    return a;
}

```

Remember:

- No DFS.
- No HashSet.
- Two pointers.
- Redirect to the other starting node when reaching null.

Complexity: O(H1 + H2) time, O(1) space.

## Pattern C: Multiple target nodes – LeetCode 1676

Change: Instead of two targets, we have an array of targets.

Use a HashSet for target lookup.

```

public TreeNode lowestCommonAncestor(
        TreeNode root, TreeNode[] nodes) {

    Set<TreeNode> set = new HashSet<>();

    for (TreeNode node : nodes) {
        set.add(node);
    }

    return dfs(root, set);
}

private TreeNode dfs(TreeNode root, Set<TreeNode> set) {

    if (root == null || set.contains(root)) {
        return root;
    }

    TreeNode left = dfs(root.left, set);
    TreeNode right = dfs(root.right, set);

    if (left != null && right != null) {
        return root;
    }

    return left != null ? left : right;
}

```

Remember:

- Replace `root == p || root == q` with `set.contains(root)`.
- Everything else remains the same.

Complexity: O(N + K) time, O(K + H) space.

## Pattern D: Nodes may not exist – LeetCode 1644

Change: Unlike 236, we cannot assume both target nodes exist.

We must verify that both were found.

One clean approach is to return a result containing the candidate LCA and a count of targets found.

```

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

        if (root == null) {
            return new Result(null, 0);
        }

        Result left = dfs(root.left, p, q);
        Result right = dfs(root.right, p, q);

        int count = left.count + right.count;

        if (root == p || root == q) {
            count++;
        }

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

Remember:

- Track the number of targets found.
- Return the LCA only if count = 2.

Complexity: O(N) time, O(H) recursion space.

## Pattern E: Deepest Leaves – LeetCode 1123 and 865

Change: There are no explicitly given target nodes.

Instead, we need the LCA of all deepest leaves.

Return two pieces of information from every recursive call:

1. The LCA candidate.
2. The maximum depth.

```

class Solution {

    class Result {
        TreeNode node;
        int depth;

        Result(TreeNode node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }

    public TreeNode lcaDeepestLeaves(TreeNode root) {
        return dfs(root).node;
    }

    private Result dfs(TreeNode root) {

        if (root == null) {
            return new Result(null, 0);
        }

        Result left = dfs(root.left);
        Result right = dfs(root.right);

        if (left.depth == right.depth) {
            return new Result(root, left.depth + 1);
        }

        if (left.depth > right.depth) {
            return new Result(left.node, left.depth + 1);
        }

        return new Result(right.node, right.depth + 1);
    }
}

```

Remember:

- Equal depths → current node is LCA.
- Left deeper → propagate left result.
- Right deeper → propagate right result.

Complexity: O(N) time, O(H) space.

# 4. Quick decision-making cheat sheet

When you see an LCA problem, ask these questions:

## Which LCA algorithm should I use?

1\. Is it a Binary Search Tree?

Yes → BST traversal

Use node values to decide left, right, or current node.

2\. Does each node have a parent pointer?

Yes → Two pointers

Use linked-list intersection logic.

3\. Are there two target nodes in a normal binary tree?

Yes → Recursive DFS

Use the standard 236 template.

4\. Are there multiple target nodes?

Yes → HashSet + DFS

Use the 1676 variation.

5\. Can target nodes be missing?

Yes → DFS + count

Verify that all targets were found.

6\. Are we looking for deepest leaves?

Yes → DFS + depth

Return both candidate node and depth.

# 5. Complexity cheat sheet

Let `N` = number of tree nodes, `H` = tree height, and `K` = number of target nodes.

| Pattern               | Time       | Auxiliary space |
| --------------------- | ---------- | --------------- |
| Binary Tree (236)     | O(N)       | O(H)            |
| BST (235)             | O(H)       | O(H) recursive  |
| Parent Pointer (1650) | O(H1 + H2) | O(1)            |
| Multiple Nodes (1676) | O(N + K)   | O(H + K)        |
| Missing Nodes (1644)  | O(N)       | O(H)            |
| Deepest Leaves (1123) | O(N)       | O(H)            |

# 6. Your revision checklist

Use this to make sure you have mastered LCA.

LCA mastery tracker

0/9 completed

Understand the 4 base cases of LCA 236

Write LCA 236 without looking at the solution

Explain how left and right results propagate upwards

Solve LCA 235 using BST properties

Solve LCA 1650 using two pointers

Modify 236 for multiple nodes (1676)

Modify 236 to handle missing nodes (1644)

Understand returning node + depth (1123)

Solve 865 independently

## My final advice

For interviews, spend 70% of your learning time on these three things:

1. LeetCode 236: Understand recursion and propagation.
2. LeetCode 1676: Understand how to extend an algorithm to multiple targets.
3. LeetCode 1644: Understand how to handle additional constraints without breaking the original algorithm.

