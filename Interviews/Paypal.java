import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ListNode {
   int val;
   ListNode next;
   ListNode() {}
   ListNode(int val) { this.val = val; }
   ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

public class Paypal {

    public static String getLongestCommonPrefix (String[] arr) {
        Arrays.sort(arr);
        String firstWord = arr[0];
        String lastWord = arr[arr.length - 1];

        int index = 0;
        while (index < firstWord.length()) {
            if (firstWord.charAt(index) == lastWord.charAt(index)) {
                index++;
            } else {
                break;
            }
        }
        return firstWord.substring(0, index);
    }

    public static void testGetLongestCommonPrefix() {
        System.out.println("test getLongestCommonPrefix.");
        String[] arr = {"flower", "float", "flight", "flying"};
        System.out.println("input:");
        for (String s : arr) {
            System.out.print(s + "  ");
        }
        System.out.println();
        String res = getLongestCommonPrefix(arr);
        System.out.println("output:");
        System.out.println("Longest common prefix is : " + res);
    }

    public static ListNode getMiddle (ListNode head) {
        ListNode slowPtr = head;
        ListNode fastPtr = head;

        while (fastPtr.next != null && fastPtr.next.next != null) {
            slowPtr = slowPtr.next;
            fastPtr = fastPtr.next.next;
        }
        return slowPtr;
    }

    public static void testGetMiddle() {
        System.out.println("Test getMiddle in linkedList.");
        ListNode list = new ListNode(1);
        list.next = new ListNode(2);
        list.next.next = new ListNode(3);
        list.next.next.next = new ListNode(4);
        list.next.next.next.next = new ListNode(5);
        list.next.next.next.next.next = new ListNode(6);

        System.out.println("input:");
        ListNode temp = list;
        while (temp.next != null) {
            System.out.print(temp.val + "->");
            temp = temp.next;
        }
        System.out.println(temp.val);

        System.out.println("output:");
        ListNode res = getMiddle(list);
        System.out.println("Middle node is : " + res.val);
    }


    public static void removeDuplicates (int[] arr) {
        int curr = 1;
        int next = 0;
        int len = arr.length -1;

        while (curr <= len ) {
            if (arr[curr] != arr[next]) {
                arr[++next] = arr[curr];
            }
            curr++;
        }
        while (next < len) {
            arr[++next] = -1;
        }
    }

    public static void testRemoveDuplicates () {
        System.out.println("test remove duplicates");
        int[] arr = {0,0,1,1,1,2,2,3,3,4};
        System.out.println("input:");
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
        removeDuplicates(arr);

        System.out.println("output:");
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
    }

    public static void main (String[] args) {
        testRemoveDuplicates();
        System.out.println();
        testGetMiddle();
        System.out.println();
        testGetLongestCommonPrefix();
    }
}
