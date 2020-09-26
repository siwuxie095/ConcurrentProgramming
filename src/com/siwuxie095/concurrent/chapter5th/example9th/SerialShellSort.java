package com.siwuxie095.concurrent.chapter5th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 11:23:22
 */
@SuppressWarnings("all")
class SerialShellSort {

    public static void serialShellSort(int[] arr) {
        int h = 1;
        // 计算出 h 的最大值
        while (h <= arr.length / 3) {
            h = h * 3 + 1;
        }
        while (h > 0) {
            for (int i = h; i < arr.length; i++) {
                if (arr[i] < arr[i - h]) {
                    int tmp = arr[i];
                    int j = i - h;
                    while (j >= 0 && arr[j] > tmp) {
                        arr[j + h] = arr[j];
                        j -= h;
                    }
                    arr[j + h] = tmp;
                }
            }
            // 计算出下一个 h 值
            h = (h - 1) / 3;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        serialShellSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }

}
