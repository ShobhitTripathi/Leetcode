2264. Largest 3-Same-Digit Number in String

You are given a string num representing a large integer. An integer is good if it meets the following conditions:

It is a substring of num with length 3.
It consists of only one unique digit.
Return the maximum good integer as a string or an empty string "" if no such integer exists.

Note:

A substring is a contiguous sequence of characters within a string.
There may be leading zeroes in num or a good integer.
 

Example 1:
```
Input: num = "6777133339"
Output: "777"
Explanation: There are two distinct good integers: "777" and "333".
"777" is the largest, so we return "777".
```
Example 2:
```
Input: num = "2300019"
Output: "000"
Explanation: "000" is the only good integer.
```
Example 3:
```
Input: num = "42352338"
Output: ""
Explanation: No substring of length 3 consists of only one unique digit. Therefore, there are no good integers.
 ```

Constraints:
```
3 <= num.length <= 1000
num only consists of digits.
```


Solution
```java
class Solution {
    public String largestGoodInteger(String num) {

        // This will store the largest 3-digit same-number substring found so far.
        String ans = "";

        // Loop through the string, but stop at length-3 point
        for (int i = 0; i + 2 < num.length(); i++) {
            
            // Extract the 3 characters
            char a = num.charAt(i);
            char b = num.charAt(i + 1);
            char c = num.charAt(i + 2);

            // Check if all three characters are the same
            if (a == b && b == c) {
                
                // Form the substring of length 3 (e.g., "777", "000")
                String curr = num.substring(i, i + 3);

                // Compare lexicographically to track the maximum good integer
                // For strings of equal length containing digits, lexicographic comparison works.
                if (curr.compareTo(ans) > 0) {
                    ans = curr;
                }
            }
        }

        // If no valid substring found, ans will remain empty string
        return ans;
    }
}

```
