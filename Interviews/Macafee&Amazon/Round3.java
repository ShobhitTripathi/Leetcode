import java.util.*;

 class ListNode {
    int data;
    ListNode next;

     public ListNode(int data) {
         this.data = data;
     }
 }

public class Round3 {
    public static void deleteLinkedList(ListNode head) {
        ListNode temp = head;
        int count = 0;
        ListNode prev = null;
        while (getSize(temp) >= 1) {
            count++;
            if (count == 3) {
                prev.next = temp.next;
                count = 0;
                System.out.println("value deleted:" + temp.data);
                temp = temp.next;
            } else {
                prev = temp;
                temp = temp.next;
            }
        }
    }

    private static int getSize(ListNode head) {
        int len = 0;
        ListNode temp = head;
        while (temp.next != head) {
            len++;
            temp = temp.next;
        }
        return len;
    }


    public static void main(String[] args) {
        ListNode head = new ListNode(1); //count 1
        head.next = new ListNode(2); // count 2
//        head.next.next = new ListNode(3); // count 3
        head.next.next.next = new ListNode(4); // count 0
        head.next.next.next.next = new ListNode(5);
        head.next.next.next.next.next = new ListNode(6);
        head.next.next.next.next.next.next = new ListNode(7);
        head.next.next.next.next.next.next.next = head;

        deleteLinkedList(head);
//        System.out.println(head.data);

    }
}
