package com.siwuxie095.concurrent.chapter7th.example3rd;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * @author Jiajing Li
 * @date 2020-10-02 10:41:12
 */
public class GreeterBot {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("GreeterBot", ConfigFactory.load("greeterBot.conf"));
        ActorRef helloWorld = actorSystem.actorOf(Props.create(HelloWorld.class), "helloWorld");
        System.out.println("HelloWold Actor Path: " + helloWorld.path());
    }

}
