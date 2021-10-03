package Amazon;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class TopKfrequentElements {

    public static int[] getTopElements(int[] arr, int k) {
        // base check
        if (arr.length == k)
            return arr;
        Map<Integer, Integer> map = new HashMap<>();

        // 1. build hash map : character and how often it appears
        // O(N) time
        for (int n : arr) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        //init heap to keep less frequent elements first
        Queue<Integer> heap = new PriorityQueue<Integer>(
                (n1, n2) -> map.get(n1) - map.get(n2));

        // 2. keep k top frequent elements in the heap
        // O(N log k) < O(N log N) time
        for (int n : map.keySet()) {
            heap.add(n);
            if (heap.size() > k) {
                heap.poll();
            }
        }

        // 3. build an output array
        // O(k log k) time
        int[] top = new int[k];
        for(int i = k - 1; i >= 0; --i) {
            top[i] = heap.poll();
        }

        return top;
    }

    public static void main(String[] args) {
        int[] arr = {1, 1, 1, 1, 2 ,2 ,2, 3 ,3 ,3 , 4, 4, 5, 5 ,5 ,5 ,5};
        printArray(arr);
        int k = 3;
        int[] res = getTopElements(arr, k);
        System.out.println("top " + k + " frequent elements: ");
        printArray(res);
    }

    private static void printArray(int[] arr) {
        for (int i = 0;i < arr.length;i++) {
                System.out.print(" " + arr[i]);
            }
            System.out.println();
    }
}
