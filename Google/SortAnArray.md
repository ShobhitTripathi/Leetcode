# Sort an Array

The purpose of this problem is to evaluate the interviewee's understanding of sorting algorithms and their ability to implement these algorithms without relying on built-in sort methods.

There are a variety of sorting algorithms such as Bubble Sort, Insertion Sort, Selection Sort, Merge Sort, Heap Sort, Quick Sort, Counting Sort, Radix Sort, and others. In this article, we will concentrate on four algorithms that are deemed efficient enough for this particular problem - Merge Sort, Heap Sort, Counting Sort, and Radix Sort.

<img width="3780" height="2268" alt="image" src="https://github.com/user-attachments/assets/0994e4a0-8e76-4e60-b8a5-1026ff8f8773" />


We attached a list of time complexities of some popular sorting algorithms. 
Here, n is the number of elements in the array, k is the size of buckets used, and d is the number maximum of digits of an element in the array.

In this article we will be giving brief descriptions of these algorithms but won't cover them in great detail.
For those who would like to explore these and other sorting algorithms in greater detail, we are providing a link to our Sorting Leetbook.

# Approach 1: Merge Sort

Intuition
```
Merge Sort is a divide-and-conquer sorting algorithm. The intuition behind it is to divide the data set into smaller and smaller sub-arrays until it is easy to sort, and then merge the sorted sub-arrays back into a larger sorted array.

The steps for implementing Merge Sort are as follows:

Divide the data set into two equal parts: The first step in the Merge Sort algorithm is to divide the data set into two equal halves. This is done by finding the middle point of the data set and splitting the data into two parts.

Recursively sort each half: Once the data set is divided into two halves, the Merge Sort function is called recursively on each half. The recursive calls continue until each half of the data is sorted into single-element arrays.

Merge the sorted halves: Once each half of the data is sorted, the two halves are merged back into one final sorted array. The merging process involves comparing the first elements of each half and inserting the smaller element into the final array. This process continues until one of the halves is empty. The remaining elements of the other half are then inserted into the final array.

Repeat the process until the entire data is sorted: The Merge Sort function is called recursively until the entire data set is sorted.
```

<img width="3780" height="2268" alt="image" src="https://github.com/user-attachments/assets/eb245d71-4cb5-4a94-9040-eb19c6d5bfd0" />


Algorithm
```
Create a helper function called merge which takes in the original array arr, indices left, mid, right, and a temporary array tempArr as parameters.

Calculate the start indices and sizes of the two halves of the array. The first half starts from the left index and the second half starts from mid+1.
Copy elements of both halves into the temporary array.
Merge the sub-arrays from the temporary array tempArr back into the original array arr in a sorted order using a while loop. The loop runs until either the first half or second half is completely merged. In each iteration, the smaller of the two elements from the first and second half is copied into the original array "arr".
Copy any remaining elements from the first half or second half into the original array.
Create a recursive function called mergeSort which takes in the original array arr, indices left, right, and a temporary array tempArr as parameters.

Check if the left index is greater than or equal to the right index. If it is, we return from the function.
Calculate the mid index.
Sort the first and second halves of the array recursively by calling the mergeSort function.
Merge the sorted halves by calling the merge function.
Create a temporary array temporaryArray with the same size as the nums array.

Call the mergeSort function on the nums array with boundary, 0, and nums.size()-1.

Return the sorted array nums.
```

Implementation
```java
class Solution {
    // Function to merge two sub-arrays in sorted order.
    private void merge(int[] arr, int left, int mid, int right, int[] tempArr) {
        // Calculate the start and sizes of two halves.
        int start1 = left;
        int start2 = mid + 1;
        int n1 = mid - left + 1;
        int n2 = right - mid;
        
        // Copy elements of both halves into a temporary array.
        for (int i = 0; i < n1; i++) {
            tempArr[start1 + i] = arr[start1 + i];
        }
        for (int i = 0; i < n2; i++) {
            tempArr[start2 + i] = arr[start2 + i];
        }

        // Merge the sub-arrays 'in tempArray' back into the original array 'arr' in sorted order.
        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (tempArr[start1 + i] <= tempArr[start2 + j]) {
                arr[k] = tempArr[start1 + i];
                i += 1;
            } else {
                arr[k] = tempArr[start2 + j];
                j += 1;
            }
            k += 1;
        }

        // Copy remaining elements
        while (i < n1) {
            arr[k] = tempArr[start1 + i];
            i += 1;
            k += 1;
        }
        while (j < n2) {
            arr[k] = tempArr[start2 + j];
            j += 1;
            k += 1;
        }
    }

    // Recursive function to sort an array using merge sort
    private void mergeSort(int[] arr, int left, int right, int[] tempArr) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        // Sort first and second halves recursively.
        mergeSort(arr, left, mid, tempArr);
        mergeSort(arr, mid + 1, right, tempArr);
        // Merge the sorted halves.
        merge(arr, left, mid, right, tempArr);
    }
    
    public int[] sortArray(int[] nums) {
        int[] temporaryArray = new int[nums.length];
        mergeSort(nums, 0, nums.length - 1, temporaryArray);
        return nums;
    }
}

```

