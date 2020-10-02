package com.siwuxie095.concurrent.chapter7th.example3rd;

import akka.actor.UntypedAbstractActor;

/**
 * @author Jiajing Li
 * @date 2020-10-02 10:41:02
 */
@SuppressWarnings("all")
public class Greeter extends UntypedAbstractActor {

    public static enum Message {
        /**
         * message type
         */
        GREET,
        DONE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Message.GREET) {
            System.out.println("Hello World");
            this.getSender().tell(Message.DONE, this.getSelf());
        } else {
            this.unhandled(message);
        }
    }
}
