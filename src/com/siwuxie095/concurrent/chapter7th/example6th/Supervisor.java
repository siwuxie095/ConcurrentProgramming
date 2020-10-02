package com.siwuxie095.concurrent.chapter7th.example6th;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedAbstractActor;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;


/**
 * @author Jiajing Li
 * @date 2020-10-02 19:38:03
 */
@SuppressWarnings("all")
public class Supervisor extends UntypedAbstractActor {

    private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES), t -> {
        if (t instanceof ArithmeticException) {
            System.out.println("meet ArithmeticException, resume");
            return SupervisorStrategy.resume();
        } else if (t instanceof NullPointerException) {
            System.out.println("meet NullPointerException, restart");
            return SupervisorStrategy.restart();
        } else if (t instanceof IllegalArgumentException) {
            System.out.println("meet IllegalArgumentException, stop");
            return SupervisorStrategy.stop();
        } else {
            System.out.println("meet other exception, escalate");
            return SupervisorStrategy.escalate();
        }
    });

    /**
     * 使用自定义的监督策略
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Props) {
            this.getContext().actorOf((Props) message, "restarter");
        } else {
            unhandled(message);
        }
    }
}
