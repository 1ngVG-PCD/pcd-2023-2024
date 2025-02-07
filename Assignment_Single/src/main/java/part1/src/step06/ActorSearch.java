package part1.src.step06;

import akka.actor.AbstractActor;

public class ActorSearch extends AbstractActor {


    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }
}
