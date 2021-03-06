package com.siwuxie095.concurrent.chapter2nd.example1st;

/**
 * @author Jiajing Li
 * @date 2020-08-23 22:44:03
 */
public class Main {

    /**
     * 线程与进程（Thread And Process）
     *
     * 进程是计算机中的程序关于某数据集合上的一次运行活动，是系统进行资源分配和调度的基本单位，是操作系统的基础。
     * 在早期面向进程设计的计算机结构中，进程是程序的基本执行实体。在当代面向线程设计的计算机结构中，进程是线程
     * 的容器。程序是指令、数据以及组织形式的描述，进程是程序的实体。
     *
     * 进程是一个容器，其中可以容纳若干个线程。线程可以理解为轻量级进程，是程序执行的最小单位。线程之间的关系，
     * 可能是写协作关系，也可能是竞争关系。
     *
     * 使用多线程而不是用多进程去进行并发程序的设计，是因为线程间的切换和调度的成本远远小于进程。
     */
    public static void main(String[] args) {

    }

}
