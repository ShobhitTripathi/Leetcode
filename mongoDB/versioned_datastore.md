Problem Statement
```
You are building a versioned datastore. 
You are given two APIs.

PUT(docId int, contents string, timestamp int) -> This will save the contents of that docId for that timestamp. 
There can be more than one call for a doc ID with differing timestamp, so we would essentially need to store versions for a given docId.

GET(docId int, timestamp int) -> For the timestamp in input, return the content of that docId for the version just before the timestamp. 
If there is no content, return an empty string.

The timestamp for PUT calls can be unordered. For eg.
PUT(1, "abc", 10)
PUT(2, "bcd", 11)
PUT(1, "cde", 8)
PUT(2, "def", 9)

GET(1, 5) -> ""
GET(1, 9) -> "cde"
GET(2, 15) -> "bcd"

```
Solution
```java

import java.util.*;

class VersionedStore {

    private Map<Integer, TreeMap<Integer, String>> store;

    public VersionedStore() {
        store = new HashMap<>();
    }

    public void put(int docId, String content, int timestamp) {
        store.putIfAbsent(docId, new TreeMap<>());
        store.get(docId).put(timestamp, content);
    }

    public String get(int docId, int timestamp) {
        if (!store.containsKey(docId)) return "";

        TreeMap<Integer, String> versions = store.get(docId);
        Integer key = versions.floorKey(timestamp);

        if (key == null) return "";
        return versions.get(key);
    }
}

```
Notes
```
Complexity Analysis:
PUT	O(log N)
GET	O(log N)
Space	O(total PUTs)

Concurrent System : Use ConcurrentHashMap + ConcurrentSkipListMap


// GET_RANGE(docId, startTs, endTs): O(log N + K) [log N → find start AND k is number of results]
public List<String> getRange(int docId, int startTs, int endTs) {
    if (!store.containsKey(docId)) return new ArrayList<>();

    TreeMap<Integer, String> versions = store.get(docId);
    return new ArrayList<>(
        versions.subMap(startTs, true, endTs, true).values()
    );
}

```
