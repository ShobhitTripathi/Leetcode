# 875. Koko Eating Bananas
Medium


Koko loves to eat bananas. There are n piles of bananas, the ith pile has piles[i] bananas. The guards have gone and will come back in h hours.

Koko can decide her bananas-per-hour eating speed of k. Each hour, she chooses some pile of bananas and eats k bananas from that pile. If the pile has less than k bananas, she eats all of them instead and will not eat any more bananas during this hour.

Koko likes to eat slowly but still wants to finish eating all the bananas before the guards return.

Return the minimum integer k such that she can eat all the bananas within h hours.

 

Example 1:
```
Input: piles = [3,6,7,11], h = 8
Output: 4
```
Example 2:
```
Input: piles = [30,11,23,4,20], h = 5
Output: 30
```
Example 3:
```
Input: piles = [30,11,23,4,20], h = 6
Output: 23
``` 

Constraints:

- 1 <= piles.length <= 10<sup>4</sup>
- piles.length <= h <= 10<sup>9</sup>
- 1 <= piles[i] <= 10<sup>9</sup>

## Approach
```
- Binary search on range (1, max(pile))
- while the condition satisfies go towards left half
- else go towards right half
```

## Solution
```java
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int max = Arrays.stream(piles).max().getAsInt();
        int l = 1;
       // int r = getMax(piles);
       int r = max;

        int res = r;

       // arr = [1 ...... r(max in k)]
        while (l <= r) {
            int mid = l + ((r - l) / 2);
            long totalTime = 0;
            for (int pile : piles) {
                totalTime += Math.ceil((double) pile / mid); 
            }
            if (totalTime <= h) {
                res = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return res;        
    }

    public int getMax(int[] piles) {
        int max = 0;

        for (int num : piles) {
            max = Math.max(max, num);
        }
        return max;
    }
}

```

## Complexity Analysis
```
- Time Complexity:  O(n*logm), m: max(piles)
- Space Complexity: O(1)
```
