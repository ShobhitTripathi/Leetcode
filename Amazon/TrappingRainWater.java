package Amazon;

public class TrappingRainWater {
/*
    For instance, at the very beginning, if leftMax = 2, rightMax = 5,
    then we move left pointer from index 0 to index 1 since left bar is smaller.
    For index 1, left bar is 2, we can conclude that the area in position 1 is 2.

    Let us analysis two possible conditions:
    Condition 1: there exists another bar with height h, as 2 < h < 5,
        then position 1 can fill water with 2 units, even though water will be blocked by the middle bar
    Condition 2: there exists another bar with height h, as 2 > h,
        then position 1 still can fill water with 2 units
        (in other words, if there does not exist any bars greater than 2,
        then water will fill out every position until reach the right most bar with height 5)

    So anything in the middle of leftMax bar and rightMax bar will not influence how much water can current position trap
    Overall,
    we only need to track whether leftMax is smaller than rightMax or not (shorter bar will determine volume of water
    If leftMax is smaller, use left bar as current container rim
    If rightMax is smaller, use right bar as current container rim
    DO NOT FORGET to deduct the height of current position while doing calculation (try to imagine that height of current position is the height of base）
*/


    private static int trapRain (int[] height) {
        if (height == null || height.length == 0)
            return 0;

        int result = 0;
        // leftMax represents the highest bar from left
        int leftMax = Integer.MIN_VALUE;
        // rightMax represents the highest bar from right
        int rightMax = Integer.MIN_VALUE;

        // use two pointers to scan the entire array until they meet with each other
        // Key points: any bars in the middle of leftMax bar and rightMax bar will not influence
        // how much water can current position trap
        for (int left = 0, right = height.length - 1; left < right;) {
            leftMax = Math.max(leftMax, height[left]);
            rightMax = Math.max(rightMax, height[right]);

            //how much can current position trap depends on the shorter bar
            if (leftMax < rightMax) {
                result += leftMax - height[left];
                left++;
            } else {
                result += rightMax - height[right];
                right--;
            }
        }
        return result;
    }


    // Neetcode solution
    public int maxArea(int[] heights) {
        if (heights == null || heights.length == 0)
            return 0;
        int result = 0;
        int left = 0;
        int right = heights.length - 1;

        while (left < right) {
            // right - left is the distance (width) between the two lines, and Math.min(heights[left], heights[right]) is the height (because the shorter line determines how much water the container can hold)
            int area = Math.min(heights[left], heights[right]) * (right - left);
            result = Math.max(result, area);
            if (heights[left] <= heights[right]) {
                left++;
            } else {
                right--;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        //
        int[] arr = {0,1,0,2,1,0,1,3,2,1,2,1};
        int res = trapRain(arr);
        System.out.println("Amount of water trapped : " +  res);
    }

}
