package com.siwuxie095.concurrent.chapter7th.example13th;

import akka.actor.UntypedActor;
import akka.transactor.Coordinated;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * @author Jiajing Li
 * @date 2020-10-03 22:40:12
 */
@SuppressWarnings("all")
public class Company extends UntypedActor {

    private Ref.View<Integer> balance = STM.newRef(100);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Coordinated) {
            final Coordinated coord = (Coordinated) message;
            final int amount = (int) coord.getMessage();
            ActorBot.employee.tell(coord.coordinate(amount), this.getSelf());
            try {
                coord.atomic(() -> {
                    if (balance.get() < amount) {
                        throw new RuntimeException(String.format("balance %s is less than amount %s", balance.get(), amount));
                    }
                    STM.increment(balance, -amount);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("GetBalance".equals(message)) {
            this.getSender().tell(balance.get(), this.getSelf());
        } else {
            unhandled(message);
        }
    }
}
