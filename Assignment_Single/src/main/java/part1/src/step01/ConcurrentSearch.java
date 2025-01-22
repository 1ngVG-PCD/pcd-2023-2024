package part1.src.step01;

import java.io.File;
import java.util.List;

public class ConcurrentSearch {

    public int run(List<File> pdfs, String word) {
        int bufferSize = Math.min(pdfs.size(), Runtime.getRuntime().availableProcessors() * 2); // Dimensione del buffer circolare
        Monitor monitor = new Monitor(bufferSize);

        // Avvio dei thread worker
        int numThreads = Runtime.getRuntime().availableProcessors();
        Thread[] workers = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = new Worker(monitor, word);
            workers[i].start();
        }

        try {
            for (File file : pdfs) {
                monitor.addFile(file); // Aggiungi file al buffer
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Segnala al monitor che non ci sono più file da aggiungere
        monitor.setFinished();

        // Aspetta la terminazione dei worker
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

