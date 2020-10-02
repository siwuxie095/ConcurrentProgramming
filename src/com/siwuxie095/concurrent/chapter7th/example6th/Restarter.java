package com.siwuxie095.concurrent.chapter7th.example6th;

import akka.actor.UntypedAbstractActor;

import java.util.Optional;

/**
 * @author Jiajing Li
 * @date 2020-10-02 19:49:01
 */
@SuppressWarnings("all")
public class Restarter extends UntypedAbstractActor {

    public enum Message {
        /**
         * message type
         */
        DONE,
        RESTART;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("preStart hashCode: " + this.hashCode());
    }

    @Override
    public void postStop() throws Exception {
        System.out.println("postStop hashCode: " + this.hashCode());
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        System.out.println("preRestart hashCode: " + this.hashCode());
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        System.out.println("postRestart hashCode: " + this.hashCode());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Message.DONE) {
            this.getContext().stop(this.getSelf());
        } else if (message == Message.RESTART) {
            /// 交替注释如下两行抛异常的代码进行测试
            // 抛出空指针异常，
            System.out.println(((Object) null).toString());
            // 抛出算术异常，默认会被 restart，但这里会 resume
            double value = 0 / 0;
            System.out.println("go on");
        } else {
            unhandled(message);
        }
    }
}
