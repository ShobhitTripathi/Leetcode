# The Celebrity Problem

A celebrity is a person who is known to all but does not know anyone at a party. A party is being organized by some people. 
A square matrix mat[][] (n*n) is used to represent people at the party such that if an element of row i and column j is set to 1 it means ith person knows jth person. 
You need to return the index of the celebrity in the party, if the celebrity does not exist, return -1.

Note: Follow 0-based indexing.

Examples:
Input: mat[][] = [[1, 1, 0], [0, 1, 0], [0, 1, 1]]
Output: 1
Explanation: 0th and 2nd person both know 1st person. Therefore, 1 is the celebrity person. 

Input: mat[][] = [[1, 1], [1, 1]]
Output: -1
Explanation: Since both the people at the party know each other. Hence none of them is a celebrity person.

Input: mat[][] = [[1]]
Output: 0

Constraints:
1 <= mat.size()<= 1000
0 <= mat[i][j]<= 1
mat[i][i] == 1

```
Approach:
Using Two Pointers - O(n) Time and O(1) Space
The idea is to use two pointers, one from start and one from the end. Assume the start person is A, and the end person is B. If A knows B, then A must not be the celebrity. Else, B must not be the celebrity. At the end of the loop, only one index will be left as a celebrity. Go through each person again and check whether this is the celebrity. 

Follow the steps below to solve the problem:

Create two indices i and j, where i = 0 and j = n-1
Run a loop until i is less than j.
Check if i knows j, then i can't be a celebrity. so increment i, i.e. i++
Else j cannot be a celebrity, so decrement j, i.e. j--
Assign i as the celebrity candidate
Now at last check whether the candidate is actually a celebrity by re-running a loop from 0 to n-1  and constantly checking if the candidate knows a person or if there is a candidate who does not know the candidate.
Then we should return -1. else at the end of the loop, we can be sure that the candidate is actually a celebrity.
```

```java
// Java program to find celebrity

import java.util.*;

class GfG {

    // Function to find the celebrity
    static int celebrity(int[][] mat) {
        int n = mat.length;

        int i = 0, j = n - 1;
        while (i < j) {
            
            // j knows i, thus j can't be celebrity
            if (mat[j][i] == 1)
                j--;
            
            // else i can't be celebrity
            else
                i++;
        }
    
        // i points to our celebrity candidate
        int c = i;
    
        // Check if c is actually
        // a celebrity or not
        for (i = 0; i < n; i++) {
            if (i == c) continue;
            
            // If any person doesn't
            // know 'c' or 'c' doesn't
            // know any person, return -1
            if (mat[c][i] != 0 || mat[i][c] == 0)
                return -1;
        }
    
        return c;
    }
    
    public static void main(String[] args) {
        int[][] mat = { { 0, 1, 0 },
                        { 0, 0, 0 },
                        { 0, 1, 0 } };
        System.out.println(celebrity(mat));
    }
}
```