Complexity Analysis
```
Here, n is the number of elements in the nums array.

Time complexity: O(nlogn)

We divide the nums array into two halves till there is only one element in the array, which will lead to O(logn) steps.
n→n/2→n/4→...→1 (k steps)
n/2 
(k−1)
 =1⟹ k≈logn
And after each division, we merge those respective halves which will take O(n) time each.
Thus, overall it takes O(nlogn) time.
Space complexity: O(n)

The recursive stack will take O(logn) space and we used an additional array temporaryArray of size n.
Thus, overall we use O(logn+n)=O(n) space.

```



# Approach 2: Heap Sort
Intuition
```
The intuition behind Heap Sort is to organize the elements of the data set into a binary heap (a max binary heap, or a min binary heap), which provides a fast way to access the largest (or smallest) element. A max (or min) binary heap is a complete binary tree-based data structure where a parent node must be greater (or smaller) than or equal to its children nodes. This property ensures that the largest (or smallest) element is always at the root node of the max (or min) binary heap.

The steps for implementing Heap Sort are as follows:

Build the binary heap: Organize the elements of the array into a max binary heap such that the parent node is either greater than or equal to its children nodes. In the resulting max binary heap we will have the largest element at the root node.

Swap the root node and the last element: Swap the root node (which is the largest element) with the last element in the heap. So that we place the largest element at the end, thus, trying to sort in ascending order.

Rebuild the heap: Rebuild the heap with the new root node, and while not considering the already swapped elements from the array to satisfy the heap property.

Repeat steps 2 and 3: Repeat steps 2 and 3 until the binary heap is empty.

Note: The first step of building the binary heap can be done efficiently using the bottom-up approach O(n) time, where the binary heap is built from the bottom-most level to the top-most level. But we will implement top-down heapify in this article as it is easier to understand and implement during an interview setting, but we recommend you to practice implementation of bottom-up heapify also.
```

<img width="3780" height="1224" alt="image" src="https://github.com/user-attachments/assets/8dd532cf-58fa-4003-bf41-65bddae4c303" />

<img width="3780" height="1648" alt="image" src="https://github.com/user-attachments/assets/6b79adbc-0749-4b41-9baf-cf4db60a814c" />


Algorithm
```
Create a function heapify that takes an array arr, size n, and index i as input.

Initialize largest as i.
Calculate the left child of node i as 2 * i + 1 and the right child as 2 * i + 2.
If the left child of node i is less than n and the value of the left child is greater than the value at largest, then set largest to left.
If the right child of node i is less than n and the value of the right child is greater than the value at largest, then set largest to right.
If largest is not equal to i, then swap the values at i and largest, and call heapify on the affected sub-tree rooted at largest.
Create a function heapSort that takes an array arr as input.

Initialize n as the size of the array.
Build the max heap by calling heapify function on each node (except leaf nodes).
Then, traverse the elements of the array arr from end to beginning, and for each element swap the root with the last element and call heapify on the reduced heap to make sure it remains a max heap.
Call heapSort on the array nums.

Return the sorted array nums.
```

