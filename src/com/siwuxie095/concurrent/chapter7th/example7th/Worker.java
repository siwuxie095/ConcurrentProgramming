package com.siwuxie095.concurrent.chapter7th.example7th;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Jiajing Li
 * @date 2020-10-03 10:02:07
 */
@SuppressWarnings("all")
public class Worker extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public static enum Message {
        // message type
        WORKING,
        DONE,
        CLOSE;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("Worker is starting");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("Worker is stopping");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Message.WORKING) {
            System.out.println("I am working");
        } else if (message == Message.DONE) {
            System.out.println("stop working");
        } else if (message == Message.CLOSE) {
            System.out.println("I will shutdown");
            this.getSender().tell(Message.CLOSE, this.getSelf());
            this.getContext().stop(this.getSelf());
        } else {
            unhandled(message);
        }
    }

}
