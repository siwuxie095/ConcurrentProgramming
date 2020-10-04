package com.siwuxie095.concurrent.chapter7th.example14th;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-04 17:12:06
 */
@SuppressWarnings("all")
public class ActorBot {

    public static final int PARTICLE_COUNT = 100_000;

    public static void main(String[] args) throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        actorSystem.actorOf(Props.create(Master.class), "master");
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            actorSystem.actorOf(Props.create(Particle.class), "particle_" + i);
        }
    }

}
