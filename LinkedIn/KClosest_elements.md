# 658. Find K Closest Elements

Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result should also be sorted in ascending order.

An integer a is closer to x than an integer b if:

|a - x| < |b - x|, or
|a - x| == |b - x| and a < b
 

Example 1:
Input: arr = [1,2,3,4,5], k = 4, x = 3
Output: [1,2,3,4]

Example 2:
Input: arr = [1,1,2,3,4,5], k = 4, x = -1
Output: [1,1,2,3]

Constraints:
1 <= k <= arr.length
1 <= arr.length <= 104
arr is sorted in ascending order.
-104 <= arr[i], x <= 104

```Algo
Initalize two variables to perform binary search with, left = 0 and right = len(arr) - k.

Perform a binary search. 
At each operation, calculate mid = (left + right) / 2 and compare the two elements located at arr[mid] and arr[mid + k]. 
If the element at arr[mid] is closer to x, then move the right pointer. 
If the element at arr[mid + k] is closer to x, then move the left pointer. 
Remember, the smaller element always wins when there is a tie.

At the end of the binary search, we have located the leftmost index for the final answer. 
Return the subarray starting at this index that contains k elements.

Time complexity: O(log(N−k)+k).
```

```java
class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        // Initialize binary search bounds
        int left = 0;
        int right = arr.length - k;
        
        // Binary search against the criteria described
        while (left < right) {
            int mid = (left + right) / 2;
            if (x - arr[mid] > arr[mid + k] - x) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        // Create output in correct format
        List<Integer> result = new ArrayList<Integer>();
        for (int i = left; i < left + k; i++) {
            result.add(arr[i]);
        }
        
        return result;
    }
}

```

```Note
Although finding the bounds only takes O(log(N−k)) time from the binary search, it still costs us O(k) to build the final output.

Both the Java and Python implementations require O(k) time to build the result. 
However, it is worth noting that if the input array were given as a list instead of an array of integers, 
then the Java implementation could use the ArrayList.subList() method to build the result in O(1) time. 
If this were the case, the Java solution would have an (extremely fast) overall time complexity of O(log(N−k)).
```
