/*
Problem-:
Minimum number of rabbits that must be present in the forest

There are some colored rabbits in a forest. Given an array arr[] of size N, 
such that arr[i] denotes the number of rabbits having same color as the ith rabbit, t
he task is to find the minimum number of rabbits that could be in the forest.

Examples:

Input: arr[] = {2, 2, 0}
Output: 4
Explanation: Considering the 1st and the 2nd rabbits to be of same color, eg. Blue, t
here should be 3 blue-colored rabbits. The third rabbit is the only rabbit of that color. 
Therefore, the minimum number of rabbits that could be present in the forest are = 3 + 1 = 4.

Input: arr[] = {10, 10, 10}
Output: 11
Explanation: Considering all the rabbits to be of the same color, the minimum number of rabbits present in forest are 10 + 1 = 11.



Approach: The approach to solving this problem is to find the number of groups of rabbits that have the same color and the number of rabbits in each group. Below are the steps:

Initialize a variable count to store the number of rabbits in each group.
Initialize a map and traverse the array having key as arr[i] and value as occurrences of arr[i] in the given array.
Now, if y rabbits answered x, then:
If (y%(x + 1)) is 0, then there must be (y / (x + 1)) groups of (x + 1) rabbits.
If (y % (x + 1)) is non-zero, then there must be (y / (x + 1)) + 1 groups of (x + 1) rabbits.
Add the product of the number of groups and the number of rabbits in each group to the variable count.
After the above steps, the value of count gives the minimum number of rabbits in the forest.

*/


// Java program for the above approach

import java.util.*;

class GFG {

	// Function to find the minimum
	// number of rabbits in the forest
	public static int minNumberOfRabbits(int[] answers,
										int N)
	{
		// Initialize map
		Map<Integer, Integer> map
			= new HashMap<Integer, Integer>();

		// Traverse array and map arr[i]
		// to the number of occurences
		for (int a : answers) {
			map.put(a, map.getOrDefault(a, 0) + 1);
		}

		// Initialize count as 0;
		int count = 0;

		// Find the number groups and
		// no. of rabbits in each group
		for (int a : map.keySet()) {
			int x = a;
			int y = map.get(a);

			// Find number of groups and
			// multiply them with number
			// of rabbits in each group
			if (y % (x + 1) == 0) {
				count = count + (y / (x + 1)) * (x + 1);
			}
			else
				count
					= count + ((y / (x + 1)) + 1) * (x + 1);
		}

		// count gives minimum number
		// of rabbits in the forest
		return count;
	}

	// Driver Code
	public static void main(String[] args)
	{
		int arr[] = { 2, 2, 0 };
		int N = arr.length;

		// Function Call
		System.out.println(minNumberOfRabbits(arr, N));
	}
}
