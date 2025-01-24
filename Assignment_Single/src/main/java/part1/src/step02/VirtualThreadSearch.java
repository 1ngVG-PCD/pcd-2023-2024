package part1.src.step02;

import java.io.File;
import java.util.List;

/**
 * La classe {@code VirtualThreadSearch} implementa una ricerca concorrente basata su Virtual Threads
 * per cercare una parola specifica all'interno di una lista di file PDF.
 * Utilizza un contatore thread-safe ({@code AtomInt}) e un latch personalizzato ({@code Latch})
 * per sincronizzare i thread.
 */
public class VirtualThreadSearch {

    /**
     * Esegue la ricerca di una parola specificata in una lista di file PDF.
     * Ogni file PDF viene elaborato in modo concorrente tramite un Virtual Thread.
     *
     * @param pdfs  La lista dei file PDF da analizzare.
     * @param word  La parola da cercare nei file.
     * @return Il numero di file PDF in cui è stata trovata la parola.
     * @throws InterruptedException Se il thread principale viene interrotto durante l'attesa
     *                              che tutti i Virtual Threads terminino.
     */
    public int run(List<File> pdfs, String word) throws InterruptedException {
        AtomInt resultCount = new AtomInt(0); // Contatore dei risultati
        Latch latch = new Latch(pdfs.size()); // Contatore per sincronizzazione

        // Crea e avvia un Virtual Thread per ogni file PDF
        for (File pdfFile : pdfs) {
            Thread worker = VirtualThreadWorkerFactory.createWorker(pdfFile, word, resultCount, latch);
            worker.start(); // Avvia il Virtual Thread
        }

        // Attende che tutti i thread terminino
        latch.await();
        return resultCount.get();
    }
}

