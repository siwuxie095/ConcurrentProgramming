package com.siwuxie095.concurrent.chapter7th.example11th;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author Jiajing Li
 * @date 2020-10-03 19:53:03
 */
@SuppressWarnings("all")
public class ActorBot {

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        ActorRef worker = actorSystem.actorOf(Props.create(Worker.class), "worker");
        ActorRef printer = actorSystem.actorOf(Props.create(Printer.class), "printer");

        // 等待 future 返回
        Future<Object> future = Patterns.ask(worker, 5, 1500);
        int res = (int) Await.result(future, Duration.create(6, TimeUnit.SECONDS));
        System.out.println("return:" + res);

        // 直接导向其他 Actor，pipe() 不会等待
        future = Patterns.ask(worker, 6, 1500);
        Patterns.pipe(future, actorSystem.dispatcher()).to(printer);
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
        Thread.sleep(10 * 1000);
        actorSystem.terminate();
    }

}
