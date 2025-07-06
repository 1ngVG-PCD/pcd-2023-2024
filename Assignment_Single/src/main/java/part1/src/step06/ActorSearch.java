package part1.src.step06;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import part1.src.logic.*;
import part1.src.services.ContainsWord;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class ActorSearch implements Search {
    private ResultManager resultManager;
    private final ContainsWord containsWord;

    public ActorSearch() {
        this.containsWord = new ContainsWord();
    }

    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) {
        resultManager = new ResultManager(outputUpdater);
        CompletableFuture<Integer> future = new CompletableFuture<>();

        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true);
        inputThread.start();

        ActorSystem system = ActorSystem.create("SearchSystem");
        ActorRef searchActor = system.actorOf(SearchActor.props(resultManager, future), "searchActor");

        searchActor.tell(new Messages.StartSearch(directory, word), ActorRef.noSender());

        try {
            Integer result = future.get(); // Attende il completamento effettivo
            system.terminate();
            return result;
        } catch (Exception e) {
            system.terminate();
            return 0;
        }
    }
}