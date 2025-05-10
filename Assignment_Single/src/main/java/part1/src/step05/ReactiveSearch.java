package part1.src.step05;

import part1.src.logic.OutputUpdater;
import part1.src.logic.Search;
import part1.src.logic.SetState;

import java.io.File;

public class ReactiveSearch implements Search {
    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {

        ResultManager resultManager = new ResultManager(outputUpdater);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true); // Imposta come daemon per terminare con il programma
        inputThread.start();

        // Inserire la Logica per la gestione del task in modo reattivo

        return resultManager.getResult();
    }
}
