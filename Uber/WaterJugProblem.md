# **Key Insight (Mathematical Solution)**

The problem is the classic **Water Jug Problem**, which is solved using the **Bézout's identity** principle:

You can measure exactly `target` liters using jugs of size `x` and `y` **iff**:

1. `target <= x + y`
2. `target` is a **multiple of gcd(x, y)**

Formally:

### **target is reachable ⇔ target % gcd(x, y) == 0 AND target ≤ x + y**

Why?

Because any amount of water you can measure is of the form:

```
a*x + b*y  where a, b are integers (water transfers)
```

And all measurable quantities must be multiples of `gcd(x, y)`.

---

# **Check Examples**

### Example 1

`x=3, y=5, target=4`

```
gcd(3,5) = 1
4 % 1 == 0
4 ≤ 8
→ true
```

### Example 2

`x=2, y=6, target=5`

```
gcd(2,6) = 2
5 % 2 != 0
→ false
```

### Example 3

`x=1, y=2, target=3`

```
gcd(1,2) = 1
3 % 1 == 0
3 ≤ 3
→ true
```

---

# **Optimal Code (Java)**

```java
class Solution {
    public boolean canMeasureWater(int x, int y, int target) {
        if (target > x + y) return false;
        if (target == x || target == y || target == x + y) return true;

        return target % gcd(x, y) == 0;
    }
    
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }
        return a;
    }
}
```

---

# **Time Complexity**

* Computing GCD: **O(log(min(x, y)))**
* Constant extra work.

### **Total Time: O(log(min(x, y)))**

### **Space: O(1)**

---

If you want, I can also provide:

* A BFS/State-space simulation solution
* Step-by-step derivation
* Explanation of why Bézout’s identity works here

Just tell me.
