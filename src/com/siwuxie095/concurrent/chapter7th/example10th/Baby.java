package com.siwuxie095.concurrent.chapter7th.example10th;

import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

/**
 * @author Jiajing Li
 * @date 2020-10-03 18:38:01
 */
@SuppressWarnings("all")
public class Baby extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    public static enum Message {
        // message type
        SLEEP,
        PLAY,
        CLOSE;
    }

    PartialFunction<Object, BoxedUnit> angry = new PartialFunction<Object, BoxedUnit>() {

        @Override
        public BoxedUnit apply(Object param) {
            System.out.println("angryApply: " + param);
            if (param == Message.SLEEP) {
                getSender().tell("I am already angry", getSelf());
                System.out.println("I am already angry");
            } else if (param == Message.PLAY) {
                System.out.println("I like playing");
                getContext().become(happy);
            }
            return null;
        }

        @Override
        public boolean isDefinedAt(Object x) {
            return true;
        }

    };


    PartialFunction<Object, BoxedUnit> happy = new PartialFunction<Object, BoxedUnit>() {

        @Override
        public BoxedUnit apply(Object param) {
            System.out.println("happyApply: " + param);
            if (param == Message.PLAY) {
                getSender().tell("I am already happy :-)", getSelf());
                System.out.println("I am already happy :-)");
            } else if (param == Message.SLEEP) {
                System.out.println("I don't want to sleep");
                getContext().become(angry);
            }
            return null;
        }

        @Override
        public boolean isDefinedAt(Object x) {
            return true;
        }


    };


    @Override
    public void onReceive(Object message) throws Throwable {
        System.out.println("onReceive: " + message);
        if (message == Message.SLEEP) {
            this.getContext().become(angry);
        } else if (message == Message.PLAY) {
            this.getContext().become(happy);
        } else {
            unhandled(message);
        }
    }
}
