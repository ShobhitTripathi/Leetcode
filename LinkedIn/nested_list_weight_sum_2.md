364. Nested List Weight Sum II

You are given a nested list of integers nestedList. Each element is either an integer or a list whose elements may also be integers or other lists.

The depth of an integer is the number of lists that it is inside of. For example, the nested list [1,[2,2],[[3],2],1] has each integer's value set to its depth. Let maxDepth be the maximum depth of any integer.

The weight of an integer is maxDepth - (the depth of the integer) + 1.

Return the sum of each integer in nestedList multiplied by its weight.

 
Example 1:
Input: nestedList = [[1,1],2,[1,1]]
Output: 8
Explanation: Four 1's with a weight of 1, one 2 with a weight of 2.
1*1 + 1*1 + 2*2 + 1*1 + 1*1 = 8

Example 2:
Input: nestedList = [1,[4,[6]]]
Output: 17
Explanation: One 1 at depth 3, one 4 at depth 2, and one 6 at depth 1.
1*3 + 4*2 + 6*1 = 17
 
Constraints:
1 <= nestedList.length <= 50
The values of the integers in the nested list is in the range [-100, 100].
The maximum depth of any integer is less than or equal to 50.
There are no empty lists.

```approach
Algorithm

Initialize the first level of the BFS tree by adding all the elements in the input nestedList to the queue.
For each level, pop out the front element from the queue.
If it is a list then add its elements into the queue. Otherwise, update the value of sumOfElements, maxDepth and sumOfProducts.
When the queue becomes empty, return the value of (maxDepth + 1) * sumOfElements - sumOfProducts.

TIME COMPLEXITY : O(N)

```

```java

/**
 * // This is the interface that allows for creating nested lists.
 * // You should not implement it, or speculate about its implementation
 * public interface NestedInteger {
 *     // Constructor initializes an empty nested list.
 *     public NestedInteger();
 *
 *     // Constructor initializes a single integer.
 *     public NestedInteger(int value);
 *
 *     // @return true if this NestedInteger holds a single integer, rather than a nested list.
 *     public boolean isInteger();
 *
 *     // @return the single integer that this NestedInteger holds, if it holds a single integer
 *     // Return null if this NestedInteger holds a nested list
 *     public Integer getInteger();
 *
 *     // Set this NestedInteger to hold a single integer.
 *     public void setInteger(int value);
 *
 *     // Set this NestedInteger to hold a nested list and adds a nested integer to it.
 *     public void add(NestedInteger ni);
 *
 *     // @return the nested list that this NestedInteger holds, if it holds a nested list
 *     // Return empty list if this NestedInteger holds a single integer
 *     public List<NestedInteger> getList();
 * }
 */
class Solution {

    public int depthSumInverse(List<NestedInteger> nestedList) {
        int maxDepth = getDepth(nestedList);
        System.out.println(maxDepth);
        return dfs(nestedList, maxDepth, 1);
    }

    private int getDepth(List<NestedInteger> nestedList) {
        int maxDepth = 1;

        for (NestedInteger list : nestedList) {
            if (!list.isInteger() && list.getList().size() > 0) {
                maxDepth = Math.max(maxDepth, 1 + getDepth(list.getList()));
            }
        }
        return maxDepth;
    }

    private int dfs(List<NestedInteger> nestedList, int depth, int index) {
        int total = 0;

        for (NestedInteger list : nestedList) {
            if (list.isInteger()) {
                total += list.getInteger() * (depth - index + 1);
            } else {
                total += dfs(list.getList(), depth, index + 1);
            }
        }

        return total;
    }
}

```
