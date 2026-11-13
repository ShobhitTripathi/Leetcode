# **Most Popular Content ID — Data Structure Design**

This is a classic **Design a Data Structure with O(1) Increment, Decrement and Get Maximum** problem.

It is closely related to LeetCode 432 — All O`(1)` Data Structure.

The requirements are:

1. `increasePopularity(contentId)` increases popularity by 1.
2. `decreasePopularity(contentId)` decreases popularity by 1.
3. `mostPopular()` returns a content ID having maximum popularity.
4. If all content IDs have popularity 0, return `-1`.
5. Content IDs are positive integers.

The interesting part is achieving **O(1) average time complexity for all three operations**.

## **1. Understand the problem**

Consider this stream:

```
increasePopularity(1)
increasePopularity(2)
increasePopularity(1)
increasePopularity(3)
increasePopularity(2)
```

Popularity becomes:

| Content ID | Popularity |
| ---------- | ---------- |
| 1          | 2          |
| 2          | 2          |
| 3          | 1          |

Most popular = 1 or 2.

Both are valid because they have equal maximum popularity.

Now:

```
decreasePopularity(1)
```

Popularity becomes:

| Content ID | Popularity |
| ---------- | ---------- |
| 1          | 1          |
| 2          | 2          |
| 3          | 1          |

Most popular = 2.

Now:

```
decreasePopularity(2)
decreasePopularity(2)
```

Content 2 reaches popularity 0 and should no longer be considered.

If all content IDs reach 0, return `-1`.

## **2. Why not use HashMap + Max Heap?**

A straightforward solution would be:

- HashMap: `contentId → popularity`.
- Max Heap: Stores content IDs ordered by popularity.

However, when popularity changes, we need to update an existing heap entry.

A normal Java PriorityQueue does not efficiently support arbitrary element updates.

We would need to:

- Remove the old entry.
- Insert the updated entry.

Removal takes O(N), unless we implement an indexed heap.

We want something simpler with O(1) average operations.

## **3. Recommended approach: HashMap + Doubly Linked List**

The key observation:

**We only need to know which content IDs have the maximum popularity. We don't need to sort all content IDs.**

We can maintain popularity groups.

For example:

```
Popularity 1 → [Content 3, Content 4]
Popularity 2 → [Content 1, Content 2]
Popularity 3 → [Content 5]
```

We maintain these groups in a doubly linked list sorted by popularity.

**Popularity buckets**

HEAD

**1**

3, 4

**2**

1, 2

**3**

5

TAIL

The tail always contains the highest popularity bucket.

We need two HashMaps:

**Map 1: Content ID → Popularity Bucket**

```
Map<Integer, Bucket> contentMap;
```

**Map 2:** Not actually necessary if we maintain the doubly linked list and directly access adjacent buckets.

Each bucket contains:

```
class Bucket {
    int popularity;
    Set<Integer> contentIds;
    Bucket prev;
    Bucket next;
}
```

The linked list allows us to move a content ID from popularity K to K+1 or K-1 in O(1).

## **4. Java implementation**

We use sentinel `head` and `tail` nodes to simplify insertion and deletion.

