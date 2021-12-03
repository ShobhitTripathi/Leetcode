import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TargetSum {

    static ArrayList<ArrayList<Integer> > combinationSum(ArrayList<Integer> arr, int sum) {
        ArrayList<ArrayList<Integer> > ans = new ArrayList<>();
        ArrayList<Integer> temp = new ArrayList<>();

        // first do hashing since hashset does not always
        // sort
        // removing the duplicates using HashSet and
        // Sorting the arrayList
        Set<Integer> set = new HashSet<>(arr);
        arr.clear();
        arr.addAll(set);
        Collections.sort(arr);

        findNumbers(ans, arr, sum, 0, temp);
        return ans;
    }

    static void findNumbers(ArrayList<ArrayList<Integer> > ans, ArrayList<Integer> arr,
                            int sum, int index, ArrayList<Integer> temp) {
        if (sum == 0) {
            // Adding deep copy of list to ans
            ans.add(new ArrayList<>(temp));
            return;
        }

        for (int i = index; i < arr.size(); i++) {
            // checking that sum does not become negative
            if ((sum - arr.get(i)) >= 0) {
                // adding element which can contribute to
                // sum
                temp.add(arr.get(i));
                findNumbers(ans, arr, sum - arr.get(i), i, temp);
                // removing element from list (backtracking)
                temp.remove(arr.get(i));
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(2);
        arr.add(3);
        arr.add(5);
        arr.add(7);

        int sum = 7;
        ArrayList<ArrayList<Integer>> res = combinationSum(arr, sum);
        System.out.println(res.toString());
    }
}
