package com.siwuxie095.concurrent.chapter7th.example9th;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.agent.Agent;
import akka.dispatch.ExecutionContexts;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-03 17:37:19
 */
@SuppressWarnings("all")
public class RouteBot {

    public static Agent<Boolean> flag = Agent.create(true, ExecutionContexts.global());

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("RouteBot", ConfigFactory.load("routeBot.conf"));
        ActorRef watcher = actorSystem.actorOf(Props.create(Watcher.class), "watcher");
        int i = 1;
        while (flag.get()) {
            watcher.tell(Worker.Message.WORKING, ActorRef.noSender());
            if (i % 10 == 0) {
                watcher.tell(Worker.Message.CLOSE, ActorRef.noSender());
            }
            i++;
            Thread.sleep(100);
        }
    }

}
