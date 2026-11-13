Problem Statement
```
1335. Minimum Difficulty of a Job Schedule

You want to schedule a list of jobs in d days. 
Jobs are dependent (i.e To work on the ith job, you have to finish all the jobs j where 0 <= j < i).

You have to finish at least one task every day. 
The difficulty of a job schedule is the sum of difficulties of each day of the d days. 
The difficulty of a day is the maximum difficulty of a job done on that day.

You are given an integer array jobDifficulty and an integer d. The difficulty of the ith job is jobDifficulty[i].

Return the minimum difficulty of a job schedule. If you cannot find a schedule for the jobs return -1.

 

Example 1:


Input: jobDifficulty = [6,5,4,3,2,1], d = 2
Output: 7
Explanation: First day you can finish the first 5 jobs, total difficulty = 6.
Second day you can finish the last job, total difficulty = 1.
The difficulty of the schedule = 6 + 1 = 7 

Example 2:

Input: jobDifficulty = [9,9,9], d = 4
Output: -1
Explanation: If you finish a job per day you will still have a free day. you cannot find a schedule for the given jobs.
Example 3:

Input: jobDifficulty = [1,1,1], d = 3
Output: 3
Explanation: The schedule is one job per day. total difficulty will be 3.
```

Algorithm
```
The dynamic programming solution to this problem consists of three components:

State definition:

Index i (the index of the first task for today in the jobDifficulty array) and day d (number of remaining days) will define the DP state. min_diff(i, d) refers to the minimum difficulty when starting at the i 
th
  job with d days left.

min_diff(0, d) will be the final answer since it represents starting at the beginning of the job array and finishing all jobs in exactly d days.

State transition function:

The job at index j will be the first task for the upcoming day, therefore, the jobs to be finished today are all jobs with indices between i and j, which is jobDifficulty[i:j]. 
Since the difficulty of a day is the maximum difficulty of a job done in that day, the maximum of jobDifficulty[i:j] will be added to the sum of job difficulties.

With this in mind, let's formulate the state transition function as follows:

min_diff(i, d) = min_diff(j, d - 1) + max(jobDifficulty[i:j]) for all j > i

Base case:

When there is only 1 day remaining, we need to finish all unfinished jobs on that day and increase the difficulty of the job schedule by the maximum difficulty of these jobs.

Before we dive into the code, let's take a moment to consider edge cases and optimizations.

One edge case that we must consider is if the number of days is more than the number of tasks, then we won't be able to arrange at least one job per day; in this case, we should return -1.

Finally, one optimization that we can implement is to use a variable, daily_max_job_diff, to keep track of the most difficult job scheduled on the current day. 
As we scan through the jobs for the current day, we do not need to revisit the full subarray of jobDifficulty[i:j] every time we want to calculate the maximum difficulty. 
Instead, each day, we can update the maximum job difficulty seen so far if the current job difficulty is greater than daily_max_job_diff.
```


Solution
```java

class Solution {
    public int minDifficulty(int[] jobDifficulty, int d) {

        int n = jobDifficulty.length;
        // Edge case: make sure there is at least one job per day
        if (n < d) {
            return -1;
        }

        int[][] mem = new int[n][d + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= d; j++) {
                mem[i][j] = -1;
            }
        }

        return minDiff(0, d, jobDifficulty, mem);
    }

    private int minDiff(int i, int daysRemaining, int[] jobDifficulty, int[][] mem) {
        // Use memoization to avoid repeated computation of states
        if (mem[i][daysRemaining] != -1) {
            return mem[i][daysRemaining];
        }

        // Base case: finish all remaining jobs in the last day
        if (daysRemaining == 1) {
            int res = 0;
            for (int j = i; j < jobDifficulty.length; j++) {
                res = Math.max(res, jobDifficulty[j]);
            }
            return res;
        }

        int res = Integer.MAX_VALUE;
        int dailyMaxJobDiff = 0;
        // Iterate through possible starting index for the next day
        // and ensure we have at least one job for each remaining day.
        for (int j = i; j < jobDifficulty.length - daysRemaining + 1; j++) {
            dailyMaxJobDiff = Math.max(dailyMaxJobDiff, jobDifficulty[j]);
            res = Math.min(res, dailyMaxJobDiff + minDiff(j + 1, daysRemaining - 1, jobDifficulty, mem));
        }
        mem[i][daysRemaining] = res;
        return res;
    }
}

```

Complexity Analysis
```
Let n be the length of the jobDifficulty array, and d be the total number of days.

Time complexity: O(n 2⋅d) since there are n⋅d possible states, and we need O(n) time to calculate the result for each state.

Space complexity: O(n⋅d) space is required to memoize all n⋅d states.
```
