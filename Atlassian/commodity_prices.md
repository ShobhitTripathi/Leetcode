# **Commodity Prices — In-Memory Data Structure Design**

This is a classic **Time-Based Data Store + Maximum Tracking** problem.

The requirements are:

1. We receive a stream of `(timestamp, commodityPrice)`.
2. Timestamps can arrive out of order.
3. Duplicate timestamps are allowed, but the latest update must overwrite the existing price.
4. We need to retrieve the maximum commodity price at any point.
5. Optimize for frequent reads and writes.
6. Ideally, `getMaxCommodityPrice()` should run in **O(1)**.

The important design decision is how to maintain the maximum when an existing timestamp is updated.

## **1. Understand the problem**

Example:

```
Input:
(100, 50)
(200, 80)
(150, 60)
(300, 90)
```

Current state:

| Timestamp | Price |
| --------- | ----- |
| 100       | 50    |
| 150       | 60    |
| 200       | 80    |
| 300       | 90    |

Maximum price = 90.

Now suppose we receive:

```
(300, 40)
```

We must update the existing timestamp rather than insert a new record.

New state:

| Timestamp | Price |
| --------- | ----- |
| 100       | 50    |
| 150       | 60    |
| 200       | 80    |
| 300       | 40    |

Maximum price = 80.

Notice that the previous maximum was invalidated. This is the central challenge.

## **2. Approach 1: HashMap + Scan**

The simplest implementation uses a HashMap.

```
Map<Long, Integer> prices = new HashMap<>();
```

### **Upsert**

```
public void upsert(long timestamp, int price) {
    prices.put(timestamp, price);
}
```

### **Get maximum**

```
public int getMaxCommodityPrice() {

    int max = Integer.MIN_VALUE;

    for (int price : prices.values()) {
        max = Math.max(max, price);
    }

    return max;
}
```

Complexity:

| Operation   | Complexity   |
| ----------- | ------------ |
| Upsert      | O(1) average |
| Get maximum | O(N)         |
| Space       | O(N)         |

This is acceptable when writes are frequent but maximum-price queries are rare.

However, the requirement prioritizes frequent reads and writes, so we should improve this.

## **3. Approach 2: HashMap + TreeMap (Recommended baseline)**

We maintain two data structures:

**1. HashMap — Timestamp to Price**

Fast lookup and update by timestamp

```
100 → 50
150 → 60
200 → 80
300 → 90
```

**2. TreeMap — Price to Frequency**

Maintains prices in sorted order

```
50 → 1
60 → 1
80 → 1
90 → 1
```

lastKey() = maximum price

Why frequency?

Because multiple timestamps can have the same price.

For example:

```
(100, 90)
(200, 90)
(300, 80)
```

If we update timestamp 100 from 90 to 70, price 90 must still exist because timestamp 200 has that price.

### **Java implementation**

```

class CommodityPriceTracker {

    // timestamp -> price
    private final Map<Long, Integer> timestampPrice =
            new HashMap<>();

    // price -> frequency
    private final TreeMap<Integer, Integer> priceFrequency =
            new TreeMap<>();

    public void upsert(long timestamp, int newPrice) {

        // Remove old price if timestamp already exists.
        Integer oldPrice = timestampPrice.get(timestamp);

        if (oldPrice != null) {
            int count = priceFrequency.get(oldPrice);

            if (count == 1) {
                priceFrequency.remove(oldPrice);
            } else {
                priceFrequency.put(oldPrice, count - 1);
            }
        }

        // Insert new price.
        timestampPrice.put(timestamp, newPrice);

        priceFrequency.merge(newPrice, 1, Integer::sum);
    }

    public int getMaxCommodityPrice() {

        if (priceFrequency.isEmpty()) {
            throw new NoSuchElementException(
                    "No commodity prices available");
        }

        return priceFrequency.lastKey();
    }
}

```

### **Complexity**

Let N be the number of unique timestamps and P the number of distinct prices.

| Operation   | Complexity               |
| ----------- | ------------------------ |
| Upsert      | O(log P)                 |
| Get maximum | O(log P) in Java TreeMap |
| Space       | O(N + P)                 |

Note: Java's `TreeMap.lastKey()` is O(log P) in the general API complexity model, not a guaranteed O(1).

This is a good general-purpose solution, but we can do better for maximum-price reads.

## **4. Approach 3: HashMap + TreeMap + Cached Maximum (O(1) reads)**

Recommended if reads are very frequent

Yes! We can achieve **O(1) maximum-price retrieval** by maintaining a variable called `maxPrice`.

The idea:

- Maintain the HashMap for timestamp-to-price mapping.
- Maintain the TreeMap for price frequencies.
- Maintain a variable `maxPrice` containing the current maximum.

