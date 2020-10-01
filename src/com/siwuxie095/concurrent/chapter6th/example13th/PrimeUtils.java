package com.siwuxie095.concurrent.chapter6th.example13th;

import java.util.stream.IntStream;

/**
 * @author Jiajing Li
 * @date 2020-10-01 14:29:45
 */
@SuppressWarnings("all")
public class PrimeUtils {

    public static boolean isPrime(int num) {
        int tmp = num;
        if (tmp < 2) {
            return false;
        }
        for (int i = 2; Math.sqrt(tmp) >= i; i++) {
            if (tmp % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        long serialCount = IntStream.range(1, 1_000_000).filter(PrimeUtils::isPrime).count();
        System.out.println(serialCount);
        long parallelCount = IntStream.range(1, 1_000_000).parallel().filter(PrimeUtils::isPrime).count();
        System.out.println(parallelCount);
    }

}
