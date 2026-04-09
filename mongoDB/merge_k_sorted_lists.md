# 23. Merge k Sorted Lists
Hard


You are given an array of k linked-lists lists, each linked-list is sorted in ascending order.

Merge all the linked-lists into one sorted linked-list and return it.

 

Example 1:
```
Input: lists = [[1,4,5],[1,3,4],[2,6]]
Output: [1,1,2,3,4,4,5,6]
Explanation: The linked-lists are:
[
  1->4->5,
  1->3->4,
  2->6
]
merging them into one sorted list:
1->1->2->3->4->4->5->6
```
Example 2:
```
Input: lists = []
Output: []
```
Example 3:
```
Input: lists = [[]]
Output: []
``` 

Constraints:

- k == lists.length
- 0 <= k <= 104
- 0 <= lists[i].length <= 500
- -10<sup>4</sup> <= lists[i][j] <= 10<sup>4</sup>
- lists[i] is sorted in ascending order.
- The sum of lists[i].length will not exceed 10<sup>4</sup>.

## Approach
```
- Create a function to merge two sorted lists
- Use it to merge all the given lists
Sub approach 1:
  - we can merge two lists then use the merge list as new list and merge it with next given list
Sub approach 2:
  - we can merge all the lists as a group of 2 lists untill we're left with 1 final list
```

## Solution
```java
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

// This is divide and conquer method - O(nlogk)
class Solution {
    /**
     * Merges an array of k sorted linked lists into one sorted linked list.
     *
     * @param lists Array of ListNode representing the heads of k sorted linked lists
     * @return The head of the merged sorted linked list
     */
    public ListNode mergeKLists(ListNode[] lists) {
        // If the input list is null or empty, return null
        if (lists == null || lists.length == 0) {
            return null;
        }
        // Use divide-and-conquer to merge the lists
        return divide(lists, 0, lists.length - 1);
    }

    /**
     * Recursively divides the list of linked lists into halves until one or two lists remain,
     * then merges them back together.
     *
     * @param lists Array of ListNode representing the heads of the linked lists
     * @param l     Left index of the current range of lists
     * @param r     Right index of the current range of lists
     * @return The head of the merged sorted linked list for the range [l, r]
     */
    private ListNode divide(ListNode[] lists, int l, int r) {
        // If the left index exceeds the right, no lists to merge, return null
        if (l > r) {
            return null;
        }
        // If there is only one list in the range, return it
        if (l == r) {
            return lists[l];
        }
        // Find the midpoint of the current range
        int mid = l + (r - l) / 2;
        // Recursively divide the left half and right half
        ListNode left = divide(lists, l, mid);
        ListNode right = divide(lists, mid + 1, r);
        // Merge the two halves
        return conquer(left, right);
    }

    /**
     * Merges two sorted linked lists into one sorted linked list.
     *
     * @param l1 Head of the first sorted linked list
     * @param l2 Head of the second sorted linked list
     * @return The head of the merged sorted linked list
     */
    private ListNode conquer(ListNode l1, ListNode l2) {
        // Create a dummy node to act as the starting point of the merged list
        ListNode dummy = new ListNode(0);
        // Pointer to track the current position in the merged list
        ListNode curr = dummy;

        // Merge nodes from l1 and l2 in sorted order
        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                // If l1's value is smaller, add it to the merged list
                curr.next = l1;
                l1 = l1.next;
            } else {
                // If l2's value is smaller, add it to the merged list
                curr.next = l2;
                l2 = l2.next;
            }
            // Move the pointer to the next position in the merged list
            curr = curr.next;
        }

        // If there are remaining nodes in l1 or l2, append them to the merged list
        if (l1 != null) {
            curr.next = l1;
        } else {
            curr.next = l2;
        }

        // Return the merged list starting from the first real node
        return dummy.next;
    }
}



// Solution 2

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        // Edge case: no lists
        if (lists.length == 0) {
            return null;
        }

        // Min-heap to always get the smallest node among k lists
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>(
            (a, b) -> a.val - b.val
        );

        // Add the head of each non-null list to the heap
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }

        // Dummy node to simplify result list construction
        ListNode res = new ListNode(0);
        ListNode curr = res;

        // Process nodes in ascending order
        while (!minHeap.isEmpty()) {
            // Extract smallest node
            ListNode node = minHeap.poll();

            // Append to result
            curr.next = node;
            curr = curr.next;

            // Push next node from same list into heap
            if (node.next != null) {
                minHeap.offer(node.next);
            }
        }

        // Return merged list (skip dummy)
        return res.next;
    }   
}

```

## Complexity Analysis
```
- Time complexity : O(Nlogk) where k is the number of linked lists.
    We can merge two sorted linked list in O(n) time where nn is the total number of nodes in two lists.
    Sum up the merge process and we can get: O(Nlogk)
- Space complexity : O(logk) for recursive call stack
  We can merge two sorted linked lists in O(1) space.
```
