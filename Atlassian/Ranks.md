# 1366. Rank Teams by Votes
Medium
Topics
Companies
Hint
In a special ranking system, each voter gives a rank from highest to lowest to all teams participating in the competition.

The ordering of teams is decided by who received the most position-one votes. If two or more teams tie in the first position, we consider the second position to resolve the conflict, if they tie again, we continue this process until the ties are resolved. If two or more teams are still tied after considering all positions, we rank them alphabetically based on their team letter.

You are given an array of strings votes which is the votes of all voters in the ranking systems. Sort all teams according to the ranking system described above.

Return a string of all teams sorted by the ranking system.

 

Example 1:

Input: votes = ["ABC","ACB","ABC","ACB","ACB"]
Output: "ACB"
Explanation: 
Team A was ranked first place by 5 voters. No other team was voted as first place, so team A is the first team.
Team B was ranked second by 2 voters and ranked third by 3 voters.
Team C was ranked second by 3 voters and ranked third by 2 voters.
As most of the voters ranked C second, team C is the second team, and team B is the third.
Example 2:

Input: votes = ["WXYZ","XYZW"]
Output: "XWYZ"
Explanation:
X is the winner due to the tie-breaking rule. X has the same votes as W for the first position, but X has one vote in the second position, while W does not have any votes in the second position. 
Example 3:

Input: votes = ["ZMNAGUEDSJYLBOPHRQICWFXTVK"]
Output: "ZMNAGUEDSJYLBOPHRQICWFXTVK"
Explanation: Only one voter, so their votes are used for the ranking.
 

Constraints:

1 <= votes.length <= 1000
1 <= votes[i].length <= 26
votes[i].length == votes[j].length for 0 <= i, j < votes.length.
votes[i][j] is an English uppercase letter.
All characters of votes[i] are unique.
All the characters that occur in votes[0] also occur in votes[j] where 1 <= j < votes.length.


 
```
Key Logic -:

Rank Map Initialization:

Each team is mapped to an array where each index represents a position in the ranking, and the value at the index indicates how many voters ranked the team at that position.
  
Populate Rank Counts:
Iterate over each vote and increment the count in the corresponding position array for each team.
  
Custom Sorting:
Sort the teams based on the position counts. For two teams a and b, compare their counts at each position from first to last.
If the counts differ, the team with the higher count at the current position is ranked higher.
If all counts are identical, break ties alphabetically.
  
Build Result String:
Append the sorted teams to a StringBuilder to form the final result.
  
Time Complexity:
O(N * M + M * log(M) * M), where N is the number of votes, and M is the number of teams.
O(N * M) for populating the rank counts.
O(M * log(M) * M) for sorting with a comparison cost of O(M) (number of positions).

Space Complexity:
O(M * M) for the rank map to store the count arrays for each team.

```



```java
import java.util.*;

public class TeamRanking {
    public String rankTeams(String[] votes) {
        // Map to store rank counts for each team
        Map<Character, int[]> rankMap = new HashMap<>();

        // Initialize rank map for each team
        for (char team : votes[0].toCharArray()) {
            rankMap.put(team, new int[26]); // Max 26 positions
        }

        // Count votes for each team at every position
        for (String vote : votes) {
            for (int i = 0; i < vote.length(); i++) {
                rankMap.get(vote.charAt(i))[i]++;
            }
        }

        // Create a list of teams for sorting
        List<Character> teams = new ArrayList<>(rankMap.keySet());

        // Sort teams based on the rank counts and alphabetically for tie-breaking
        teams.sort((a, b) -> {
            int[] rankA = rankMap.get(a);
            int[] rankB = rankMap.get(b);
            for (int i = 0; i < 26; i++) {
                if (rankA[i] != rankB[i]) {
                    return rankB[i] - rankA[i]; // Higher votes come first
                }
            }
            return a - b; // Alphabetical order as a tie-breaker
        });

        // Build and return the result string
        StringBuilder result = new StringBuilder();
        for (char team : teams) {
            result.append(team);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        TeamRanking tr = new TeamRanking();

        // Example 1
        System.out.println(tr.rankTeams(new String[]{"ABC", "ACB", "ABC", "ACB", "ACB"})); // Output: "ACB"

        // Example 2
        System.out.println(tr.rankTeams(new String[]{"WXYZ", "XYZW"})); // Output: "XWYZ"

        // Example 3
        System.out.println(tr.rankTeams(new String[]{"ZMNAGUEDSJYLBOPHRQICWFXTVK"})); // Output: "ZMNAGUEDSJYLBOPHRQICWFXTVK"
    }
}


```

# To extend based on different ranking on tie
```java
import java.util.*;

public class TeamRanking {
    public String rankTeams(String[] votes, Map<Character, Integer> priorityMap) {
        // Map to store rank counts for each team
        Map<Character, int[]> rankMap = new HashMap<>();

        // Initialize rank map for each team
        for (char team : votes[0].toCharArray()) {
            rankMap.put(team, new int[26]); // Max 26 positions
        }

        // Count votes for each team at every position
        for (String vote : votes) {
            for (int i = 0; i < vote.length(); i++) {
                rankMap.get(vote.charAt(i))[i]++;
            }
        }

        // Create a list of teams for sorting
        List<Character> teams = new ArrayList<>(rankMap.keySet());

        // Sort teams based on the rank counts, tie-breaking with custom priority, and alphabetically
        teams.sort((a, b) -> {
            int[] rankA = rankMap.get(a);
            int[] rankB = rankMap.get(b);

            // Compare rank counts
            for (int i = 0; i < 26; i++) {
                if (rankA[i] != rankB[i]) {
                    return rankB[i] - rankA[i]; // Higher votes come first
                }
            }

            // Tie-breaking with priority map
            int priorityA = priorityMap.getOrDefault(a, 0);
            int priorityB = priorityMap.getOrDefault(b, 0);
            if (priorityA != priorityB) {
                return priorityB - priorityA; // Higher priority comes first
            }

            // Final tie-breaking alphabetically
            return a - b;
        });

        // Build and return the result string
        StringBuilder result = new StringBuilder();
        for (char team : teams) {
            result.append(team);
        }
        return result.toString();
    }

    public static void main(String[] args) {
        TeamRanking tr = new TeamRanking();

        // Example votes
        String[] votes = {"ABC", "ACB", "ABC", "ACB", "ACB"};

        // Additional priority map
        Map<Character, Integer> priorityMap = new HashMap<>();
        priorityMap.put('A', 10);
        priorityMap.put('B', 5);
        priorityMap.put('C', 7);

        System.out.println(tr.rankTeams(votes, priorityMap)); // Output: "ACB"
    }
}

```








