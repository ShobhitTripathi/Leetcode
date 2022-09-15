/******************************************************************************
You have infinite stack, Exhanges : 2 from first stack adds 1 to next stack.
Continue till you have 1 element left in all the stack
Give the sum of all the 1's in the stack
*******************************************************************************/

class ListNode {
    public int val;
    public ListNode next;
    
    public ListNode() {}
    
    public ListNode(int val) {
        this.val = val;
        this.next = null;
    }
    
    public static ListNode addAll(int[] A, ListNode result) {
        for (int i = 0;i < A.length;i++) {
            result.next = new ListNode(A[i]);
            result = result.next;
        }
        return result.next;
    }
    
    public static void printList(ListNode head) {
        ListNode temp = head;
        while (temp != null) {
            System.out.println(temp.val);
            temp = temp.next;
        }
    }
}

public class Main
{
    
    private static int solution(int[] A) {
        ListNode originalList = new ListNode();
        originalList.addAll(A, originalList);
        ListNode list = originalList;
        
        
        while (list != null) {
            if (list.val == 0 || list.val == 1) {
                list = list.next;
            } else {
                if (list.next != null) {
                    int temp = list.val / 2;
                    list.val = list.val % 2;
                    list.next.val = list.next.val + temp;
                } else if (list.next == null && list.val > 1){
                    list.next = new ListNode(list.val / 2);
                    list.val = list.val % 2;
                } else {
                    break;
                }
                list = list.next;
            }
           
        }
        
        ListNode temp = originalList;
        int result = 0;
        
        while (temp != null) {
            if (temp.val == 1) {
                result++;
            }
            temp = temp.next;
        }
        return result;
    }
    
    
	public static void main(String[] args) {
		int[] A = {1, 0, 4, 1};
		int result = solution(A);
		System.out.println("For Stacks {1, 0, 4, 1}  result = " +result);
		
		int B[] = {2, 3};
		int result1 = solution(B);
		System.out.println("For Stacks {2, 3}  result = " +result1);
	}
}
