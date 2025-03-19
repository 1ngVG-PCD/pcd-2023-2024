package part1.src.step01;

import part1.src.logic.*;

import java.io.File;

/**
 * La classe ConcurrentSearch implementa un'architettura concorrente per la ricerca
 * di una parola specifica all'interno di un insieme di file PDF.
 * Utilizza un monitor (bounded buffer) per sincronizzare i thread worker
 * che processano i file in parallelo.
 */
public class ConcurrentSearch implements Search{

    private final ProgramStateManager stateManager = ProgramStateManager.getInstance(); // Ottieni l'istanza singleton

    /**
     * Avvia la ricerca concorrente della parola specificata in una directory di file PDF.
     * La scansione della directory e l'elaborazione dei file avvengono in parallelo.
     *
     * @param directory La directory da analizzare.
     * @param word      La parola da cercare nei file PDF.
     * @return Il numero di file PDF in cui la parola è stata trovata.
     */
    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {

        int bufferSize = Runtime.getRuntime().availableProcessors() * 2; // Dimensione del buffer circolare
        Monitor monitor = new Monitor(bufferSize);

        int totalFilesAnalyzed = 0, pdfFilesFound = 0, pdfFilesWithWord = 0;

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.start();

        // Avvia la scansione della directory in un thread separato
        Thread scannerThread = new Thread(new DirectoryScanner(directory, monitor));
        scannerThread.start();

        // Avvio dei thread worker tramite la factory
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] workers = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = WorkerFactory.createWorker(monitor, word);
            workers[i].start();
        }

        try {
            // Aspetta che la scansione termini
            scannerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segnala ai worker che non ci sono più file da aggiungere
        monitor.setFinished();

        // Aspetta la terminazione di tutti i worker
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return monitor.getResultCount();
    }
}
