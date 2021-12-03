package walmart;


import java.util.*;

public class Interview {

    public static List<List<Integer>> getPairsToTargetSum (int[] arr, int target) {
        List<List<Integer>> res = new ArrayList<>();
        if (arr.length < 2) {
            System.out.println("The array input should have length more than 2.");
            return res;
        }
        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> curr = new ArrayList<>();

        //create map
        for (int n : arr) {
            map.put(n, target - n);
        }

        //create the list for sum
        for (int i = 0;i < arr.length;i++) {
            int key = arr[i];
            if (map.containsKey(target - key)) {
                curr.add(key);
                curr.add(target - key);
                map.remove(target - key);
                map.remove(key);
            } else {
                continue;
            }
            if (curr.size() == 2) {
                List<Integer> c_curr = new ArrayList<>(curr);
                res.add(c_curr);
                curr.clear();
            }
        }
        return res;
    }


    public static List<List<Integer>> pairedElements(int arr[], int sum) {
        Arrays.sort(arr);
        int low = 0;
        int high = arr.length - 1;
        List<Integer> curr = new ArrayList<Integer>();
        List<List<Integer>> res = new ArrayList<>();
        while (low < high) {
            if (arr[low] + arr[high] == sum) {
                curr.add(arr[low]);
                curr.add(arr[high]);
            }
            if (curr.size() == 2) {
                List<Integer> c_curr = new ArrayList<>(curr);
                res.add(c_curr);
                curr.clear();
            }
            if (arr[low] + arr[high] > sum) {
                high--;
            } else {
                low++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3 ,4 ,5 ,6};
        int target = 7;

//        List<List<Integer>> res = getPairsToTargetSum(arr, target);
        List<List<Integer>> res = pairedElements(arr, target);
        System.out.println(res.toString());
    }




}


/*
 0 4 1 2

 inputStack 0 4 1 2
 tempStack  0 1 2 4
 temp






    preOrder (BinaryTreeNode root) {
        System.out.println(root.val);
        preOrder(root.left);
        preOrder(root.right);
    }


 */