Implementation
```java
class Solution {
    private void swap(int[] arr, int index1, int index2) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }
    
    // Function to heapify a subtree (in top-down order) rooted at index i.
    private void heapify(int[] arr, int n, int i) {
        // Initialize largest as root 'i'.
        int largest = i; 
        int left = 2 * i + 1;
        int right = 2 * i + 2; 

        // If left child is larger than root.
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // If right child is larger than largest so far.
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // If largest is not root swap root with largest element
        // Recursively heapify the affected sub-tree (i.e. move down).
        if (largest != i) {
            swap(arr, i, largest); 
            heapify(arr, n, largest);
        }
    }

    private void heapSort(int[] arr) {
        int n = arr.length;
        // Build heap; heapify (top-down) all elements except leaf nodes.
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Traverse elements one by one, to move current root to end, and
        for (int i = n - 1; i >= 0; i--) {
            swap(arr, 0, i);
            // call max heapify on the reduced heap.
            heapify(arr, i, 0);
        }
    }

    public int[] sortArray(int[] nums) {
        heapSort(nums);
        return nums;
    }
}

```

Complexity Analysis
```
Here, n is the number of elements in the nums array.

Time complexity: O(nlogn)

While heapifying the nums array we traverse the height of the complete binary tree made using n elements, which leads to O(logn) time operations, and the heapify is done n times, once for each element.
Thus, overall it takes O(nlogn) time.
Space complexity: O(logn)

The recursive stack will take O(logn) space, the sorting happens in place and we don't use any other additional memory.
```

# Approach 3: Counting Sort
Intuition
```
The intuition behind counting sort is to count the frequency of each element in the input array and then place the elements in their correct positions based on their values and frequencies. Counting sort is a non-comparative sorting algorithm and is useful in situations where the elements in the array have a limited range.

The steps for implementing Counting Sort are as follows:

Create a counting hash map: Create a hash map that maps integers with integers which will be used to store the frequency of each element.

Find the minimum and maximum values: Iterate over the array to be sorted to find the minimum and maximum elements which will be used later on.

Count the frequency of each element: Loop through the array to be sorted and increase the count of the corresponding element in the counting hash map.

Place elements in the original array: Loop through the input array's element's range from the minimum value to the maximum value and place each element in its proper position in the original array based on the frequency in the hash map.
```
<img width="3780" height="2268" alt="image" src="https://github.com/user-attachments/assets/d02acbef-628d-47c1-aab6-d27243118dd3" />


Algorithm
```
Create a function countingSort to sort the input array arr.
Create a counting hash map counts to store the count of each element of the array.
Find the minimum and maximum values minVal and maxVal in the array.
Iterate through the array arr and update the count of each element in the hash map.
Initialize a variable index to zero, which will be used to store the sorted elements in the array arr.
Start a loop that goes from the minimum value minVal to the maximum value maxVal.
For each value val in the loop, check if its count in the hash map counts is greater than zero. If it is, overwrite that value in the array arr starting at the index position. Update the index and decrease the count of the value in the hash map counts by 1.
The input array arr should now be sorted. Return the sorted array.
Calls the countingSort function with nums as a parameter.
Return the sorted array nums.
```

Implementation
```java
class Solution {
    private void countingSort(int[] arr) {
        // Create the counting hash map.
        HashMap<Integer,Integer> counts = new HashMap<>();
        int minVal = arr[0], maxVal = arr[0];
        
        // Find the minimum and maximum values in the array,
        // and update it's count in the hash map.
        for (int i = 0; i < arr.length; i++) {
            minVal = Math.min(minVal, arr[i]);
            maxVal = Math.max(maxVal, arr[i]);
            counts.put(arr[i], counts.getOrDefault(arr[i], 0) + 1);
        }
        
        int index = 0;
        // Place each element in its correct position in the array.
        for (int val = minVal; val <= maxVal; ++val) {
            // Append all 'val's together if they exist.
            while (counts.getOrDefault(val, 0) > 0) {
                arr[index] = val;
                index += 1;
                counts.put(val, counts.get(val) - 1);
            }
        }
    }

    public int[] sortArray(int[] nums) {
        countingSort(nums);
        return nums;
    }
}
```

Complexity Analysis
```
Here, n is the number of elements in the nums array, and k is the range of value of its elements (minimum value to maximum value).

Time complexity: O(n+k)

We iterate on the array elements while counting the frequency and finding minimum and maximum values, thus taking O(n) time.
Then we iterate on the input array's element's range which will take O(k) time.
Thus, overall it takes O(n+k) time.
Space complexity: O(n)

We use a hash map counts which might store all O(n) elements of the input array in worst-case.
```

# Approach 4: Radix Sort

