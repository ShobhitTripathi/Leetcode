# 1. Counting Nodes in an N-ary Tree (Normal Version)

### **Steps**

1. If the root is null, return 0.
2. Count the current node as 1.
3. Recursively count all nodes in each child subtree.
4. Return the total.

### **Code (Simple DFS)**

```java
class Node {
    int val;
    List<Node> children;
}

int countNodes(Node root) {
    if (root == null) return 0;
    int count = 1;
    for (Node child : root.children) {
        count += countNodes(child);
    }
    return count;
}
```

---

# 2. Distributed Version

Each node represents a machine in a distributed system arranged in a tree structure.

Each node must implement:

```
send(id, message)
receive(id, message)
```

Goal:
After all nodes run the protocol, **every node must know the total number of nodes in the system**.

We assume:

* Nodes know their direct parent (except root).
* Nodes know their list of children.
* Messages are reliable.
* No node failures.

We implement a **two-phase protocol**:

---

# Phase 1: Bottom-Up Aggregation

Each node computes the size of its subtree and sends it upward.

### Steps

1. Each node starts with:

   * `pendingChildren = number of children`
   * `sumFromChildren = 0`

2. Leaf node:

   * Immediately sends `1` to its parent

3. Internal node waits until:

   * It receives subtree sizes from all children
   * `pendingChildren == 0`

4. Internal node computes:

   ```
   mySubtree = 1 + sumFromChildren
   ```

5. If it has a parent:

   * It sends `mySubtree` to the parent

6. If it is the root:

   * `totalNodes = mySubtree`
   * Move to Phase 2 (broadcast down)

---

# Phase 2: Top-Down Broadcast

Every node must learn the `totalNodes` value.

### Steps

1. Root sends `totalNodes` to all children.
2. Each child receives it, stores it, and forwards it to its children.
3. Continue until all leaves receive the value.

At the end, **every node knows the total number of nodes**.

---

# Distributed Node Code (Java-Style Pseudocode)

Below is a clean simulation-ready version.

```java
class DistributedNode {

    int id;
    Integer parent;                // null for root
    List<Integer> children;        // child node IDs

    int pendingChildren;           // number of child responses expected
    int sumFromChildren = 0;       // aggregates subtree counts
    Integer finalCount = null;     // final total nodes after both phases

    Map<Integer, DistributedNode> network; // simulate send/receive
                                           // in real system this is a message bus

    public DistributedNode(int id, Integer parent, List<Integer> children,
                           Map<Integer, DistributedNode> network) {
        this.id = id;
        this.parent = parent;
        this.children = children;
        this.network = network;
        this.pendingChildren = children.size();
    }

    // Simulate sending a message to another node
    void send(int targetId, Object msg) {
        network.get(targetId).receive(this.id, msg);
    }

    // Handle incoming messages
    void receive(int from, Object message) {
        // Phase 1: receiving subtree sizes from children
        if (message instanceof Integer && finalCount == null) {
            int childSubtree = (int) message;
            sumFromChildren += childSubtree;
            pendingChildren--;

            // All children reported
            if (pendingChildren == 0) {
                int mySubtree = 1 + sumFromChildren;

                // If I have a parent, send subtree size upward
                if (parent != null) {
                    send(parent, mySubtree);
                } else {
                    // I am the root; I know total node count
                    finalCount = mySubtree;
                    broadcastFinalCount();
                }
            }
        }

        // Phase 2: receiving final total count from parent
        else if (message instanceof TotalCountMessage) {
            TotalCountMessage msg = (TotalCountMessage) message;
            this.finalCount = msg.totalNodes;

            // Forward to children
            for (int child : children) {
                send(child, msg);
            }
        }
    }

    // Root starts Phase 2
    private void broadcastFinalCount() {
        TotalCountMessage msg = new TotalCountMessage(finalCount);
        for (int child : children) {
            send(child, msg);
        }
    }

    static class TotalCountMessage {
        int totalNodes;
        TotalCountMessage(int totalNodes) {
            this.totalNodes = totalNodes;
        }
    }
}
```

---

# How This Works

1. **Leaves send `1`** to their parent.
2. Internal nodes **wait**, add up child subtree sizes, and send the sum upward.
3. Root gets the final subtree size (which is the entire tree size).
4. Root **broadcasts** this count to all children.
5. Each node propagates the final count downward.
6. Every node stores the same final count.

---

# Complexity

### Time Complexity

* Each edge is used once in aggregation and once in broadcast.

```
O(N)
```

### Space Complexity

* State per node + recursion stack:

```
O(N)
```

---
