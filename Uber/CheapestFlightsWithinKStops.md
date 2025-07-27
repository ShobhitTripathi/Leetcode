# 787. Cheapest Flights Within K Stops
Solved
Medium
Topics
premium lock icon
Companies
There are n cities connected by some number of flights. You are given an array flights where flights[i] = [fromi, toi, pricei] indicates that there is a flight from city fromi to city toi with cost pricei.

You are also given three integers src, dst, and k, return the cheapest price from src to dst with at most k stops. If there is no such route, return -1.

 

Example 1:


Input: n = 4, flights = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src = 0, dst = 3, k = 1
Output: 700
Explanation:
The graph is shown above.
The optimal path with at most 1 stop from city 0 to 3 is marked in red and has cost 100 + 600 = 700.
Note that the path through cities [0,1,2,3] is cheaper but is invalid because it uses 2 stops.
Example 2:


Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
Output: 200
Explanation:
The graph is shown above.
The optimal path with at most 1 stop from city 0 to 2 is marked in red and has cost 100 + 100 = 200.
Example 3:


Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
Output: 500
Explanation:
The graph is shown above.
The optimal path with no stops from city 0 to 2 is marked in red and has cost 500.
 

Constraints:

1 <= n <= 100
0 <= flights.length <= (n * (n - 1) / 2)
flights[i].length == 3
0 <= fromi, toi < n
fromi != toi
1 <= pricei <= 104
There will not be any multiple flights between two cities.
0 <= src, dst, k < n
src != dst

```java

class Solution {
    // Map<Integer, List<int[]>> map;
    // int cost = Integer.MAX_VALUE;

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
        // Build graph as adjacency list
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] flight : flights) {
            graph
                .computeIfAbsent(flight[0], x -> new ArrayList<>())
                .add(new int[]{flight[1], flight[2]}); // {destination, cost}
        }

        // PriorityQueue: [totalCost, currentCity, remainingStops]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src, k + 1});

        // cost[node][stops] = min cost to reach node with stops
        int[] visited = new int[n];
        Arrays.fill(visited, -1);

        while (!pq.isEmpty()) {
            int[] curr = pq.poll();
            int cost = curr[0], city = curr[1], stops = curr[2];

            if (city == dst) return cost;

            if (stops > 0) {
                if (visited[city] != -1 && visited[city] >= stops) continue;
                visited[city] = stops;

                if (graph.containsKey(city)) {
                    for (int[] neighbor : graph.get(city)) {
                        int nextCity = neighbor[0];
                        int price = neighbor[1];
                        pq.offer(new int[]{cost + price, nextCity, stops - 1});
                    }
                }
            }
        }

        return -1;
        
    }

}

```
