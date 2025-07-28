# 432. All O`one Data Structure

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
Summary of the approach:

Each Node represents a frequency bucket.
keys in a Node stores all keys with that frequency.
A doubly linked list maintains nodes in increasing order of frequency.
The map keeps track of each key’s current node.
This allows all operations to run in O(1) time.

```

```java

public class Node {
    int freq;
    Node prev;
    Node next;
    Set<String> keys = new HashSet<>(); // Stores all keys with the same frequency

    Node(int freq) {
        this.freq = freq;
    }
}

class AllOne {
    Node head; // Dummy head node
    Node tail; // Dummy tail node
    Map<String, Node> map = new HashMap<>(); // Maps key to its corresponding frequency node

    AllOne() {
        head = new Node(0);
        tail = new Node(0);
        head.next = tail;
        tail.prev = head;
    }

    public void inc(String key) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            int freq = node.freq;
            node.keys.remove(key); // Remove key from current node

            Node nextNode = node.next;
            if (nextNode == tail || nextNode.freq != freq + 1) {
                // Create new node for freq+1 if not present
                Node newNode = new Node(freq + 1);
                newNode.keys.add(key);
                insertAfter(node, newNode);
                map.put(key, newNode);
            } else {
                nextNode.keys.add(key);
                map.put(key, nextNode);
            }

            if (node.keys.isEmpty()) {
                removeNode(node);
            }
        } else {
            // New key, insert at freq = 1
            Node firstNode = head.next;
            if (firstNode == tail || firstNode.freq > 1) {
                Node newNode = new Node(1);
                newNode.keys.add(key);
                insertAfter(head, newNode);
                map.put(key, newNode);
            } else {
                firstNode.keys.add(key);
                map.put(key, firstNode);
            }
        }
    }

    public void dec(String key) {
        if (!map.containsKey(key)) return;

        Node node = map.get(key);
        node.keys.remove(key);
        int freq = node.freq;

        if (freq == 1) {
            // Remove key completely if freq becomes 0
            map.remove(key);
        } else {
            Node prevNode = node.prev;
            if (prevNode == head || prevNode.freq != freq - 1) {
                // Create new node for freq-1 if not present
                Node newNode = new Node(freq - 1);
                newNode.keys.add(key);
                insertAfter(prevNode, newNode);
                map.put(key, newNode);
            } else {
                prevNode.keys.add(key);
                map.put(key, prevNode);
            }
        }

        if (node.keys.isEmpty()) {
            removeNode(node);
        }
    }

    public String getMaxKey() {
        if (tail.prev == head) return "";
        return tail.prev.keys.iterator().next(); // Highest frequency key
    }

    public String getMinKey() {
        if (head.next == tail) return "";
        return head.next.keys.iterator().next(); // Lowest frequency key
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void insertAfter(Node prev, Node node) {
        Node next = prev.next;
        prev.next = node;
        node.prev = prev;
        node.next = next;
        next.prev = node;
    }
}


```
