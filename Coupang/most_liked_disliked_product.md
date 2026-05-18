Problem Statement
```
Given a list of products, users can either **like** or **dislike** a product.

At any point in time, we should be able to return:

1. The **most liked product**
2. The **most disliked product**

Design a system that supports these operations efficiently:

java
like(productName)
dislike(productName)
getMostLikedProduct()
getMostDislikedProduct()

```
Approach
```
Maintain two maps:
likeCount
dislikeCount

Each product will have its like and dislike count.

To quickly fetch the most liked and most disliked product, use two priority queues:

1. Max heap for likes
2. Max heap for dislikes

Whenever a product is liked/disliked, push the updated count into the heap.

Because old values may still exist in the heap, we use **lazy deletion**.
While getting the top product, we check whether the heap value matches the latest value in the map.

```

Solution

```java
import java.util.*;

class ProductSystem {

    static class Product {
        String name;
        int count;

        Product(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }

    // Stores latest like count of each product
    private Map<String, Integer> likeCount = new HashMap<>();

    // Stores latest dislike count of each product
    private Map<String, Integer> dislikeCount = new HashMap<>();

    // Max heap based on like count
    private PriorityQueue<Product> likeHeap = new PriorityQueue<>(
        (a, b) -> b.count - a.count
    );

    // Max heap based on dislike count
    private PriorityQueue<Product> dislikeHeap = new PriorityQueue<>(
        (a, b) -> b.count - a.count
    );

    public void like(String productName) {
        int newCount = likeCount.getOrDefault(productName, 0) + 1;
        likeCount.put(productName, newCount);

        // Push updated count into heap
        likeHeap.offer(new Product(productName, newCount));
    }

    public void dislike(String productName) {
        int newCount = dislikeCount.getOrDefault(productName, 0) + 1;
        dislikeCount.put(productName, newCount);

        // Push updated count into heap
        dislikeHeap.offer(new Product(productName, newCount));
    }

    public String getMostLikedProduct() {
        while (!likeHeap.isEmpty()) {
            Product top = likeHeap.peek();

            // Ignore outdated heap entries
            if (likeCount.get(top.name) == top.count) {
                return top.name;
            }

            likeHeap.poll();
        }

        return null;
    }

    public String getMostDislikedProduct() {
        while (!dislikeHeap.isEmpty()) {
            Product top = dislikeHeap.peek();

            // Ignore outdated heap entries
            if (dislikeCount.get(top.name) == top.count) {
                return top.name;
            }

            dislikeHeap.poll();
        }

        return null;
    }

    public static void main(String[] args) {
        ProductSystem system = new ProductSystem();

        system.like("iPhone");
        system.like("Samsung");
        system.like("iPhone");

        system.dislike("Nokia");
        system.dislike("Samsung");
        system.dislike("Nokia");

        System.out.println(system.getMostLikedProduct());     // iPhone
        System.out.println(system.getMostDislikedProduct());  // Nokia
    }
}
```

Complexity
```
Time Complexity:
For `N` operations:
like()                  -> O(log N)
dislike()               -> O(log N)
getMostLikedProduct()   -> O(log N) amortized
getMostDislikedProduct()-> O(log N) amortized

Space Complexity: O(N)
```