```

import java.util.*;

class Bucket {
    int popularity;
    Set<Integer> contentIds;

    Bucket prev;
    Bucket next;

    Bucket(int popularity) {
        this.popularity = popularity;
        this.contentIds = new HashSet<>();
    }
}

class ContentPopularity {

    private final Map<Integer, Bucket> contentMap;

    private final Bucket head;
    private final Bucket tail;

    public ContentPopularity() {

        contentMap = new HashMap<>();

        head = new Bucket(-1);
        tail = new Bucket(-1);

        head.next = tail;
        tail.prev = head;
    }

    // Increase popularity by 1.
    public void increasePopularity(int contentId) {

        Bucket current = contentMap.get(contentId);

        // Content is new or currently has popularity 0.
        if (current == null) {

            Bucket first = head.next;

            // Reuse popularity-1 bucket if available.
            if (first != tail && first.popularity == 1) {

                first.contentIds.add(contentId);
                contentMap.put(contentId, first);

            } else {

                Bucket newBucket = new Bucket(1);
                newBucket.contentIds.add(contentId);

                insertAfter(head, newBucket);
                contentMap.put(contentId, newBucket);
            }

            return;
        }

        // Move from popularity K to K+1.
        Bucket next = current.next;
        int newPopularity = current.popularity + 1;

        Bucket target;

        if (next != tail &&
            next.popularity == newPopularity) {

            target = next;

        } else {

            target = new Bucket(newPopularity);
            insertAfter(current, target);
        }

        target.contentIds.add(contentId);
        contentMap.put(contentId, target);

        // Remove from old bucket.
        current.contentIds.remove(contentId);

        if (current.contentIds.isEmpty()) {
            removeBucket(current);
        }
    }

    // Decrease popularity by 1.
    public void decreasePopularity(int contentId) {

        Bucket current = contentMap.get(contentId);

        // Ignore unknown content IDs.
        if (current == null) {
            return;
        }

        int newPopularity = current.popularity - 1;

        // Popularity cannot go below zero.
        if (newPopularity == 0) {

            current.contentIds.remove(contentId);
            contentMap.remove(contentId);

            if (current.contentIds.isEmpty()) {
                removeBucket(current);
            }

            return;
        }

        // Move from popularity K to K-1.
        Bucket prev = current.prev;

        Bucket target;

        if (prev != head &&
            prev.popularity == newPopularity) {

            target = prev;

        } else {

            target = new Bucket(newPopularity);
            insertAfter(prev, target);
        }

        target.contentIds.add(contentId);
        contentMap.put(contentId, target);

        current.contentIds.remove(contentId);

        if (current.contentIds.isEmpty()) {
            removeBucket(current);
        }
    }

    // Return any content ID with maximum popularity.
    public int mostPopular() {

        if (tail.prev == head) {
            return -1;
        }

        return tail.prev.contentIds.iterator().next();
    }

    private void insertAfter(Bucket node, Bucket newNode) {

        Bucket next = node.next;

        node.next = newNode;
        newNode.prev = node;

        newNode.next = next;
        next.prev = newNode;
    }

    private void removeBucket(Bucket node) {

        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}

```

## **5. Dry run**

Let's execute the following operations:

```
increasePopularity(1)
increasePopularity(2)
increasePopularity(1)
increasePopularity(3)
decreasePopularity(1)
```

**Operation-by-operation execution**

Step 1 of 5

increasePopularity(1)

**Current popularity buckets**

**1**

**1**

**Most popular**

## **1**

PreviousNext operation

## **6. Complexity analysis**

Let N be the number of content IDs.

| Operation          | Time complexity |
| ------------------ | --------------- |
| increasePopularity | O(1) average    |
| decreasePopularity | O(1) average    |
| mostPopular        | O(1) average    |
| Space              | O(N)            |

Why O(1)?

- HashMap lookup is O(1) average.
- HashSet insertion/removal is O(1) average.
- Doubly linked list insertion/removal is O(1).
- Maximum is always available at `tail.prev`.

There is no sorting, heap operation, or traversal of the popularity buckets.

## **7. Important interview follow-ups**

**Q1. What if multiple content IDs have the same maximum popularity?**

Return any one of them. The problem does not specify a tie-breaking rule.

**Q2. What if popularity becomes zero?**

Remove the content ID from both the bucket and HashMap. Zero-popularity content does not need to be stored.

**Q3. What if decreasePopularity is called for an unknown content ID?**

Ignore the operation. Alternatively, throw an exception if the API contract requires strict validation.

**Q4. Why use a doubly linked list instead of a TreeMap?**

Because popularity only changes by +1 or -1. We only need to move between adjacent popularity buckets, so we don't need a sorted tree.

**Q5. Is this thread-safe?**

No. The implementation is designed for a single-threaded execution model. For concurrent updates and reads, we could protect the operations using a `ReentrantLock`, ensuring each operation is atomic.

## **8. Interview takeaway**

Remember this pattern:

**HashMap + Doubly Linked List + HashSet**

- HashMap: Find the current bucket of a content ID.
- Doubly Linked List: Maintain popularity buckets in sorted order.
- HashSet: Store all content IDs having the same popularity.
- Tail: Provides the maximum popularity bucket.

This is one of the most useful interview patterns for designing data structures with O(1) increment, decrement, and maximum retrieval.

**Practice problem:** LeetCode 432 — All O(1) Data Structure. It uses essentially the same underlying technique.
