package com.siwuxie095.concurrent.chapter5th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 10:10:54
 */
@SuppressWarnings("all")
class SerialOddEvenSort {

    public static void serialOddEvenSort(int[] arr) {
        int exchangeFlag = 1;
        int start = 0;
        while (exchangeFlag == 1 || start == 1) {
            exchangeFlag = 0;
            for (int i = start; i < arr.length - 1; i += 2) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                    exchangeFlag = 1;
                }
            }
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        serialOddEvenSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }

}
