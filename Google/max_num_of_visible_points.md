# 1610. Maximum Number of Visible Points

You are given an array points, an integer angle, and your location, 
where location = [posx, posy] and points[i] = [xi, yi] both denote integral coordinates on the X-Y plane.

Initially, you are facing directly east from your position. 
You cannot move from your position, but you can rotate. 
In other words, posx and posy cannot be changed. 
Your field of view in degrees is represented by angle, determining how wide you can see from any given view direction. 
Let d be the amount in degrees that you rotate counterclockwise. Then, your field of view is the inclusive range of angles [d - angle/2, d + angle/2].


You can see some set of points if, for each point, the angle formed by the point, your position, and the immediate east direction from your position is in your field of view.

There can be multiple points at one coordinate. 
There may be points at your location, and you can always see these points regardless of your rotation. Points do not obstruct your vision to other points.

Return the maximum number of points you can see.

 Example 1:
```
Input: points = [[2,1],[2,2],[3,3]], angle = 90, location = [1,1]
Output: 3
Explanation: The shaded region represents your field of view. All points can be made visible in your field of view, including [3,3] even though [2,2] is in front and in the same line of sight.
```
Example 2:
```
Input: points = [[2,1],[2,2],[3,4],[1,1]], angle = 90, location = [1,1]
Output: 4
Explanation: All points can be made visible in your field of view, including the one at your location.
```
Example 3:
```
Input: points = [[1,0],[2,1]], angle = 13, location = [1,1]
Output: 1
Explanation: You can only see one of the two points, as shown above.
 ```

Constraints:
```
1 <= points.length <= 105
points[i].length == 2
location.length == 2
0 <= angle < 360
0 <= posx, posy, xi, yi <= 100
```


Approach
```
Convert all points to angles from the observer, sort them,
duplicate with +360° to handle circular wrap-around,
then use a sliding window to find the maximum number of points within the given viewing angle.

```
Steps
```
Treat the given location as the origin.
For every point, compute the angle it makes with the x-axis using atan2.
If a point is exactly at the same location, count it separately (always visible).
Sort all computed angles.
Duplicate each angle by adding 360° to handle circular wrap-around. [By duplicating angles with +360, we linearize the circle, allowing a normal sliding window.]
Use a sliding window (two pointers) to find the maximum number of angles whose difference is within the given viewing angle.
Add the same-location count to every window result.
Return the maximum value found.

Core idea:
Convert geometry into angles → sort → sliding window on angles.
```


Time & Space Complexity
```
Aspect	Complexity
Angle computation	O(n)
Sorting	O(n log n)
Sliding window	O(n)
Total Time	O(n log n)
Space	O(n)
```


Solution
```
class Solution {
    public int visiblePoints(List<List<Integer>> points, int angle, List<Integer> location) {
        // Stores angles (in degrees) of all points relative to the observer
        List<Double> angles = new ArrayList<>();

        // Counts points that lie exactly at the observer's location
        // These are always visible regardless of viewing direction
        int sameLocationCount = 0;

        // Step 1: Convert each point to its polar angle
        for (List<Integer> p : points) {
            int dx = p.get(0) - location.get(0);
            int dy = p.get(1) - location.get(1);

            // Edge case: point coincides with observer
            if (dx == 0 && dy == 0) {
                sameLocationCount++;
                continue;
            }

            // atan2 gives angle in range [-180, 180], handling all quadrants correctly
            angles.add(Math.atan2(dy, dx) * (180 / Math.PI));
        }

        // Step 2: Sort angles to enable sliding window
        Collections.sort(angles);

        // Step 3: Duplicate angles with +360 to handle circular wrap-around
        // This converts the circular angle problem into a linear one
        List<Double> extended = new ArrayList<>(angles);
        for (double a : angles) {
            extended.add(a + 360);
        }

        int maxVisible = sameLocationCount;

        // Step 4: Sliding window over angles
        // Finds the largest group where (maxAngle - minAngle) <= viewing angle
        for (int left = 0, right = 0; right < extended.size(); right++) {

            // Shrink window if it exceeds the allowed viewing angle
            while (extended.get(right) - extended.get(left) > angle) {
                left++;
            }

            // Current window size + points at observer location
            maxVisible = Math.max(
                maxVisible,
                sameLocationCount + (right - left + 1)
            );
        }

        return maxVisible;
    }
}
```
