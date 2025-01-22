package part1.src.step01;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;

public class Worker extends Thread {
    private final Monitor monitor;
    private final String searchWord;

    public Worker(Monitor monitor, String searchWord) {
        this.monitor = monitor;
        this.searchWord = searchWord;
    }

    public void run() {
        ContainsWord search = new ContainsWord();
        try {
            File pdfFile;
            while ((pdfFile = monitor.getFile()) != null) {
                if (search.containsWord(pdfFile, searchWord)) {
                    monitor.incrementResultCount();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file PDF: " + e.getMessage());
        }
    }
}

