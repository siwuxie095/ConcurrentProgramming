package com.siwuxie095.concurrent.chapter7th.example13th;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * @author Jiajing Li
 * @date 2020-10-03 23:44:00
 */
@SuppressWarnings("all")
public class Employee extends UntypedActor {

    private Ref.View<Integer> balance = STM.newRef(50);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Coordinated) {
            final Coordinated coord = (Coordinated) message;
            final int amount = (int) coord.getMessage();

            try {
                coord.atomic(() -> {
                    STM.increment(balance, amount);
                });
            } catch (Exception e) {}
        } else if ("GetBalance".equals(message)) {
            this.getSender().tell(balance.get(), this.getSelf());
        } else {
            unhandled(message);
        }
    }
}
