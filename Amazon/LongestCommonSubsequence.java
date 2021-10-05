package Amazon;

public class LongestCommonSubsequence {

    public static int lengthOfLongestCommonSubsequence(String s1, String  s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0;i < m;i++) {
            for (int j = 0;j < n;j++) {
                if (s1.charAt(i) == s1.charAt(j)) {
                    dp[i + 1][j + 1] = 1 + dp[i][j];
                } else {
                    dp[i + 1][j + 1] = Math.max(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        String s1 = "abcdef";
        String s2 = "acef";
        System.out.println("s1 : "+ s1 + "   s2 : " + s2);
        int res = lengthOfLongestCommonSubsequence(s1, s2);
        System.out.println("longest Common subsequence is : " +  res);
    }
}
