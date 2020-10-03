package com.siwuxie095.concurrent.chapter7th.example12th;

import akka.actor.UntypedAbstractActor;
import akka.dispatch.Mapper;
import scala.concurrent.Future;

/**
 * @author Jiajing Li
 * @date 2020-10-03 21:47:00
 */
@SuppressWarnings("all")
public class Counter extends UntypedAbstractActor {

    private Mapper addMapper = new Mapper<Integer, Integer>() {
        @Override
        public Integer apply(Integer value) {
            return value + 1;
        }
    };

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof Integer) {
            for (int i = 0; i < 10_000; i++) {
                // 现在希望能够知道 Future 何时结束
                Future<Integer> future = AgentBot.countAgent.alter(addMapper);
                AgentBot.futures.add(future);
            }
            this.getContext().stop(this.getSelf());
        } else {
            unhandled(message);
        }
    }
}
