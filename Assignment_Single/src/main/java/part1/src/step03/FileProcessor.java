package part1.src.step03;

import part1.src.services.ContainsWord;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * La classe {@code FileProcessor} esegue la ricerca di una parola nei file PDF.
 */
public class FileProcessor implements Runnable {
    private final BlockingQueue<File> fileQueue;
    private final String searchWord;
    private final AtomicInteger resultCount;

    /**
     * Costruttore della classe FileProcessor.
     *
     * @param fileQueue   La coda condivisa contenente i file da processare.
     * @param searchWord  La parola da cercare nei file PDF.
     * @param resultCount Contatore atomico per il numero di file che contengono la parola.
     */
    public FileProcessor(BlockingQueue<File> fileQueue, String searchWord, AtomicInteger resultCount) {
        this.fileQueue = fileQueue;
        this.searchWord = searchWord;
        this.resultCount = resultCount;
    }

    @Override
    public void run() {
        try {
            while (true) {
                File pdfFile = fileQueue.take(); // Prende un file dalla coda
                if (pdfFile == FileSentinel.getSentinel()) {
                    break; // Termina quando riceve la sentinella
                }

                if (containsWord(pdfFile, searchWord)) {
                    resultCount.incrementAndGet();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file PDF: " + e.getMessage());
        }
    }

    private boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }
}
