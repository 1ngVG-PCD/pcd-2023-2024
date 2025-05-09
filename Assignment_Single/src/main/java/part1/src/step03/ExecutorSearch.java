package part1.src.step03;

import part1.src.logic.OutputUpdater;
import part1.src.logic.Search;
import part1.src.logic.SetState;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorSearch implements Search {
    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {
        ResultManager resultManager = new ResultManager(outputUpdater);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true); // Imposta come daemon per terminare con il programma
        inputThread.start();

        // Usiamo un pool di thread fisici per lo scanner
        ExecutorService scannerExecutor = Executors.newSingleThreadExecutor();
        scannerExecutor.execute(new DirectoryScanner(directory, resultManager, word));
        scannerExecutor.shutdown(); // Chiude dopo il completamento

        // Attendi la terminazione di tutti i task
        scannerExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return resultManager.getResult();
    }
}
