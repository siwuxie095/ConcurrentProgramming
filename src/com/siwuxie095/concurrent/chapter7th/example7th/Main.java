package com.siwuxie095.concurrent.chapter7th.example7th;

/**
 * @author Jiajing Li
 * @date 2020-10-03 10:01:10
 */
public class Main {

    /**
     * 选择 Actor
     *
     * 在一个 ActorSystem 中，可能存在大量的 Actor。如果才能有效地对大量 Actor 进行批量的管理和通信呢？Akka 提供了
     * 一个 ActorSelection，用来批量进行消息发送。
     *
     * 以 Worker 和 ActorBot 为例，批量生成了大量 Worker。接着要给这些 Worker 发送消息，通过 ActorSelection()
     * 方法提供的选择通配符，可以得到 ActorSelection，它代表所有满足条件的 Worker。然后通过 ActorSelection 实例
     * 就可以向所有 Worker 发送消息。
     */
    public static void main(String[] args) {

    }

}
