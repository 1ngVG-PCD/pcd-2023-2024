package part1.src.step02;

import part1.src.services.ContainsWord;
import java.io.File;
import java.io.IOException;

/**
 * La classe {@code VirtualThreadWorkerFactory} crea Virtual Threads che processano file PDF.
 */
public class VirtualThreadWorkerFactory {

    /**
     * Crea un Virtual Thread che processa un file PDF.
     *
     * @param pdfFile     Il file PDF da analizzare.
     * @param word        La parola da cercare nei file.
     * @param resultCount Il contatore dei risultati condiviso.
     * @param latch       Il latch per sincronizzare i thread.
     * @return Un'istanza Runnable per il Virtual Thread.
     */
    public static Runnable createWorker(File pdfFile, String word, AtomInt resultCount, Latch latch) {
        return () -> {
            try {
                if (containsWord(pdfFile, word)) {
                    resultCount.incrementAndGet(); // Incrementa il contatore dei risultati
                }
            } catch (Exception e) {
                System.err.println("Errore durante l'elaborazione del file PDF: " + pdfFile.getName());
            } finally {
                latch.countDown(); // Segnala la terminazione del worker
            }
        };
    }

    private static boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }
}
