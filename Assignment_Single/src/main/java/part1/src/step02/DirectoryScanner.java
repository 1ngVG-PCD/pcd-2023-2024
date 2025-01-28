package part1.src.step02;

import java.io.File;

/**
 * La classe {@code DirectoryScanner} scansiona una directory e invia i file PDF trovati
 * a un Monitor condiviso, da cui i worker prelevano i file per la ricerca.
 */
public class DirectoryScanner implements Runnable {
    private final File directory;
    private final Monitor monitor;
    private final Latch latch;

    /**
     * Costruttore della classe DirectoryScanner.
     *
     * @param directory La directory da analizzare.
     * @param monitor   Il Monitor condiviso per sincronizzazione tra produttore e worker.
     * @param latch     Il latch per sincronizzare la terminazione.
     */
    public DirectoryScanner(File directory, Monitor monitor, Latch latch) {
        this.directory = directory;
        this.monitor = monitor;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            scanDirectory(directory);
            monitor.addFile(FileSentinel.getSentinel()); // Segnala la fine della scansione
            latch.countDown(); // Segnala che la scansione è terminata
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void scanDirectory(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".pdf")) {
                    monitor.addFile(file); // Mette il file nel buffer del monitor
                }
            }
        }
    }
}
