# Find the Celebrity

Suppose you are at a party with n people labeled from 0 to n - 1 and among them, there may exist one celebrity. The definition of a celebrity is that all the other n - 1 people know the celebrity, but the celebrity does not know any of them.

Now you want to find out who the celebrity is or verify that there is not one. You are only allowed to ask questions like: "Hi, A. Do you know B?" to get information about whether A knows B. You need to find out the celebrity (or verify there is not one) by asking as few questions as possible (in the asymptotic sense).

You are given an integer n and a helper function bool knows(a, b) that tells you whether a knows b. Implement a function int findCelebrity(n). There will be exactly one celebrity if they are at the party.

Return the celebrity's label if there is a celebrity at the party. If there is no celebrity, return -1.

Note that the n x n 2D array graph given as input is not directly available to you, and instead only accessible through the helper function knows. graph[i][j] == 1 represents person i knows person j, wherease graph[i][j] == 0 represents person j does not know person i.

 

Example 1:
Input: graph = [[1,1,0],[0,1,0],[1,1,1]]
Output: 1
Explanation: There are three persons labeled with 0, 1 and 2. graph[i][j] = 1 means person i knows person j, otherwise graph[i][j] = 0 means person i does not know person j. The celebrity is the person labeled as 1 because both 0 and 2 know him but 1 does not know anybody.

Example 2:
Input: graph = [[1,0,1],[1,1,0],[0,1,1]]
Output: -1
Explanation: There is no celebrity.
 
Constraints:
n == graph.length == graph[i].length
2 <= n <= 100
graph[i][j] is 0 or 1.
graph[i][i] == 1
 

Follow up: If the maximum number of allowed calls to the API knows is 3 * n, could you find a solution without exceeding the maximum number of calls?

```
Approach:
Our algorithm firstly narrows the people down to a single celebrityCandidate using the algorithm just above,
 and then it checks whether or not that candidate is a celebrity using the isCelebrity(...).
```

```java
// O(N)

private boolean knows(int a, int b) {
  return graph[a][b] == 1;
}

public class Solution extends Relation {
    
    private int numberOfPeople;
    
    public int findCelebrity(int n) {
        numberOfPeople = n;
        int celebrityCandidate = 0;
        for (int i = 0; i < n; i++) {
            if (knows(celebrityCandidate, i)) {
                celebrityCandidate = i;
            }
        }
        if (isCelebrity(celebrityCandidate)) {
            return celebrityCandidate;
        }
        return -1;
    }
    
    private boolean isCelebrity(int i) {
        for (int j = 0; j < numberOfPeople; j++) {
            if (i == j) continue; // Don't ask if they know themselves.
            if (knows(i, j) || !knows(j, i)) {
                return false;
            }
        }
        return true;
    }
}

// solution with matrix iself O(N)
import java.util.Stack;

class GfG {

    static int celebrity(int[][] mat) {
        int n = mat.length;
        Stack<Integer> st = new Stack<>();

        // Step 1: Push all people (0 to n-1) into the stack
        for (int i = 0; i < n; i++)
            st.push(i);

        // Step 2: Find a potential celebrity
        // Keep comparing two people at a time until only one candidate remains
        while (st.size() > 1) {

            int a = st.pop(); // person A
            int b = st.pop(); // person B

            // If A knows B → A cannot be a celebrity, push B back
            if (mat[a][b] != 0) {
                st.push(b);
            }
            // Else, B knows A → B cannot be a celebrity, push A back
            else {
                st.push(a);
            }
        }

        // Step 3: Potential celebrity candidate
        int c = st.pop();

        // Step 4: Verify the candidate
        for (int i = 0; i < n; i++) {
            if (i == c) continue;

            // Celebrity must know NO ONE (row values = 0)
            // and EVERYONE must know the celebrity (column values = 1)
            if (mat[c][i] != 0 || mat[i][c] == 0)
                return -1; // Not a celebrity
        }

        // Step 5: Candidate passes verification → celebrity found
        return c;
    }

    public static void main(String[] args) {
        int[][] mat = {
            { 1, 1, 0 },
            { 0, 1, 0 },
            { 0, 1, 1 }
        };
        System.out.println(celebrity(mat)); // Output: 1
    }
}
```



