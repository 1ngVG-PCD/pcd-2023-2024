package part1.src.step03;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * La classe {@code DirectoryScanner} scansiona una directory e invia i file PDF
 * a una coda condivisa per l'elaborazione parallela.
 */
public class DirectoryScanner implements Runnable {
    private final File directory;
    private final BlockingQueue<File> fileQueue;
    private final CountDownLatch latch;

    /**
     * Costruttore della classe DirectoryScanner.
     *
     * @param directory La directory da analizzare.
     * @param fileQueue La coda per i file trovati.
     * @param latch     Il latch per la sincronizzazione della terminazione.
     */
    public DirectoryScanner(File directory, BlockingQueue<File> fileQueue, CountDownLatch latch) {
        this.directory = directory;
        this.fileQueue = fileQueue;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            scanDirectory(directory);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            latch.countDown(); // Segnala che la scansione è terminata
        }
    }

    private void scanDirectory(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".pdf")) {
                    fileQueue.put(file);
                }
            }
        }
    }
}