Intuition
```
The intuition behind radix sort is that it takes advantage of the fact that integers have a finite number of digits and each digit can have a limited number of values (0 to 9). Instead of comparing elements, it sorts elements by the individual digits of the integers.

The steps for implementing Radix Sort are as follows:

Sort array using bucket sort: For each place value (unit place to last place) sort the array using counting sort.

Bucket Sort: We need 10 buckets for each digit (0 - 9) and we will push array elements in their respective bucket, and fetch the elements from each bucket one by one in the order it was pushed in the bucket.

Sort considering all elements are positive: Elements in the input array can be both positive or negative, thus we will sort the array considering all elements are positive then we will separate out the negative elements at the end in reversed order.

This approach is not expected by the interviewer and is a bit complex to code during an interview setting,
but we are listing it here to show you how you can use a radix sort on integer arrays.
```

<img width="3780" height="1744" alt="image" src="https://github.com/user-attachments/assets/436a9e7b-5126-46ed-a37b-5732770e57a4" />


Algorithm
```
Create a function bucketSort which takes an array arr and an integer placeValue (indicating the place according to which the array will be sorted) as input.

Create 2D array buckets with 10 rows, to store respective bucket elements together.
Loop through each element in arr, find the digit of the number based on the current place value, and store it in the respective bucket.
Overwrite arr with the elements stored in each bucket in the correct order.
Create a function radixSort which takes an array arr as input.

Find the maximum absolute value maaxElement in arr and find the number of digits maxDigits in the maximum element.
Loop through the digits, starting from the least significant digit place, and call bucketSort for each place value.
Create two arrays negatives and positives and store the negative and positive elements of arr in them.
Reverse the elements in negatives and merge the elements of negatives and positives in arr.
Call function radixSort with nums array as input.

Returns the sorted nums array.
```

Implementation
```java
class Solution {
    // Bucket sort function for each place value digit.
    private void bucketSort(int[] arr, int placeValue) {
        ArrayList<List<Integer>> buckets = new ArrayList<>(10);
        for (int digit = 0; digit < 10; ++digit) {
            buckets.add(digit, new ArrayList<Integer>());
        }
        
        // Store the respective number based on its digit.
        for (int val : arr) {
            int digit = Math.abs(val) / placeValue;
            digit = digit % 10;
            buckets.get(digit).add(val);
        }

        // Overwrite 'arr' in sorted order of current place digits.
        int index = 0;
        for (int digit = 0; digit < 10; ++digit) {
            for (int val : buckets.get(digit)) {
                arr[index] = val;
                index++;
            }
        }
    }
    
    // Radix sort function.
    private void radixSort(int[] arr) {
        // Find the absolute maximum element to find max number of digits.
        int maxElement = arr[0];
        for (int val : arr) {
            maxElement = Math.max(Math.abs(val), maxElement);
        }
        int maxDigits = 0;
        while (maxElement > 0) {
            maxDigits += 1;
            maxElement /= 10;
        }

        // Radix sort, least significant digit place to most significant.
        int placeValue = 1;
        for (int digit = 0; digit < maxDigits; ++digit) {
            bucketSort(arr, placeValue);
            placeValue *= 10;
        }

        // Seperate out negatives and reverse them. 
        ArrayList<Integer> negatives = new ArrayList<>();
        ArrayList<Integer> positives = new ArrayList<>();
        for (int val : arr) {
            if (val < 0) {
                negatives.add(val);
            } else {
                positives.add(val);
            }
        }
        Collections.reverse(negatives);

        // Final 'answer' will be 'negative' elements, then 'positive' elements.
        int index = 0;
        for (int val : negatives) {
            arr[index] = val;
            index++;
        }
        for (int val : positives) {
            arr[index] = val;
            index++;
        }
    }

    public int[] sortArray(int[] nums) {
        radixSort(nums);
        return nums;
    }
}
```

Complexity Analysis
```
Here, n is the number of elements in the nums array, d is the maximum number of digits and b is the size of the bucket used.

Time complexity: O(d⋅(n+b))

We iterate on the array elements to find the maximum number and then find the count of its digits, thus taking O(n+d) time.
Then we sort the array for each integer place which will take O(n+b) time, thus for all d places it will take O(d⋅(n+b)) time.
At the end, we seperate out positive and negative numbers and reverse the negatives, which overall will take O(n) time.
Thus, overall it takes O((n+d)+d⋅(n+b)+n)=O(d⋅(n+b)) time.
Space complexity: O(n+b)

We use additional arrays negatives and positives which use O(n) space and buckets which use O(n+b) space.
```
