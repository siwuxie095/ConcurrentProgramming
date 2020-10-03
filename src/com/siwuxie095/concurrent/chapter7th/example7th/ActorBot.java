package com.siwuxie095.concurrent.chapter7th.example7th;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-10-03 10:03:38
 */
@SuppressWarnings("all")
public class ActorBot {

    private static final int WORKER_COUNT = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        List<ActorRef> workers = new ArrayList<>();
        for (int i = 0; i < WORKER_COUNT; i++) {
            workers.add(actorSystem.actorOf(Props.create(Worker.class), "worker_" + i));
        }
        ActorSelection actorSelection = actorSystem.actorSelection("akka://ActorBot/user/worker_*");
        actorSelection.tell(Worker.Message.WORKING, ActorRef.noSender());
        Thread.sleep(30 * 1000);
        actorSystem.terminate();
    }

}