### **How do we update maxPrice?**

There are three cases:

**Case 1: New timestamp**

```
Current maxPrice = 80
Incoming: (400, 100)

New maxPrice = 100
```

Simply update the variable:

```
maxPrice = Math.max(maxPrice, newPrice);
```

**Case 2: Existing timestamp, old price is not maximum**

```
Current maxPrice = 100
Timestamp 200: 60 → 70

maxPrice remains 100
```

No additional work is required.

**Case 3: Existing timestamp, old price is maximum**

```
Current maxPrice = 100
Timestamp 400: 100 → 50
```

We must check whether any other timestamp still has price 100.

- If yes, maxPrice remains 100.
- If no, find the next maximum using `TreeMap.lastKey()`.

### **Java implementation**

```

class CommodityPriceTracker {

    private final Map<Long, Integer> timestampPrice =
            new HashMap<>();

    private final TreeMap<Integer, Integer> priceFrequency =
            new TreeMap<>();

    private Integer maxPrice = null;

    public void upsert(long timestamp, int newPrice) {

        Integer oldPrice = timestampPrice.get(timestamp);

        // Remove old price frequency.
        if (oldPrice != null) {

            int count = priceFrequency.get(oldPrice);

            if (count == 1) {
                priceFrequency.remove(oldPrice);
            } else {
                priceFrequency.put(oldPrice, count - 1);
            }
        }

        // Insert new price.
        timestampPrice.put(timestamp, newPrice);

        priceFrequency.merge(
                newPrice, 1, Integer::sum
        );

        // Update cached maximum.
        if (maxPrice == null || newPrice > maxPrice) {

            maxPrice = newPrice;

        } else if (oldPrice != null &&
                   oldPrice.equals(maxPrice) &&
                   !priceFrequency.containsKey(maxPrice)) {

            maxPrice = priceFrequency.lastKey();
        }
    }

    public int getMaxCommodityPrice() {

        if (maxPrice == null) {
            throw new NoSuchElementException(
                    "No commodity prices available");
        }

        return maxPrice;
    }
}

```

### **Complexity**

| Operation   | Complexity |
| ----------- | ---------- |
| Upsert      | O(log P)   |
| Get maximum | **O(1)**   |
| Space       | O(N + P)   |

Here, P is the number of distinct prices.

This is a very good trade-off: we move the work of recalculating the maximum into the write operation so that reads are constant time.

## **5. Approach 4: Indexed Max Heap (Alternative optimization)**

There is another interesting approach worth knowing for interviews.

Instead of using a TreeMap, maintain:

1. HashMap: `timestamp → heap index`.
2. Max Heap: stores `(timestamp, price)`.

The heap must support arbitrary updates using the timestamp-to-index mapping.

When a timestamp is updated:

1. Find its existing heap position in O(1).
2. Update its price.
3. Restore the heap property using sift-up or sift-down.
4. Update index mappings whenever heap elements swap.

For a new timestamp, insert into the heap.

The root always contains the maximum price.

**Indexed Max Heap**

# **100**

Maximum price — root

80

90

50

60

Illustrative max heap; parent price is always greater than or equal to child prices.

Complexity:

| Operation                 | Complexity |
| ------------------------- | ---------- |
| Insert new timestamp      | O(log N)   |
| Update existing timestamp | O(log N)   |
| Get maximum               | **O(1)**   |
| Space                     | O(N)       |

This avoids maintaining a separate price-frequency TreeMap.

However, implementing an indexed heap correctly is more involved because every swap must update the index map.

## **6. Which approach should you choose?**

| Approach                       | Upsert   | Get Max  | Implementation |
| ------------------------------ | -------- | -------- | -------------- |
| HashMap + scan                 | O(1)     | O(N)     | Simple         |
| HashMap + TreeMap              | O(log P) | O(log P) | Simple         |
| HashMap + TreeMap + cached max | O(log P) | **O(1)** | Recommended    |
| HashMap + Indexed Max Heap     | O(log N) | **O(1)** | More complex   |

## **7. Important interview follow-up questions**

Be prepared for these:

1. What happens when the timestamp corresponding to the maximum price gets updated to a lower value?
2. What if multiple timestamps have the same maximum price?
3. What if prices can be negative?
4. What if we need minimum price as well?
5. What if we need maximum price within a timestamp range?
6. How would you make this thread-safe when multiple threads perform upserts and reads?
7. What if we need the top K commodity prices?

**My recommendation:** Start with HashMap + TreeMap, then introduce the cached `maxPrice` variable. This directly addresses the interviewer's hint and demonstrates that you understand the trade-off between read and write complexity.

The key concept to remember:

> If a value is queried frequently but expensive to calculate, maintain it incrementally during writes rather than recalculating it during every read.
