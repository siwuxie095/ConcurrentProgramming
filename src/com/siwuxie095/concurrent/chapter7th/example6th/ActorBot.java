package com.siwuxie095.concurrent.chapter7th.example6th;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-02 20:00:13
 */
public class ActorBot {

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        customStrategy(actorSystem);
        Thread.sleep(30 * 1000);
        actorSystem.terminate();
    }

    private static void customStrategy(ActorSystem actorSystem) {
        ActorRef actor = actorSystem.actorOf(Props.create(Supervisor.class), "supervisor");
        actor.tell(Props.create(Restarter.class), ActorRef.noSender());
        ActorSelection actorSelection = actorSystem.actorSelection("akka://ActorBot/user/supervisor/restarter");
        for (int i = 0; i < 100; i++) {
            actorSelection.tell(Restarter.Message.RESTART, ActorRef.noSender());
        }
    }

}
