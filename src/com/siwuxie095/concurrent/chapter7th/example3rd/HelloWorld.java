package com.siwuxie095.concurrent.chapter7th.example3rd;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;

/**
 * @author Jiajing Li
 * @date 2020-10-02 10:48:39
 */
@SuppressWarnings("all")
public class HelloWorld extends UntypedAbstractActor {

    private ActorRef greeter;

    @Override
    public void preStart() throws Exception {
        greeter = this.getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter Actor Path: " + greeter.path());
        greeter.tell(Greeter.Message.GREET, this.getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Greeter.Message.DONE) {
            greeter.tell(Greeter.Message.GREET, this.getSelf());
            this.getContext().stop(this.getSelf());
        } else {
            this.unhandled(message);
        }
    }
}
