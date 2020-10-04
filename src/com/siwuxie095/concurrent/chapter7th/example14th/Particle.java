package com.siwuxie095.concurrent.chapter7th.example14th;

import akka.actor.ActorSelection;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 粒子 Particle
 *
 * @author Jiajing Li
 * @date 2020-10-04 16:29:24
 */
@SuppressWarnings("all")
public class Particle extends UntypedAbstractActor {

    private final LoggingAdapter log = Logging.getLogger(this.getContext().system(), this);

    private PsoValue pBest = null;

    private PsoValue gBest = null;

    private List<Double> velocity = new ArrayList<>(5);

    private List<Double> x = new ArrayList<>(5);

    private Random r = new Random();

    @Override
    public void preStart() throws Exception {
        for (int i = 0; i < 5; i++) {
            velocity.add(Double.NEGATIVE_INFINITY);
            x.add(Double.NEGATIVE_INFINITY);
        }

        // x1 <= 400
        x.set(1, (double) r.nextInt(401));

        // x2 <= 440 - 1.1*x1
        double max = 400 - 1.1 * x.get(1);
        if (max < 0) {
            max = 0;
        }
        x.set(2, r.nextDouble() * max);

        // x3 <= 484 - 1.21*x1 - 1.1*x2
        max = 484 - 1.21 * x.get(1) - 1.1 * x.get(2);
        if (max < 0) {
            max = 0;
        }
        x.set(3, r.nextDouble() * max);

        // x4 <= 532.4 - 1.331*x1 -1.21*x2 - 1.1*x3
        max = 532.4 - 1.331 * x.get(1) - 1.21 * x.get(2) - 1.1 * x.get(3);
        if (max < 0) {
            max = 0;
        }
        x.set(4, r.nextDouble() * max);

        double newFit = Fitness.fitness(x);
        pBest = new PsoValue(newFit, x);
        PBestMsg pBestMsg = new PBestMsg(pBest);
        ActorSelection actorSelection = this.getContext().actorSelection("akka://ActorBot/user/master");
        actorSelection.tell(pBestMsg, this.getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof GBestMsg) {
            gBest = ((GBestMsg) message).getValue();
            // 更新速度（速度和位置的更新，依据的是标准的粒子群实现）
            for (int i = 1; i < velocity.size(); i++) {
                updateVelocity(i);
            }
            // 更新位置（速度和位置的更新，依据的是标准的粒子群实现）
            for (int i = 1; i < x.size(); i++) {
                updateX(i);
            }
            // 为了避免粒子跑到合理空间之外，强制将粒子约束在合理的区间中。即 一旦发现粒子跑出了定义范围，就重新将它随机化
            validateX();
            double newFit = Fitness.fitness(x);
            if (newFit > pBest.getValue()) {
                pBest = new PsoValue(newFit, x);
                PBestMsg pBestMsg = new PBestMsg(pBest);
                this.getSender().tell(pBestMsg, this.getSelf());
            }
        } else {
            unhandled(message);
        }
    }

    private double updateVelocity(int i) {
        double newV = Math.random() * velocity.get(i)
                + 2 * Math.random() * (pBest.getX().get(i) - x.get(i))
                + 2 * Math.random() * (gBest.getX().get(i) - x.get(i));
        newV = (newV > 0) ? Math.min(newV, 5) : Math.max(newV, -5);
        velocity.set(i, newV);
        return newV;
    }

    private double updateX(int i) {
        double newX =x.get(i) + velocity.get(i);
        x.set(i, newX);
        return newX;
    }

    private void validateX() {
        // x1
        if (x.get(1) > 400) {
            x.set(1, (double) r.nextInt(401));
        }

        // x2
        double max = 400 - 1.1 * x.get(1);
        if (x.get(2) > max || x.get(2) < 0) {
            x.set(2, r.nextDouble() * max);
        }

        // x3
        max = 484 - 1.21 * x.get(1) - 1.1 * x.get(2);
        if (x.get(3) > max || x.get(3) < 0) {
            x.set(3, r.nextDouble() * max);
        }

        // x4
        max = 532.4 - 1.331 * x.get(1) - 1.21 * x.get(2) - 1.1 * x.get(3);
        if (x.get(4) > max || x.get(4) < 0) {
            x.set(4, r.nextDouble() * max);
        }
    }
}
