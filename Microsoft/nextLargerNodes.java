class Solution {
    public int[] nextLargerNodes(ListNode head) {
        ArrayList<Integer> A = new ArrayList<>();
        ListNode list = head;
        while (list != null) {
            A.add(list.val);
            list = list.next;
        }
        int[] res = new int[A.size()];
        Stack<Integer> stack = new Stack<>();
        
        for (int i = 0;i < A.size();++i) {
            while (!stack.isEmpty() && A.get(stack.peek()) < A.get(i))
                res[stack.pop()] = A.get(i);
            stack.push(i);
        }
        return res;
    }
}
