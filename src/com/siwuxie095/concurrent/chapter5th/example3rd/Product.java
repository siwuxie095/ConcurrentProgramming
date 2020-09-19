package com.siwuxie095.concurrent.chapter5th.example3rd;

/**
 * @author Jiajing Li
 * @date 2020-09-19 21:21:57
 */
@SuppressWarnings("all")
public final class Product {

    /**
     * class 上的 final 确保无子类
     */

    /**
     * final 保证属性不会被二次赋值
     */
    private final String no;

    private final String name;

    private final double price;

    /**
     * 在创建对象时，必须指定数据
     *
     * 因为创建之后，无法进行修改
     */
    public Product(String no, String name, double price) {
        super();
        this.no = no;
        this.name = name;
        this.price = price;
    }

    public String getNo() {
        return no;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
