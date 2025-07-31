# LeetCode 716: Max Stack

## 🧾 Problem Description

Design a stack that supports the following operations:

- `void push(int x)` – Push element `x` onto the stack.
- `int pop()` – Remove the element on top of the stack and return it.
- `int top()` – Get the element on the top.
- `int peekMax()` – Retrieve the maximum element in the stack.
- `int popMax()` – Retrieve the maximum element in the stack, and remove it. If there is more than one maximum element, remove the one closest to the top.

## ✅ Example

```
MaxStack stack = new MaxStack();
stack.push(5);
stack.push(1);
stack.push(5);
stack.top();      // returns 5
stack.popMax();   // returns 5
stack.top();      // returns 1
stack.peekMax();  // returns 5
stack.pop();      // returns 1
stack.top();      // returns 5
```

---

## 🚀 Optimized Java Solution

This solution uses a **Doubly Linked List** for stack-like behavior and a **TreeMap** to efficiently retrieve and remove the maximum value.

### 💡 Time Complexity

| Operation   | Complexity |
|-------------|------------|
| push        | O(log N)   |
| pop         | O(1)       |
| top         | O(1)       |
| peekMax     | O(log N)   |
| popMax      | O(log N)   |

### 🧠 Java Code

```java
import java.util.*;

class MaxStack {

    private static class Node {
        int val;
        Node prev, next;
        Node(int val) { this.val = val; }
    }

    private Node head, tail;
    private TreeMap<Integer, List<Node>> map;

    public MaxStack() {
        head = new Node(0); tail = new Node(0);
        head.next = tail; tail.prev = head;
        map = new TreeMap<>();
    }

    public void push(int x) {
        Node node = new Node(x);
        insert(node);
        map.computeIfAbsent(x, k -> new ArrayList<>()).add(node);
    }

    public int pop() {
        Node node = tail.prev;
        remove(node);
        List<Node> nodes = map.get(node.val);
        nodes.remove(nodes.size() - 1);
        if (nodes.isEmpty()) map.remove(node.val);
        return node.val;
    }

    public int top() {
        return tail.prev.val;
    }

    public int peekMax() {
        return map.lastKey();
    }

    public int popMax() {
        int max = map.lastKey();
        List<Node> nodes = map.get(max);
        Node node = nodes.remove(nodes.size() - 1);
        if (nodes.isEmpty()) map.remove(max);
        remove(node);
        return max;
    }

    private void insert(Node node) {
        node.prev = tail.prev;
        node.next = tail;
        tail.prev.next = node;
        tail.prev = node;
    }

    private void remove(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
```

---

## 🧪 Notes

- TreeMap ensures efficient max element access.
- Doubly Linked List allows O(1) removal of nodes.
- This solution efficiently handles duplicates and max-element removal.

---