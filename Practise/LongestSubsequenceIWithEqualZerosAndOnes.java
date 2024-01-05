/*
Given a binary string S, the task is to find the longest subsequence that has an equal number of 0s and 1s and all the 0s are present before all the 1s.
Input: S = “0011001111”,
Output: 8
Explanation: By removing the 3rd and 4th characters, the string becomes 00001111.
This is the longest possible subsequence following the given conditions.
Input: S = “11001”
Output: 2
Explanation: The longest possible subsequence satisfying the conditions is “01”
 
Input: S = “111100”
Output: 0
Explanation: There is no such subsequence that satisfies the conditions.
*/

public class Main
{
	public static void main(String[] args) {
		String input1 = "0011";
		System.out.println("input : " + input1 + " result : " + getLongestSequence(input1));
		
		String input2 = "0011001111";
		System.out.println("input : " + input2 + " result : " + getLongestSequence(input2));
		
		String input3 = "110011";
		System.out.println("input : " + input3 + " result : " + getLongestSequence(input3));
	}
	
	private static int getLongestSequence(String s) {
	    int zeroCount = 0, oneCount = 0, max = 0;
	    for (char c : s.toCharArray()) {
	        if (c == '0') {
	            zeroCount++;
	        } else {
	           oneCount++;
	        }
	        
	        if (zeroCount == oneCount) {
	                max = Math.max(max, zeroCount+oneCount);
	       }
	    }
	    return max;
	}
}
