package com.siwuxie095.concurrent.chapter6th.example12th;

import java.util.Arrays;

/**
 * @author Jiajing Li
 * @date 2020-09-30 22:18:23
 */
@SuppressWarnings("all")
public class Test {

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7};
        Arrays.stream(arr).forEach(x -> System.out.println(x));
    }

}
