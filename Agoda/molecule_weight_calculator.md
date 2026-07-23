This is a classic **Stack + Parsing** problem.

The important observations are:

1. An element may have a count (`H2`).
2. A group may have a multiplier (`(CH4)2`).
3. Parentheses can be nested (`C(H(O2)3)2`).
4. We need to evaluate the innermost group first.

A stack naturally models nested parentheses.

---

# Approach

Maintain a stack where each entry represents the weight accumulated for the current group.

* Push `0` when encountering `(`.
* Add element weights to the current group.
* When encountering `)`, pop the group weight, multiply it by the multiplier (if any), and add it to the previous group.

---

## Example

```
H(CH4)2
```

Initially

```
Stack = [0]
```

### Read `H`

Weight = 1

```
Stack = [1]
```

### Read `(`

Start a new group.

```
Stack = [1, 0]
```

### Read `C`

```
Weight = 12

Stack = [1, 12]
```

### Read `H4`

```
Weight = 1 × 4 = 4

Stack = [1, 16]
```

### Read `)2`

Pop the group.

```
group = 16

group *= 2

group = 32
```

Add to parent.

```
Stack = [33]
```

Answer

```
33
```

---

# Java Solution

```java
import java.util.*;

class Solution {

    static int getWeight(String formula) {

        Map<Character, Integer> weight = new HashMap<>();
        weight.put('H', 1);
        weight.put('C', 12);
        weight.put('O', 8);

        Stack<Integer> stack = new Stack<>();
        stack.push(0); // base group

        int i = 0;

        while (i < formula.length()) {

            char ch = formula.charAt(i);

            // Start of a new group
            if (ch == '(') {
                stack.push(0);
                i++;
            }

            // End of current group
            else if (ch == ')') {

                int groupWeight = stack.pop();
                i++;

                // Read multiplier after ')'
                int multiplier = 0;

                while (i < formula.length() &&
                        Character.isDigit(formula.charAt(i))) {

                    multiplier = multiplier * 10 +
                            (formula.charAt(i) - '0');
                    i++;
                }

                if (multiplier == 0)
                    multiplier = 1;

                groupWeight *= multiplier;

                stack.push(stack.pop() + groupWeight);
            }

            // Atom
            else {

                int atomWeight = weight.get(ch);
                i++;

                // Read atom count
                int count = 0;

                while (i < formula.length() &&
                        Character.isDigit(formula.charAt(i))) {

                    count = count * 10 +
                            (formula.charAt(i) - '0');
                    i++;
                }

                if (count == 0)
                    count = 1;

                stack.push(stack.pop() + atomWeight * count);
            }
        }

        return stack.pop();
    }

    public static void main(String[] args) {

        System.out.println(getWeight("CH4"));      //16

        System.out.println(getWeight("H(CH4)2"));  //33

        System.out.println(getWeight("C(H(O2)3)2"));
    }
}
```

---

# Dry Run

Input

```
C(H(O2)3)2
```

| Character | Stack     | Explanation      |
| --------- | --------- | ---------------- |
| C         | [12]      | Add carbon       |
| (         | [12,0]    | New group        |
| H         | [12,1]    | Add H            |
| (         | [12,1,0]  | New nested group |
| O2        | [12,1,16] | 8 × 2            |
| )3        | [12,49]   | 16 × 3 + 1       |
| )2        | [110]     | 49 × 2 + 12      |

Final answer:

```
110
```

---

# Complexity

Let **N** be the length of the formula.

### Time Complexity

* Every character is processed exactly once.
* Digits are consumed while parsing.

**Time:** `O(N)`

### Space Complexity

* The stack stores one entry per level of nested parentheses.

In the worst case (`((((...))))`):

**Space:** `O(N)`

---

### Interview Thought Process

1. Parse the string from left to right.
2. Element weights contribute directly to the current group.
3. Parentheses create nested groups, suggesting a **stack**.
4. When a group closes, compute its total, apply the multiplier, and merge it into the parent group.
5. Since each character is visited once, the solution runs in **O(N)** time with **O(N)** auxiliary space.
