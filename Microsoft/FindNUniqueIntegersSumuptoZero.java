class Solution {
    public int[] sumZero(int n) {
        int[] result = new int[n];
        int temp = n / 2;
        int l = 0, r = n-1;
        while (l < r) {
            result[l++] = temp;
            result[r--] = temp * -1;
            temp--;
        }
        return result;
    }
}
