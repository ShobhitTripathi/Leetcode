Given an array of strings words and an integer k, return the k most frequent strings.

Return the answer sorted by the frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.

Example 1:
Input: words = ["i","love","leetcode","i","love","coding"], k = 2
Output: ["i","love"]
Explanation: "i" and "love" are the two most frequent words.
Note that "i" comes before "love" due to a lower alphabetical order.

Example 2:
Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
Output: ["the","is","sunny","day"]
Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.




```approach

It's almost the same idea as Max heap, except that:

We need to be careful with the order, considering not only the frequency but the word lexicographically.
The min heap doesn't guarantee the order. 
We need to sort the elements in the heap before returning them or just pop them one by one from the min-heap to our result in order.

```

```java

class Solution {
    public List<String> topKFrequent(String[] words, int k) {
        Map<String, Integer> cnt = new HashMap<>();
        for (String word : words) {
            cnt.put(word, cnt.getOrDefault(word, 0) + 1);
        }
        PriorityQueue<String> h = new PriorityQueue<>(
                (w1, w2) -> cnt.get(w1).equals(cnt.get(w2)) ? w2.compareTo(w1) : cnt.get(w1) - cnt.get(w2));

        for (String word : cnt.keySet()) {
            h.offer(word);
            if (h.size() > k) {
                h.poll();
            }
        }

        List<String> res = new ArrayList<>();
        while (!h.isEmpty()) {
            res.add(h.poll());
        }
        Collections.reverse(res);
        return res;
    }
}

```

```
Time Complexity: O(Nlogk), where N is the length of words. 
We count the frequency of each word in O(N) time, then we add N words to the heap, each in O(logk) time. 
Finally, we pop from the heap up to k times or just sort all elements in the heap as the returned result, which takes O(klogk). 
As k≤N, O(N)+O(Nlogk)+O(klogk)=O(Nlogk)

Space Complexity: O(N), O(N) space is used to store our counter cnt while O(k) space is for the heap.
```
