class ListNode {
    char val;
    ListNode next;
    
    ListNode(char ch) {
        this.val = ch;
        this.next = null;
    }
}

public class Main
{
	public static void main(String[] args) {
		ListNode head = new ListNode('m');
		head.next = new ListNode('a');
		head.next.next = new ListNode('d');
		head.next.next.next = new ListNode('a');
		head.next.next.next.next = new ListNode('m');
		// head.next.next.next.next.next = new ListNode('h');
		
		printLinkedList(head);
		System.out.println();
		System.out.println("Result: " + isPallindromicStringInLinkedList(head));
	}
	
	private static boolean isPallindromicStringInLinkedList(ListNode head) {
	    boolean result = false;
	    if (head == null) {
	        return result;
	    }
	    
	    // get middle node of the list
	    ListNode middle = getMiddleNode(head);
	    System.out.println("middleNode: " + middle.val);
	    ListNode nextOfMiddle = reverseLinkedList(middle);
	    printLinkedList(head);
	    printLinkedList(nextOfMiddle);
	    
	    while (head != null && nextOfMiddle != null) {
	        if (head.val != nextOfMiddle.val) {
	            return result;
	        }
	        head = head.next;
	        nextOfMiddle = nextOfMiddle.next;
	    }
	    return true;
	}
	
	private static ListNode getMiddleNode(ListNode head) {
	    if (head == null) {
	        return null;
	    }
	    ListNode slowPtr = head;
	    ListNode fastPtr = head;
	    
	    while (fastPtr != null && fastPtr.next != null) {
	        slowPtr = slowPtr.next;
	        fastPtr = fastPtr.next.next;
	    }
	    return slowPtr;
	}
	
	private static ListNode reverseLinkedList(ListNode head) {
	    if (head == null) {
	        return null;
	    }
	    
	    ListNode next = null, curr = head, prev = null;
	    
	    while (curr != null) {
	        next = curr.next;
	        curr.next = prev;
	        prev = curr;
	        curr = next;
	    }
	    return head = prev;
	}
	
	private static void printLinkedList(ListNode head) {
	    if (head == null) {
	        return;
	    }
	    System.out.print("List : ");
	    while (head != null) {
	        System.out.print(" " + head.val);
	        head = head.next;
	    }
	    System.out.println();
	}
}
