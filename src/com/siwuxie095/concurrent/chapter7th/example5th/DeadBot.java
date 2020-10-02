package com.siwuxie095.concurrent.chapter7th.example5th;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-02 18:02:02
 */
public class DeadBot {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("DeadBot", ConfigFactory.load("deadBot.conf"));
        ActorRef worker = actorSystem.actorOf(Props.create(Worker.class), "worker");
        actorSystem.actorOf(Props.create(Watcher.class, worker), "watcher");
        worker.tell(Worker.Message.WORKING, ActorRef.noSender());
        worker.tell(Worker.Message.DONE, ActorRef.noSender());
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

}
