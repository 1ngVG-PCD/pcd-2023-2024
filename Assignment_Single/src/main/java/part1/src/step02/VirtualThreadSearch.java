package part1.src.step02;

import java.io.File;

/**
 * La classe {@code VirtualThreadSearch} implementa una ricerca concorrente basata su Virtual Threads.
 * Scansiona la directory e processa i file PDF in parallelo, utilizzando un Monitor personalizzato.
 */
public class VirtualThreadSearch {

    /**
     * Esegue la ricerca di una parola nei file PDF di una directory.
     * La scansione della directory e l'analisi dei file avvengono in parallelo con Virtual Threads.
     *
     * @param directory La directory da analizzare.
     * @param word      La parola da cercare nei file.
     * @return Il numero di file PDF in cui è stata trovata la parola.
     * @throws InterruptedException Se il thread principale viene interrotto.
     */
    public int run(File directory, String word) throws InterruptedException {
        Monitor monitor = new Monitor(10); // Bounded buffer per sincronizzazione
        AtomInt resultCount = new AtomInt(0);
        Latch latch = new Latch(1); // 1 per la scansione, poi aumenteremo con i worker

        // Virtual Thread per la scansione della directory
        Thread.ofVirtual().start(new DirectoryScanner(directory, monitor, latch));

        // Virtual Threads per elaborare i file PDF
        while (true) {
            File pdfFile = monitor.getFile();
            if (pdfFile == FileSentinel.getSentinel()) {
                break; // Termina il loop quando arriva la sentinella
            }

            latch.increment(); // Aggiunge un worker al conteggio del latch
            Thread.ofVirtual().start(VirtualThreadWorkerFactory.createWorker(pdfFile, word, resultCount, latch));
        }

        // Attende la terminazione di tutti i Virtual Threads
        latch.await();
        return resultCount.get();
    }
}
