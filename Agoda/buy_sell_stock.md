# 121. Best Time to Buy and Sell Stock
Easy


You are given an array prices where prices[i] is the price of a given stock on the ith day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.

 

Example 1:
```
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
```
Example 2:
```
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
 ```

Constraints:

- 1 <= prices.length <= 105
- 0 <= prices[i] <= 104

```java
class Solution {
    public int maxProfit(int[] prices) {
        int[] diffArr = new int[prices.length - 1];

        for (int i = 0; i < prices.length - 1; i++) {
            diffArr[i] = prices[i + 1] - prices[i];
        }

        int max = 0;
        max = calculateMax(diffArr);

        return max;
    }

    private static int calculateMax(int[] arr) {
        int maxSoFar = Integer.MIN_VALUE;
        int maxEndingHere = 0;

        for (int i = 0;i < arr.length;i++) {
            maxEndingHere += arr[i];
            if (maxSoFar < maxEndingHere) {
                maxSoFar = maxEndingHere;
            }
            if (maxEndingHere < 0) {
                maxEndingHere = 0;
            }
        }

        return maxSoFar > 0 ? maxSoFar : 0;
    }
}




class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        if(n == 1) return 0;
        int maxP = 0;
        int l = 0, r = 1;
        
        while(r < n) {
            if(prices[l] < prices[r]) {
                int profit = prices[r] - prices[l];
                maxP = Math.max(maxP, profit);
            } else
                l = r;
            r++;
        }
        return maxP;
    }
}
```
