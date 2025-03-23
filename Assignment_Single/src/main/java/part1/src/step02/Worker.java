package part1.src.step02;

import part1.src.logic.ProgramState;
import part1.src.logic.ProgramStateManager;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;

/**
 * La classe Worker rappresenta un thread consumer che preleva i file PDF dal monitor (bounded buffer),
 * analizza ciascun file per verificare se contiene una parola specifica e aggiorna il risultato nel monitor.
 */
public class Worker extends Thread {
    private final Monitor monitor; // Riferimento al monitor condiviso
    private final File pdfFile;

    /**
     * Crea un nuovo Worker.
     *
     * @param monitor    Il monitor condiviso utilizzato per accedere ai file PDF.
     * @param pdfFile Il file in cui cercare la parola
     */
    public Worker(Monitor monitor, File pdfFile) {
        this.monitor = monitor;
        this.pdfFile = pdfFile;
    }

    private final ProgramStateManager stateManager = ProgramStateManager.getInstance(); // Ottieni l'istanza singleton

    /**
     * Esegue il lavoro del thread.
     * Preleva i file PDF dal monitor e verifica se ciascun file contiene la parola specificata.
     * Se la parola viene trovata, aggiorna il contatore dei risultati nel monitor.
     * Gestisce eventuali interruzioni o errori di I/O.
     */
    @Override
    public void run() {
        ContainsWord search = new ContainsWord();
        String searchWord = monitor.getWord();

        if (stateManager.getState() == ProgramState.START) {
            monitor.incrementFilesAnalyzed();
            try {
                if (search.containsWord(pdfFile, searchWord)) {
                    monitor.incrementFilesWord();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (stateManager.getState() == ProgramState.STOP) {
            Thread.currentThread().interrupt();
        } else {
            while (stateManager.getState() == ProgramState.PAUSE) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrotto.");
                }
            }
        }
    }
}
