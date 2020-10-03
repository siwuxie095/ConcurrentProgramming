package com.siwuxie095.concurrent.chapter7th.example10th;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-03 18:38:53
 */
@SuppressWarnings("all")
public class ActorBot {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        ActorRef baby = actorSystem.actorOf(Props.create(Baby.class), "baby");

        baby.tell(Baby.Message.PLAY, ActorRef.noSender());
        baby.tell(Baby.Message.SLEEP, ActorRef.noSender());
        baby.tell(Baby.Message.PLAY, ActorRef.noSender());
        baby.tell(Baby.Message.PLAY, ActorRef.noSender());
        baby.tell(PoisonPill.getInstance(), ActorRef.noSender());
        actorSystem.terminate();
    }

}
