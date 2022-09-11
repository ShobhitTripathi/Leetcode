class Solution {
    public int maxNumberOfBalloons(String text) {
        int min = 0;
        String balloon = "balloon";
        if (text.length() < balloon.length()) {
            return min;
        }
        min = text.length();
        int[] countText = new int[26];
        int[] countBalloon = new int[26];
        
        for (char ch : text.toCharArray()) {
            ++countText[ch - 'a'];
        }
        
        for (char ch : balloon.toCharArray()) {
            ++countBalloon[ch - 'a'];
        }
        
        for (char c : "balloon".toCharArray()) {
            min = Math.min(min, countText[c - 'a'] / countBalloon[c - 'a']);
        }
        
        return min;
    }
