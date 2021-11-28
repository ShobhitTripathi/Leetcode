package com.personal;

public class CountingSort {

    public static void main(String[] args) {
        int[] numbers = {3,3,3,3,2};
        countingSort(numbers);

        for (int i = 0; i < numbers.length;i++) {
            System.out.println(numbers[i]);
        }
    }

    public static void countingSort(int[] arr) {
        int[] output = new int[256];
        int[] count = new int[256];
        int n = arr.length;

        //create count array
        for (int i = 0;i < 256;i++) {
            count[i] = 0;
        }

        //store count of each character
        for (int i = 0;i < n;i++) {
            ++count[arr[i]];
        }

        //position of the value (adding the count frequency)
        for (int i = 1;i < 256;i++) {
            count[i] += count[i - 1];
        }

        // Build the output array
        // To make it stable we are operating in reverse order
        for (int i = n - 1;i >= 0;i--) {
            output[count[arr[i]] - 1] = arr[i];
            --count[arr[i]];
        }

        for (int i = 0;i < n;i++)
            arr[i] = output[i];
    }
}
