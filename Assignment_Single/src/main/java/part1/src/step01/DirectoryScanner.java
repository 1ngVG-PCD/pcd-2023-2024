package part1.src.step01;

import java.io.File;

/**
 * La classe DirectoryScanner è responsabile della scansione della directory
 * e dell'invio dei file PDF al monitor per l'elaborazione concorrente.
 */
public class DirectoryScanner implements Runnable {
    private final File directory;
    private final Monitor monitor;

    /**
     * Costruttore della classe DirectoryScanner.
     *
     * @param directory La directory da analizzare.
     * @param monitor   Il monitor condiviso per la sincronizzazione con i worker.
     */
    public DirectoryScanner(File directory, Monitor monitor) {
        this.directory = directory;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        try {
            scanDirectory(directory);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Scansiona la directory in modo ricorsivo e aggiunge i file PDF al monitor.
     *
     * @param dir La directory da analizzare.
     * @throws InterruptedException Se il thread viene interrotto.
     */
    private void scanDirectory(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".pdf")) {
                    monitor.addFile(file);
                }
            }
        }
    }
}
