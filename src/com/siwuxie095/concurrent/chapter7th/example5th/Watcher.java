package com.siwuxie095.concurrent.chapter7th.example5th;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Jiajing Li
 * @date 2020-10-02 17:57:23
 */
@SuppressWarnings("all")
public class Watcher extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public Watcher(ActorRef ref) {
        this.getContext().watch(ref);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Terminated) {
            System.out.println(String.format("%s has terminated, shutting down system", ((Terminated) message).getActor().path()));
            this.getContext().system().terminate();
        } else {
            unhandled(message);
        }
    }
}
