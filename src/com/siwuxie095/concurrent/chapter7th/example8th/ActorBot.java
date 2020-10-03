package com.siwuxie095.concurrent.chapter7th.example8th;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Jiajing Li
 * @date 2020-10-03 10:53:55
 */
@SuppressWarnings("all")
public class ActorBot {

    public static void main(String[] args) throws TimeoutException {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        ActorRef worker = actorSystem.actorOf(Props.create(Worker.class), "worker");

        final Inbox inbox = Inbox.create(actorSystem);
        inbox.watch(worker);
        inbox.send(worker, Worker.Message.WORKING);
        inbox.send(worker, Worker.Message.DONE);
        inbox.send(worker, Worker.Message.CLOSE);

        while (true) {
            Object message = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
            if (message == Worker.Message.CLOSE) {
                System.out.println("inbox received: worker is closed");
            } else if (message instanceof Terminated) {
                System.out.println("inbox received: worker is dead");
                actorSystem.terminate();
                break;
            } else {
                System.out.println(message);
            }
        }
    }

}
