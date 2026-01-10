# 715. Range Module

A Range Module is a module that tracks ranges of numbers. Design a data structure to track the ranges represented as half-open intervals and query about them.

A half-open interval [left, right) denotes all the real numbers x where left <= x < right.

Implement the RangeModule class:
```
RangeModule() Initializes the object of the data structure.
void addRange(int left, int right) Adds the half-open interval [left, right), tracking every real number in that interval. Adding an interval that partially overlaps with currently tracked numbers should add any numbers in the interval [left, right) that are not already tracked.
boolean queryRange(int left, int right) Returns true if every real number in the interval [left, right) is currently being tracked, and false otherwise.
void removeRange(int left, int right) Stops tracking every real number currently being tracked in the half-open interval [left, right).
```

Example 1:
```
Input
["RangeModule", "addRange", "removeRange", "queryRange", "queryRange", "queryRange"]
[[], [10, 20], [14, 16], [10, 14], [13, 15], [16, 17]]
Output
[null, null, null, true, false, true]

Explanation
RangeModule rangeModule = new RangeModule();
rangeModule.addRange(10, 20);
rangeModule.removeRange(14, 16);
rangeModule.queryRange(10, 14); // return True,(Every number in [10, 14) is being tracked)
rangeModule.queryRange(13, 15); // return False,(Numbers like 14, 14.03, 14.17 in [13, 15) are not being tracked)
rangeModule.queryRange(16, 17); // return True, (The number 16 in [16, 17) is still being tracked, despite the remove operation)
 ```

Constraints:
```
1 <= left < right <= 109
At most 104 calls will be made to addRange, queryRange, and removeRange.
```

Intution
```
“Using a TreeMap of start → end lets us directly merge, split, and query intervals using floor and ceiling keys"


How Each Operation Works:
addRange
  Merge all overlapping intervals
  Insert one merged interval

queryRange
  Check the interval whose start is ≤ left
  Verify it covers right

removeRange
  Possibly split an overlapping interval
  Remove fully covered intervals
  Reinsert leftovers
```

Complexity Analysis
```
Let k be the number of affected intervals.
Time Complexity
addRange → O(k log n)
queryRange → O(log n)
removeRange → O(k log n)
```

Solution
```
class RangeModule {

    // start -> end (intervals are non-overlapping)
    private TreeMap<Integer, Integer> map;

    public RangeModule() {
        map = new TreeMap<>();
    }

    // Add range [left, right)
    public void addRange(int left, int right) {

        // Find interval with start <= left
        Integer start = map.floorKey(left);

        // If overlapping, merge
        if (start != null && map.get(start) >= left) {
            left = start;
            right = Math.max(right, map.get(start));
            map.remove(start);
        }

        // Merge all overlapping intervals
        while (true) {
            Integer next = map.ceilingKey(left);
            if (next == null || next > right) break;
            right = Math.max(right, map.get(next));
            map.remove(next);
        }

        map.put(left, right);
    }

    // Query if [left, right) is fully covered
    public boolean queryRange(int left, int right) {
        Integer start = map.floorKey(left);
        return start != null && map.get(start) >= right;
    }

    // Remove range [left, right)
    public void removeRange(int left, int right) {

        Integer start = map.floorKey(left);

        // If overlapping interval exists
        if (start != null && map.get(start) > left) {
            int end = map.get(start);
            map.remove(start);

            // Left remaining part
            if (start < left) {
                map.put(start, left);
            }

            // Right remaining part
            if (right < end) {
                map.put(right, end);
            }
        }

        // Remove fully covered intervals
        Integer next = map.ceilingKey(left);
        while (next != null && next < right) {
            int end = map.get(next);
            map.remove(next);

            if (right < end) {
                map.put(right, end);
                break;
            }
            next = map.ceilingKey(left);
        }
    }
}

```

