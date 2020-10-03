package com.siwuxie095.concurrent.chapter7th.example9th;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiajing Li
 * @date 2020-10-03 17:25:37
 */
@SuppressWarnings("all")
public class Watcher extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private Router router;

    {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ActorRef worker = this.getContext().actorOf(Props.create(Worker.class), "worker_" + i);
            this.getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Worker.Message) {
          router.route(message, this.getSender());
        } else if (message instanceof Terminated) {
            router = router.removeRoutee(((Terminated) message).actor());
            System.out.println(String.format("%s is closed, remaining routees size = %s", ((Terminated) message).actor().path(), router.routees().size()));
            if (router.routees().size() == 0) {
                System.out.println("Close system");
                RouteBot.flag.send(false);
                this.getContext().system().terminate();
            }
        } else {
            unhandled(message);
        }
    }
}
