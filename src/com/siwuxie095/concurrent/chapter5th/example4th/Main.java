package com.siwuxie095.concurrent.chapter5th.example4th;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-20 16:11:37
 */
@SuppressWarnings("all")
public class Main {

    /**
     * 生产者-消费者模式
     *
     * 生产者-消费者模式是一个经典的多线程设计模式，它为多线程间的协作提供了良好的解决方案。在生产者-消费者模式中，
     * 通常有两类线程，即若干个生产者线程和若干个消费者线程。生产者线程负责提交用户请求，消费者线程则负责具体处理
     * 生产者提交的任务。生产者和消费者之间则通过共享内存缓冲区进行通信。即 生产者线程将任务提交到共享内存缓冲区，
     * 消费者线程并不直接与生产者线程通信，而在共享内存缓冲区中获取任务，并进行处理。
     *
     * 注意：生产者-消费者模式中的内存缓冲区的主要功能是数据在多线程间的共享。此外，通过该缓冲区，可以缓解生产者和
     * 消费者间的性能差。
     *
     * 生产者-消费者模式的核心组件是共享内存缓冲区，它作为生产者和消费者间的通信桥梁，避免了生产者和消费者的直接通
     * 信，从而将生产者和消费者进行解耦。生产者不需要知道消费者的存在，消费者也不需要知道生产者的存在。
     *
     * 同时，由于内存缓冲区的存在，允许生产者和消费者在执行速度上存在时间差，无论是生产者在某一局部时间内速度高于消
     * 费者，还是消费者在局部时间内高于生产者，都可以通过共享内存缓冲区得到缓解，确保系统正常运行。
     *
     * 以 Producer、Consumer、ProducedConsumedData 等类为例，展示了一种具体的结构，实现了一个基于生产者-消费
     * 者模式的求整数平方的并行程序，其中的主要角色如下：
     *
     * （1）Producer：即 生产者。用于提交任务请求，构造任务数据，并装入内存缓冲区。
     * （2）Consumer：即 消费者。在内存缓冲区中提取任务数据，并处理任务。
     * （3）BlockingQueue：即 内存缓冲区，也叫任务队列。缓存生产者提交的任务数据，供消费者使用。
     * （4）ProducedConsumedData：即 任务。生产者向内存缓冲区提交的数据结构。
     * （5）Main：即 客户端。使用生产者和消费者的客户端。
     *
     * 其中，BlockingQueue 充当了共享内存缓冲区，用于维护任务队列。ProducedConsumedData 对象表示一个生产任务，
     * 或相关任务的数据。生产者对象和消费者对象均引用同一个 BlockingQueue 实例。
     *
     * 生产者负责创建 ProducedConsumedData 对象，并将它加入 BlockingQueue 中，消费者则从 BlockingQueue 中
     * 获取 ProducedConsumedData 对象。
     *
     * 注意：生产者-消费者模式很好地对生产者线程和消费者线程进行解耦，优化了系统整体结构。同时，由于缓冲区的作用，
     * 允许生产者线程和消费者线程存在执行上的性能差异，从一定程度上缓解了性能瓶颈对系统性能的影响。
     */
    public static void main(String[] args) throws InterruptedException {
        // 内存缓冲区（任务队列）
        BlockingQueue<ProducedConsumedData> queue = new ArrayBlockingQueue<>(10);
        // 建立生产者
        Producer producer1 = new Producer(queue);
        Producer producer2 = new Producer(queue);
        Producer producer3 = new Producer(queue);
        // 建立消费者
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        Consumer consumer3 = new Consumer(queue);
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(producer1);
        executor.execute(producer2);
        executor.execute(producer3);
        executor.execute(consumer1);
        executor.execute(consumer2);
        executor.execute(consumer3);
        Thread.sleep(10 * 1000);
        // 停止生产者
        producer1.stop();
        producer2.stop();
        producer3.stop();
        Thread.sleep(3 * 1000);
        System.out.println("all producer stopped");
        // 停止消费者
        consumer1.stop();
        consumer2.stop();
        consumer3.stop();
        Thread.sleep(3 * 1000);
        System.out.println("all consumer stopped");
        executor.shutdown();
    }

}
