class Solution {
    public int search(int[] nums, int target) {
        int low = 0, high = nums.length - 1;
        
        //get the start index of the minimum value
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (nums[mid] > nums[high])
                low = mid + 1;
            else 
                high = mid;
        }
        
        
        //low is the index of the smllest value in the array
        int rot = low;
        System.out.println("smallest number is at index : " + rot);
        
        // reset the values of low andn high
        low = 0;
        high = nums.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            // get the realMid based on the smallest number index
            int realMid = (mid + rot) % (nums.length);
            if (nums[realMid] == target)
                return realMid;
            if(nums[realMid] < target)
                low = mid + 1;
            else 
                high = mid - 1;
        }
        
        return -1;
    }
}
