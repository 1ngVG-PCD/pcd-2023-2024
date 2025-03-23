package part1.src.step02;

import part1.src.logic.OutputUpdater;
import part1.src.logic.Search;
import part1.src.logic.SetState;

import java.io.File;

public class VirtualThreadSearch implements Search {
    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {

        Monitor monitor = new Monitor(word, outputUpdater);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.start();

        // Avvia la scansione della directory in un virtual thread separato che gestisce la creazione di altri virtual thread
        Thread scannerThread = Thread.startVirtualThread(new DirectoryScanner(directory, monitor));

        try {
            // Aspetta che la scansione termini
            scannerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return monitor.getResult();
    }

}
