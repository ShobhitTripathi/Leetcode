/*
    Array
    arr = 2 3 5 7
    arr = 2,3,5,7,2,3,5,3,5,3

    2 2
    3 4
    5 3
    7 1

 */


import java.util.*;

class myComp<T> implements Comparator<Map.Entry<Integer, Integer>> {
    @Override
    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
        return (o2.getValue()).compareTo(o1.getValue());
    }
}

public class Round2 {

    public static int computeArray (int[] arr) {
        int res = 0;
        Map<Integer, Integer> map = new HashMap<>();

        // creating a frequency map
        for (int num : arr) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }

        for (Map.Entry entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // initialize heap
        Queue<Map.Entry<Integer, Integer>> q = new PriorityQueue<>(
                Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // insert into heap
        for (Map.Entry<Integer, Integer> n : map.entrySet()) {
            q.add(n);
        }

        int totalLength = arr.length;
        int half = totalLength / 2;

        while (totalLength >= half) {
            int val = q.poll().getValue();
            res++;
            totalLength = totalLength - val;
        }

        return res;
    }


    private static int usingComparator(int[] arr) {
        Map<Integer, Integer> map = new HashMap<>();

        // update the map with frequency
        for (int n : arr) {
            map.put(n, map.getOrDefault(n, 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> newList = new LinkedList<>(map.entrySet());
        Collections.sort(newList, new myComp());

        for (Map.Entry<Integer, Integer> en : map.entrySet()) {
            System.out.println("key: " + en.getKey() + " value: " + en.getValue());
        }

        for (Map.Entry<Integer, Integer> en : newList) {
            System.out.println("2 .. key: " + en.getKey() + " value: " + en.getValue());
        }

        int totalLength = arr.length;
        int half = totalLength / 2;
        int index = 0;
        int res = 0;

        while (totalLength >= half) {
            int val = newList.get(index).getValue();
            index++;
            res++;
            totalLength = totalLength - val;
        }

        return res;
    }


    public static void main(String[] args) {
        int[] arr = {2,3,5,7,3,3,5,3,3,6};
        int ans = computeArray(arr);
        int res = usingComparator(arr);
        System.out.println("using Heap: number of digits to be removed: " + ans);
        System.out.println("using comparator: number of digits to be removed: " + res);
    }
}
