## Problem Statement

Design a stack that supports these operations in **O(1)** time:

```java
push(x)
pop()
top()
getMin()
```

`getMin()` should return the minimum element currently present in the stack.

---

## Approach

Use **two stacks**:

1. `mainStack` → stores all elements.
2. `minStack` → stores the minimum element at every level.

Whenever we push a value:

* Push it into `mainStack`.
* Push the current minimum into `minStack`.

So, the top of `minStack` always contains the minimum value of the current stack.

---

## Java Solution

```java
import java.util.*;

class MinStack {

    // Stores all stack elements
    private Stack<Integer> mainStack;

    // Stores minimum element at each stack level
    private Stack<Integer> minStack;

    public MinStack() {
        mainStack = new Stack<>();
        minStack = new Stack<>();
    }

    public void push(int val) {
        mainStack.push(val);

        // If minStack is empty, val is the current minimum
        if (minStack.isEmpty()) {
            minStack.push(val);
        } else {
            // Store minimum till this level
            minStack.push(Math.min(val, minStack.peek()));
        }
    }

    public void pop() {
        if (mainStack.isEmpty()) {
            return;
        }

        mainStack.pop();
        minStack.pop();
    }

    public int top() {
        if (mainStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }

        return mainStack.peek();
    }

    public int getMin() {
        if (minStack.isEmpty()) {
            throw new RuntimeException("Stack is empty");
        }

        return minStack.peek();
    }

    public static void main(String[] args) {
        MinStack st = new MinStack();

        st.push(5);
        st.push(3);
        st.push(7);
        st.push(2);

        System.out.println(st.getMin()); // 2

        st.pop();
        System.out.println(st.getMin()); // 3

        st.pop();
        System.out.println(st.top());    // 3
        System.out.println(st.getMin()); // 3
    }
}
```

---

## Time Complexity

```text
push()   -> O(1)
pop()    -> O(1)
top()    -> O(1)
getMin() -> O(1)
```

## Space Complexity

```text
O(N)
```

Because we use two stacks.
