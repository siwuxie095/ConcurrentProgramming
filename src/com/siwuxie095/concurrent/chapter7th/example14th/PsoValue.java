package com.siwuxie095.concurrent.chapter7th.example14th;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-10-04 16:13:49
 */
@SuppressWarnings("all")
public final class PsoValue {
    /**
     * 投资方案的总收益
     */
    private final double value;
    /**
     * 投资方案中，每一年分别需要投资多少钱
     */
    private final List<Double> x;

    public PsoValue(double value, List<Double> x) {
        this.value = value;
        /*
         * 会忽略掉 index 0，不使用。转而使用 index 1、2、3、4 分别代表
         * 第一、二、三、四年的投资额，所以初始化容量大小为 5
         */
        List<Double> list = new ArrayList<>(5);
        list.addAll(x);
        this.x = Collections.unmodifiableList(list);
    }

    public double getValue() {
        return value;
    }

    public List<Double> getX() {
        return x;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("value: ")
                .append(value)
                .append(System.lineSeparator())
                .append("x: ")
                .append(x.toString());
        return builder.toString();
    }
}
