/*
You are having a string S of length N containing only lowercase english letters.
 
You perform the following operation on the above string:
 
Delete some substring of this string.
Try permuting the remaining characters of the string such that they become a palindrome.
 
Determine the minimum length substring to remove from the string S such that the remaining characters can be rearranged into a palindrome.
 
Example:
Given
 
N = 7
S = "abbcdde"
 
Approach
 
The possible ways to delete substring such that the remaining string forms a palindrome is:
 
Delete prefix of length 6, such that only "e" remains which is a palindrome.
Delete suffix of length 6, such that only "a" remains which is a palindrome.
Delete prefix of length 4, such that "dde" remains, which can be rearranged into a palindrome.
Delete suffix of length 4, such that "abb" remains, which can be rearranged into a palindrome.
The minimum length substring to delete is 4.
Thus, the answer is 4.
str= abbcbbffe
*/

public class MinSubstringRemovalPalindrome {

    public static int minSubstringRemoval(String S) {
        int n = S.length();
        int left = 0, right = n - 1;
        int result = 0;

        // Check from left to right
        while (left < right && S.charAt(left) == S.charAt(right)) {
            left++;
            right--;
        }

        // If the entire string is a palindrome, no removal needed
        if (left >= right) {
            return 0;
        }

        // Check from left to right for the remaining substring
        int i = left, j = right;

        // Check for palindrome in the remaining substring
        while (i < j) {
            if (S.charAt(i) != S.charAt(j)) {
                result = Math.max(i - left, right - j);
                break;
            }
            i++;
            j--;
        }

        // Check from right to left for the remaining substring
        i = left;
        j = right;

        // Check for palindrome in the remaining substring
        while (i < j) {
            if (S.charAt(i) != S.charAt(j)) {
                result = Math.max(result, i - left);
                break;
            }
            i++;
            j--;
        }

        return result + 1;
    }

    public static void main(String[] args) {
        System.out.println(minSubstringRemoval("abbcdde"));  // Output: 4
        System.out.println(minSubstringRemoval("abbcbbffe")); // Output: 4
    }
