package com.siwuxie095.concurrent.chapter7th.example14th;

import akka.actor.ActorSelection;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.Objects;

/**
 * 管理者 Master
 *
 * @author Jiajing Li
 * @date 2020-10-04 16:57:16
 */
@SuppressWarnings("all")
public class Master extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private PsoValue gBest = null;

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PBestMsg) {
            PsoValue pBest = ((PBestMsg) message).getValue();
            if (Objects.isNull(gBest) || gBest.getValue() < pBest.getValue()) {
                // 更新全局最优，通知所有粒子
                System.out.println(message + System.lineSeparator());
                gBest = pBest;
                ActorSelection actorSelection = this.getContext().actorSelection("akka://ActorBot/user/particle_*");
                actorSelection.tell(new GBestMsg(gBest), this.getSelf());
            }
        } else {
            unhandled(message);
        }
    }
}
