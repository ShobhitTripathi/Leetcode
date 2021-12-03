import java.util.List;
import java.util.Stack;

public class Amazon {
   /*Q1 : There is a n-arry tree given, you need to print spiral order of the tree in alternate manner, toggling the sequence at each iteration.
                5
                        1           3       4
                        2   10  12      12      13  15
                        22              13


    Answer : 5, 4, 3, 1, 2, 10, 12, 12, 13, 15, 13, 22
            --------------------------------------------------------------------------------------------------------------------------------------------

    */

    class NarrTree {
        int val;
        List<NarrTree> children;
    }


    public static void printSpriral(NarrTree node) {
        if (node == null)
            return;

        // right to left
        Stack<NarrTree> s1 = new Stack<>();
        //left to right
        Stack<NarrTree> s2 = new Stack<>();
        s1.push(node);

        while (!s1.isEmpty() || !s2.isEmpty()) {
            while (!s1.isEmpty()) {
                if (s1.peek() == null) {
                    s1.pop();
                    break;
                }
                NarrTree temp = s1.pop();
                System.out.print(temp.val + ", ");

                for (int i = temp.children.size()- 1; i >= 0;i--) {
                    s2.push(temp.children.get(i));
                }
            }

            while(!s2.isEmpty()) {
                if (s2.peek() == null) {
                    s2.pop();
                    break;
                }
                NarrTree temp1 = s2.pop();
                System.out.print(temp1.val + ", ");

                for (int i = 0;i < temp1.children.size();i++) {
                    s1.push(temp1.children.get(i));
                }

            }
        }
    }

    /*TestCase : 0 : Node is null

    TestCase : 1 : Node is 5
            5
            null           null




    Q2 : There is a n-arry tree given, you need to print spiral order of the tree in alternate manner, toggling the sequence at each iteration.
                5
                        1           3       4
                        2   10  12      22      23  15
                        21              13

    Input : nodeList {1, 12}, Given there are not duplicates in tree, find LCA.
    Answer : 5

    Input : nodeList {2, 13}, Given there are not duplicates in tree, find LCA.
    Answer : 1
    */

}
