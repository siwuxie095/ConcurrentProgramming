package com.siwuxie095.concurrent.chapter7th.example13th;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

/**
 * @author Jiajing Li
 * @date 2020-10-03 23:38:13
 */
@SuppressWarnings("all")
public class ActorBot {

    public static ActorRef company = null;

    public static ActorRef employee = null;

    public static void main(String[] args) throws Exception {
        ActorSystem actorSystem = ActorSystem.create("ActorBot", ConfigFactory.load("actorBot.conf"));
        company = actorSystem.actorOf(Props.create(Company.class), "company");
        employee = actorSystem.actorOf(Props.create(Employee.class), "employee");

        Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

        for (int i = 1; i < 20; i++) {
            company.tell(new Coordinated(i, timeout), ActorRef.noSender());
            Thread.sleep(200);
            Integer companyBalance = (Integer) Await.result(Patterns.ask(company, "GetBalance", timeout), timeout.duration());
            Integer employeeBalance = (Integer) Await.result(Patterns.ask(employee, "GetBalance", timeout), timeout.duration());

            System.out.println("companyBalance: " + companyBalance);
            System.out.println("employeeBalance: " + employeeBalance);
            System.out.println("===== ===== ===== ===== =====");
        }
    }

}
