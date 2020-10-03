package com.siwuxie095.concurrent.chapter7th.example11th;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * @author Jiajing Li
 * @date 2020-10-03 19:52:51
 */
@SuppressWarnings("all")
public class Printer extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);


    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            System.out.println("Printer: " + message);
        } else if (message == Worker.Message.DONE) {
            log.info("Stop working");
        } else if (message == Worker.Message.CLOSE) {
            log.info("I will shutdown");
            this.getSender().tell(Worker.Message.CLOSE, this.getSelf());
            this.getContext().stop(this.getSelf());
        } else {
            unhandled(message);
        }
    }
}
