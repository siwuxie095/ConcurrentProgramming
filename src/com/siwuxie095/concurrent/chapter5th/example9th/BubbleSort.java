package com.siwuxie095.concurrent.chapter5th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 09:47:54
 */
@SuppressWarnings("all")
class BubbleSort {

    public static void bubbleSort(int[] arr) {
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        bubbleSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }

}
