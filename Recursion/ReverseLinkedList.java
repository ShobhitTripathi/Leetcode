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
class Solution {
    public ListNode reverseList(ListNode head) {
        // base case
        if (head == null || head.next == null) {
            return head;
        }
        // recursive function
        ListNode reversedList = reverseList(head.next);

        // reverse the node
        head.next.next = head;
        head.next = null;

        return reversedList;
    }
}
