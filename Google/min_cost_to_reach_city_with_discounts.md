2093. Minimum Cost to Reach City With Discounts
```
A series of highways connect n cities numbered from 0 to n - 1. 
You are given a 2D integer array highways where highways[i] = [city1i, city2i, tolli] 
indicates that there is a highway that connects city1i and city2i, 
allowing a car to go from city1i to city2i and vice versa for a cost of tolli.

You are also given an integer discounts which represents the number of discounts you have. 
You can use a discount to travel across the ith highway for a cost of tolli / 2 (integer division). 
Each discount may only be used once, and you can only use at most one discount per highway.

Return the minimum total cost to go from city 0 to city n - 1, or -1 
if it is not possible to go from city 0 to city n - 1.

 

Example 1:


Input: n = 5, highways = [[0,1,4],[2,1,3],[1,4,11],[3,2,3],[3,4,2]], discounts = 1
Output: 9
Explanation:
Go from 0 to 1 for a cost of 4.
Go from 1 to 4 and use a discount for a cost of 11 / 2 = 5.
The minimum cost to go from 0 to 4 is 4 + 5 = 9.
Example 2:


Input: n = 4, highways = [[1,3,17],[1,2,7],[3,2,5],[0,1,6],[3,0,20]], discounts = 20
Output: 8
Explanation:
Go from 0 to 1 and use a discount for a cost of 6 / 2 = 3.
Go from 1 to 2 and use a discount for a cost of 7 / 2 = 3.
Go from 2 to 3 and use a discount for a cost of 5 / 2 = 2.
The minimum cost to go from 0 to 3 is 3 + 3 + 2 = 8.
Example 3:


Input: n = 4, highways = [[0,1,3],[2,3,2]], discounts = 0
Output: -1
Explanation:
It is impossible to go from 0 to 3 so return -1.


```

Solution 
```java
class Solution {
    Map<Integer, List<int[]>> graph;

    public int minimumCost(int n, int[][] highways, int discounts) {
        graph = new HashMap<>();

        for (int[] highway : highways) {
            graph.computeIfAbsent(highway[0], k -> new ArrayList<>())
                .add(new int[]{highway[1], highway[2]});
            graph.computeIfAbsent(highway[1], k -> new ArrayList<>())
                .add(new int[]{highway[0], highway[2]});
        }

        int[][] dist = new int[n][discounts + 1];
        for (int i = 0; i < n; i++) { 
            Arrays.fill(dist[i], Integer.MAX_VALUE); 
        }

        // {totalCostSoFar, city, discountsUsed}
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> a[0])
        );
        pq.offer(new int[]{0, 0, 0});
        dist[0][0] = 0;
        
        while (!pq.isEmpty()) {
           int[] node = pq.poll();
           int cost = node[0];
           int city = node[1];
           int used = node[2];
            
            if (city == n - 1) {
                return cost;
            }

            if (cost > dist[city][used]) {
                continue;
            }

            for (int[] next : graph.getOrDefault(city, Collections.emptyList())) {
                int neighbour = next[0]; 
                int toll = next[1]; 

                // Move without using discount 
                int newCost = cost + toll; 
                if (newCost < dist[neighbour][used]) {
                     dist[neighbour][used] = newCost; 
                     pq.offer(new int[]{newCost, neighbour, used});
                }

                // Move using discount
                if (used < discounts) { 
                    int discountedCost = cost + (toll / 2); 
                    if (discountedCost < dist[neighbour][used + 1]) { 
                        dist[neighbour][used + 1] = discountedCost; 
                        pq.offer(new int[]{ (int) discountedCost, neighbour, used + 1 }); 
                    } 
                }
            }
        }

        return -1;
    }
}

```


### Approach (Brief)

Use **Dijkstra's Algorithm** with an extended state:

```text
(city, discountsUsed)
```

Instead of storing the minimum cost for just a city, store:

```java
dist[city][discountsUsed]
```

which represents the minimum cost to reach a city after using a certain number of discounts.

For each road with toll `t`:

1. **Without discount**:

   ```text
   newCost = cost + t
   ```

2. **With discount** (if discounts are available):

   ```text
   newCost = cost + t/2
   ```

Use a priority queue storing:

```text
(totalCost, city, discountsUsed)
```

and always process the state with the lowest cost first.

---

### Complexity

* **Time Complexity:** `O(M × D × log(N × D))`

  * `N` = number of cities
  * `M` = number of highways
  * `D` = number of discounts

* **Space Complexity:** `O(N × D)`

---

### One-line Interview Explanation

> This is a modified Dijkstra where each state is represented by `(city, discountsUsed)`, allowing us to track the minimum cost for every possible discount usage scenario.
