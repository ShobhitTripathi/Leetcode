# 432. All O`one Data Structure
Hard
Topics
Companies
Design a data structure to store the strings' count with the ability to return the strings with minimum and maximum counts.

Implement the AllOne class:

AllOne() Initializes the object of the data structure.
inc(String key) Increments the count of the string key by 1. If key does not exist in the data structure, insert it with count 1.
dec(String key) Decrements the count of the string key by 1. If the count of key is 0 after the decrement, remove it from the data structure. It is guaranteed that key exists in the data structure before the decrement.
getMaxKey() Returns one of the keys with the maximal count. If no element exists, return an empty string "".
getMinKey() Returns one of the keys with the minimum count. If no element exists, return an empty string "".
Note that each function must run in O(1) average time complexity.

 

Example 1:

Input
["AllOne", "inc", "inc", "getMaxKey", "getMinKey", "inc", "getMaxKey", "getMinKey"]
[[], ["hello"], ["hello"], [], [], ["leet"], [], []]
Output
[null, null, null, "hello", "hello", null, "hello", "leet"]

Explanation
AllOne allOne = new AllOne();
allOne.inc("hello");
allOne.inc("hello");
allOne.getMaxKey(); // return "hello"
allOne.getMinKey(); // return "hello"
allOne.inc("leet");
allOne.getMaxKey(); // return "hello"
allOne.getMinKey(); // return "leet"
 

Constraints:

1 <= key.length <= 10
key consists of lowercase English letters.
It is guaranteed that for each call to dec, key is existing in the data structure.
At most 5 * 104 calls will be made to inc, dec, getMaxKey, and getMinKey.


 
```
Key Logic -:

To design this data structure with 
O(1) average time complexity for all operations,
we can use a combination of a HashMap and a Double Linked List.

Here's the strategy:

Key Concepts
HashMap (keyCount): To store the count of each string.

Double Linked List (countBucket):
  To store buckets of keys grouped by their counts.
  This allows us to efficiently get the minimum and maximum counts.

Each node in the linked list represents a bucket of keys that share the same count.
The list is sorted by count in ascending order.

Class Implementation

inc(String key):
Increment the count of the key in keyCount.
Move the key to the next bucket in the countBucket.
If the current bucket becomes empty, remove it.

dec(String key):
Decrement the count of the key in keyCount.
Move the key to the previous bucket in the countBucket.
If the current bucket becomes empty, remove it.

getMaxKey() and getMinKey():
Retrieve the first or last key from the linked list.

```



```java
public class Bucket {
    int count;
    Set<String> keys;
    Bucket prev, next;

    Bucket(int count) {
        this.count = count;
        this.keys = new HashSet<>();
    }
}

class AllOne {
    private Map<String, Integer> keyCount;
    private Map<Integer, Bucket> countBucket;
    private Bucket head, tail;

    public AllOne() {
        this.keyCount = new HashMap<>();
        this.countBucket = new HashMap<>();
        this.head = new Bucket(0);
        this.tail = new Bucket(0);
        head.next = tail;
        tail.prev = head;
    }
    
    public void inc(String key) {
        int count = keyCount.getOrDefault(key, 0);
        keyCount.put(key, count + 1);

        Bucket currentBucket = countBucket.get(count);
        Bucket nextBucket = countBucket.get(count + 1);

        // move the key to the next bucket in the countBucket.
        if (nextBucket == null) {
            nextBucket = new Bucket(count + 1);
            if (currentBucket == null) {
                addBucketAfter(head, nextBucket);
            } else {
                addBucketAfter(currentBucket, nextBucket);
            }
            countBucket.put(count + 1, nextBucket);
        }
        nextBucket.keys.add(key);

        // remove key from the currentbucket
        if (currentBucket != null) {
            currentBucket.keys.remove(key);
            if (currentBucket.keys.isEmpty()) {
                removeBucket(currentBucket);
                countBucket.remove(count);
            }
        }
        
    }
    
    public void dec(String key) {
        // reduce the key
        int count = keyCount.get(key);
        if (count == 1) {
            keyCount.remove(key);
        } else {
            keyCount.put(key, count - 1);
        }

        Bucket currentBucket = countBucket.get(count);
        Bucket prevBucket = countBucket.get(count - 1);

        if (count > 1) {
            if (prevBucket == null) {
                prevBucket = new Bucket(count - 1);
                addBucketAfter(currentBucket.prev, prevBucket);
                countBucket.put(count - 1, prevBucket);
            }
            prevBucket.keys.add(key);
        }

        currentBucket.keys.remove(key);
        if (currentBucket.keys.isEmpty()) {
            removeBucket(currentBucket);
            countBucket.remove(count);
        }


    }
    
    public String getMaxKey() {
        if (tail.prev == head) return "";
        return tail.prev.keys.iterator().next();
    }
    
    public String getMinKey() {
        if (head.next == tail) return "";
        return head.next.keys.iterator().next();
    }

    private void addBucketAfter(Bucket prevBucket, Bucket newBucket) {
        newBucket.prev = prevBucket;
        newBucket.next = prevBucket.next;
        prevBucket.next.prev = newBucket;
        prevBucket.next = newBucket;
    }

    private void removeBucket(Bucket bucket) {
        bucket.prev.next = bucket.next;
        bucket.next.prev = bucket.prev;
    }
}

/**
 * Your AllOne object will be instantiated and called as such:
 * AllOne obj = new AllOne();
 * obj.inc(key);
 * obj.dec(key);
 * String param_3 = obj.getMaxKey();
 * String param_4 = obj.getMinKey();
 */

```









