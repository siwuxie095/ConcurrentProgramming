package com.siwuxie095.concurrent.chapter7th.example12th;

import akka.actor.*;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jiajing Li
 * @date 2020-10-03 21:47:21
 */
@SuppressWarnings("all")
public class AgentBot {

    public static Agent<Integer> countAgent = Agent.create(0, ExecutionContexts.global());

    public static ConcurrentLinkedQueue<Future<Integer>> futures = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws TimeoutException {
        ActorSystem actorSystem = ActorSystem.create("AgentBot", ConfigFactory.load("agentBot.conf"));
        ActorRef[] counters = new ActorRef[10];
        for (int i = 0; i < counters.length; i++) {
            counters[i] = actorSystem.actorOf(Props.create(Counter.class), "counter_" + i);
        }

        final Inbox inbox = Inbox.create(actorSystem);
        for (int i = 0; i < counters.length; i++) {
            inbox.send(counters[i], 1);
            inbox.watch(counters[i]);
        }

        int closeCount = 0;
        // 等待所有 Actor 全部结束
        while (true) {
            Object message = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if (message instanceof Terminated) {
                closeCount++;
                if (closeCount == counters.length) {
                    break;
                }
            } else {
                System.out.println(message);
            }
        }

        // 等待所有的累加线程完成，因为它们都是异步的
        Futures.sequence(futures, actorSystem.dispatcher()).onComplete(new OnComplete<Iterable<Integer>>() {
            @Override
            public void onComplete(Throwable failure, Iterable<Integer> success) throws Throwable {
                System.out.println("countAgent: " + countAgent.get());
                actorSystem.terminate();
            }
        }, actorSystem.dispatcher());

    }

}
