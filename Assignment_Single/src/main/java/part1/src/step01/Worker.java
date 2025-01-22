package part1.src.step01;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;

/**
 * La classe Worker rappresenta un thread consumer che preleva i file PDF dal monitor (bounded buffer),
 * analizza ciascun file per verificare se contiene una parola specifica e aggiorna il risultato nel monitor.
 */
public class Worker extends Thread {
    private final Monitor monitor; // Riferimento al monitor condiviso
    private final String searchWord; // Parola da cercare nei file PDF

    /**
     * Crea un nuovo Worker.
     *
     * @param monitor    Il monitor condiviso utilizzato per accedere ai file PDF.
     * @param searchWord La parola da cercare all'interno dei file PDF.
     */
    public Worker(Monitor monitor, String searchWord) {
        this.monitor = monitor;
        this.searchWord = searchWord;
    }

    /**
     * Esegue il lavoro del thread.
     * Preleva i file PDF dal monitor e verifica se ciascun file contiene la parola specificata.
     * Se la parola viene trovata, aggiorna il contatore dei risultati nel monitor.
     * Gestisce eventuali interruzioni o errori di I/O.
     */
    @Override
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
            // Gestisce l'interruzione del thread
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            // Gestisce errori di I/O durante l'elaborazione dei file
            System.err.println("Errore durante la lettura del file PDF: " + e.getMessage());
        }
    }
}
