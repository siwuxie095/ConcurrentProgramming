package com.siwuxie095.concurrent.chapter7th.example11th;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Jiajing Li
 * @date 2020-10-03 19:45:58
 */
@SuppressWarnings("all")
public class Worker extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public static enum Message {
        // message type
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
        if (message instanceof Integer) {
            int value = (int) message;
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
            this.getSender().tell(value * value, this.getSelf());
        } else if (message == Message.DONE) {
            System.out.println("Stop working");
        } else if (message == Message.CLOSE) {
            System.out.println("I will shutdown");
            this.getSender().tell(Message.CLOSE, this.getSelf());
            this.getContext().stop(this.getSelf());
        } else {
            unhandled(message);
        }
    }
}
