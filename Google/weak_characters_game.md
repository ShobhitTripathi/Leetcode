# 1996. The Number of Weak Characters in the Game

You are playing a game that contains multiple characters, and each of the characters has two main properties: attack and defense. 
You are given a 2D integer array properties where properties[i] = [attacki, defensei] represents the properties of the ith character in the game.

A character is said to be weak if any other character has both attack and defense levels strictly greater than this character's attack and defense levels. 
More formally, a character i is said to be weak if there exists another character j where attackj > attacki and defensej > defensei.

Return the number of weak characters.

Example 1:
```
Input: properties = [[5,5],[6,3],[3,6]]
Output: 0
Explanation: No character has strictly greater attack and defense than the other.
```
Example 2:
```
Input: properties = [[2,2],[3,3]]
Output: 1
Explanation: The first character is weak because the second character has a strictly greater attack and defense.
```
Example 3:
```
Input: properties = [[1,5],[10,4],[4,3]]
Output: 1
Explanation: The third character is weak because the second character has a strictly greater attack and defense.
```
Constraints:
```
2 <= properties.length <= 105
properties[i].length == 2
1 <= attacki, defensei <= 105
```

Approach
```
-> Brute force is simple, check with all pairs
-> Sorting attack ascending and defense decending[for same attack], iterate from right and and based on maxDefence count

1) Greedy + Counting (Your Main Solution)
Weak ⇒ higher attack and higher defense
Build maxDefense[a] = max defense for each attack
Convert to suffix max so it represents max defense for attack ≥ a
For (a, d), if d < maxDefense[a + 1] ⇒ weak
+2 array size avoids boundary checks

Time: O(N + K)
Space: O(K) (K = maxAttack)

2) Sorting-Based Greedy
Sort by:
attack ascending
defense descending (for same attack)
Traverse from right to left
Maintain maxDefenseSeen
If currentDefense < maxDefenseSeen ⇒ weak
Update maxDefenseSeen

Time: O(N log N)
Space: O(1) (excluding sort)

One-liner comparison (interview-ready):

“Counting + suffix max avoids sorting and runs in O(N + K), while sorting gives a cleaner O(N log N) greedy solution when attack range is large.”

```


Solution
```java

class Solution {
    // Greedy: Time complexity: O(N+K) [k -> attack]
    public int numberOfWeakCharacters(int[][] properties) {
        int maxAttack = 0;

        // Find the maximum attack value
        for (int[] property : properties) {
            int attack = property[0];
            maxAttack = Math.max(maxAttack, attack);
        }

        int[] maxDefense = new int[maxAttack + 2];
        // Store the maximum defense for an attack value
        for (int[] property : properties) {
            int attack = property[0];
            int defense = property[1];

            maxDefense[attack] = Math.max(maxDefense[attack], defense);
        }

        // Store the maximum defense for attack greater than or equal to a value
        for (int i = maxAttack - 1;i >= 0;i--) {
            maxDefense[i] = Math.max(maxDefense[i], maxDefense[i + 1]);
        }

        int weakCharacters = 0;
        for (int[] property : properties) {
            int attack = property[0];
            int defense = property[1];

            // If there is a greater defense for properties with greater attack
            if (defense < maxDefense[attack + 1]) {
                weakCharacters++;
            }
        }
        return weakCharacters;
    }

    private void print2dArray(int[][] arr) {
        for (int i = 0;i < arr.length;i++) {
            System.out.print("[" + arr[i][0] + ", " + arr[i][1] + "], ");
        }
        System.out.println();
    }

    // Soring : O(NLogN)
    // public int numberOfWeakCharacters(int[][] properties) {
    //     int count = 0, maxDefence = 0;

    //     Arrays.sort(properties, (a, b) -> (a[0] == b[0]) ? (b[1] - a[1]) : a[0] - b[0]);
    //     // print2dArray(properties);

    //     for (int i = properties.length - 1;i >= 0;i--) {
    //         if (maxDefence > properties[i][1]) {
    //             count++;
                
    //         }
    //         maxDefence = Math.max(maxDefence, properties[i][1]);
    //     }

    //     return count;
    // }


    // brute force : O(N^2)
    // public int numberOfWeakCharacters(int[][] properties) {
    //     int count = 0;

    //     for (int i = 0;i < properties.length;i++) {
    //         int[] current = properties[i];

    //         for (int j = 0;j < properties.length;j++) {
    //             if (i == j) {
    //                 continue;
    //             }

    //             if (properties[j][0] > current[0] && properties[j][1] > current[1]) {
    //                 count++;
    //                 break;
    //             }
    //         }
    //     }

    //     return count;
    // }
}


```
