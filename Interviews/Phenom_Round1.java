/*
    N number of functions
    call these functions based on the some input

    eg 8 functions
    3 switches
    000
    001
    010
    011
    100
    101
    110
    111

    0000

 */


import java.util.ArrayList;
import java.util.List;

public class Round1 {

    public static void caller(List<Integer> switches) {
        //change the boolean to number
        int n = getNumberFormat(switches);
        System.out.println(n);
//        Map<Integer> map =
        //create delegates for functions and have a map. Based on the value we can call the functions.


    }

    // 1 0 1
    //4
    //5
    private static int getNumberFormat(List<Integer> switches) {
        int ans = 0;
        int multiplier = 2 * (switches.size() - 1) ;
        int index = 0;

        while (index < switches.size()) {
            ans += multiplier * switches.get(index);
            index++;
            multiplier = multiplier / 2;
        }
        return ans;
    }

    public static void func1() {
        System.out.println("func1");

    }
    public static void func2() {
        System.out.println("func2");

    }
    public static void func3() {
        System.out.println("func3");

    }

    public static void main(String[] args) {
        List<Integer> switches = new ArrayList<>();
        switches.add(1);
        switches.add(0);
        switches.add(1);
        caller(switches);
    }



}
