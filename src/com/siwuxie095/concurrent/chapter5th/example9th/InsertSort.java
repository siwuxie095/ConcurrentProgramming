package com.siwuxie095.concurrent.chapter5th.example9th;

/**
 * @author Jiajing Li
 * @date 2020-09-26 11:07:11
 */
@SuppressWarnings("all")
class InsertSort {

    public static void insertSort(int[] arr) {
        int len = arr.length;
        int j, i, key;
        for (i = 1; i < len; i++) {
            // key 为准备要插入的元素
            key = arr[i];
            j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            // 找到合适的位置，插入 key
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        int[] arr = new int[] {7, 4, 3, 8, 9, 10, 1};
        insertSort(arr);
        for (int val : arr) {
            System.out.print(val + " ");
        }
    }

}
