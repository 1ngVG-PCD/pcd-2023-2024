package part1.src.step01;

import java.io.File;
import java.util.List;

/**
 * La classe ConcurrentSearch implementa un'architettura concorrente per la ricerca
 * di una parola specifica all'interno di un insieme di file PDF.
 * Utilizza un bounded buffer (Monitor) per sincronizzare i thread worker
 * che processano i file in parallelo.
 */
public class ConcurrentSearch {

    /**
     * Avvia la ricerca concorrente della parola specificata in una lista di file PDF.
     * Utilizza un monitor (bounded buffer) per sincronizzare l'accesso ai file tra
     * produttore (thread principale) e consumatori (worker).
     *
     * @param pdfs  La lista di file PDF da analizzare.
     * @param word  La parola da cercare nei file PDF.
     * @return Il numero di file PDF in cui la parola è stata trovata.
     */
    public int run(List<File> pdfs, String word) {
        // Determina la dimensione del buffer circolare in base al numero di file e thread
        int bufferSize = Math.min(pdfs.size(), Runtime.getRuntime().availableProcessors() * 2);
        Monitor monitor = new Monitor(bufferSize);

        // Avvio dei thread worker tramite la factory
        int numThreads = Runtime.getRuntime().availableProcessors(); // Numero di core disponibili
        Thread[] workers = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            workers[i] = WorkerFactory.createWorker(monitor, word);
            workers[i].start();
        }

        try {
            // Aggiunge i file PDF al monitor per il processamento
            for (File file : pdfs) {
                monitor.addFile(file);
            }
        } catch (InterruptedException e) {
            // Gestisce eventuali interruzioni del thread principale
            Thread.currentThread().interrupt();
        }

        // Segnala al monitor che non ci sono più file da aggiungere
        monitor.setFinished();

        // Aspetta la terminazione di tutti i thread worker
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                // Gestisce interruzioni durante l'attesa
                Thread.currentThread().interrupt();
            }
        }

        // Restituisce il numero totale di file in cui la parola è stata trovata
        return monitor.getResultCount();
    }
}

