package com.siwuxie095.concurrent.chapter7th.example14th;

import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-10-04 16:27:25
 */
@SuppressWarnings("all")
public class Fitness {

    public static double fitness(List<Double> x) {
        double sum = 0;
        for (int i = 1; i < x.size(); i++) {
            sum += Math.sqrt(x.get(i));
        }
        return sum;
    }

}
