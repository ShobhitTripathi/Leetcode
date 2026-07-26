# [309. Best Time to Buy and Sell Stock with Cooldown](https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/)
Medium


You are given an array prices where prices[i] is the price of a given stock on the ith day.

Find the maximum profit you can achieve. You may complete as many transactions as you like (i.e., buy one and sell one share of the stock multiple times) with the following restrictions:

After you sell your stock, you cannot buy stock on the next day (i.e., cooldown one day).
Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).

 

Example 1:
```
Input: prices = [1,2,3,0,2]
Output: 3
Explanation: transactions = [buy, sell, cooldown, buy, sell]
```
Example 2:
```
Input: prices = [1]
Output: 0
``` 

Constraints:

- 1 <= prices.length <= 5000
- 0 <= prices[i] <= 1000

## Approach
![image](https://user-images.githubusercontent.com/20329508/167283016-32563379-5dd2-4dd8-8734-2eef1b61807d.png)

```
- From the above options diagram
- we can have two states:
  - buying
  - selling
- for buying we can either sell or cooldown
- for selling state we must give cooldown before next buying
```

## Solution
```java
class Solution {
    // Memo map: key = "index-buyingFlag", value = max profit from this state
    private Map<String, Integer> dp = new HashMap<>();

    public int maxProfit(int[] prices) {
        // Start on day 0 with the option to buy
        return dfs(0, true, prices);
    }

    private int dfs(int i, boolean buying, int[] prices) {

        // Base case: out of bounds → no more profit possible
        if (i >= prices.length) {
            return 0;
        }

        // Build a unique state key
        String key = i + "-" + buying;

        // Return memoized result if already computed
        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        // Option 1: cooldown → skip this day and do nothing
        int cooldown = dfs(i + 1, buying, prices);

        if (buying) {
            // Option 2 (buy state): buy today → subtract price and go to sell state next day
            int buy = dfs(i + 1, false, prices) - prices[i];
            dp.put(key, Math.max(buy, cooldown));

        } else {
            // Option 2 (sell state): sell today → add price and jump to i+2 (cooldown day)
            int sell = dfs(i + 2, true, prices) + prices[i];
            dp.put(key, Math.max(sell, cooldown));
        }

        return dp.get(key);
    }
}

```

## Complexity Analysis
```
- Time Complexity: O(N)
- Space Complexity: O(N)
```
