package Amazon;

import LinkedLists.ListNode;

import static LinkedLists.ListNode.printList;

public class MergeSortedLinkedLists {

    public static ListNode<Integer> mergeSortedLinkedList(ListNode<Integer> l1, ListNode<Integer> l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode<Integer> result = new ListNode<>(-1);
        ListNode<Integer> traverse = result;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                traverse.next = l1;
                l1 = l1.next;
            } else {
                traverse.next = l2;
                l2 = l2.next;
            }
            traverse = traverse.next;
        }

        if (l1 != null) {
            traverse.next = l1;
        }
        if (l2 != null) {
            traverse.next = l2;
        }
        return result.next;
    }

    public static void main(String[] args) {
        ListNode<Integer> list1 = new ListNode<>(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(3);
        list1.next.next.next = new ListNode(4);
        list1.next.next.next.next = new ListNode(5);
        list1.next.next.next.next.next = new ListNode(6);

        printList(list1);

        ListNode<Integer> list2 = new ListNode<>(1);
        list2.next = new ListNode(2);
        list2.next.next = new ListNode(3);
        list2.next.next.next = new ListNode(4);
        list2.next.next.next.next = new ListNode(5);
        list2.next.next.next.next.next = new ListNode(6);

        printList(list2);

        ListNode<Integer> resultList = mergeSortedLinkedList(list1, list2);
        System.out.println("After merge List: ");
        printList(resultList);
    }


}
