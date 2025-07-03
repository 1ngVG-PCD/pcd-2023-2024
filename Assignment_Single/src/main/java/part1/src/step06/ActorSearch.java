package part1.src.step06;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import part1.src.logic.*;
import part1.src.services.ContainsWord;

import java.io.File;

public class ActorSearch implements Search {
    private ResultManager resultManager;
    private final ContainsWord containsWord;

    public ActorSearch() {
        this.containsWord = new ContainsWord();
    }

    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) {
        resultManager = new ResultManager(outputUpdater);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true);
        inputThread.start();


        return resultManager.getResult();
    }
}

