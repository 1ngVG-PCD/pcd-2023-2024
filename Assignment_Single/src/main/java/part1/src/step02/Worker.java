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

    private final ProgramStateManager stateManager = ProgramStateManager.getInstance(); // Ottieni l'istanza singleton

    /**
     * Esegue il lavoro del thread.
     * Preleva i file PDF dal monitor e verifica se ciascun file contiene la parola specificata.
     * Se la parola viene trovata, aggiorna il contatore dei risultati nel monitor.
     * Gestisce eventuali interruzioni o errori di I/O.
     */

    public void run(File pdfFile) {
        monitor.workerStarted();
        ContainsWord search = new ContainsWord();
        try {
            if (stateManager.getState()== ProgramState.START){
                monitor.incrementFilesAnalyzed();
                if (search.containsWord(pdfFile, searchWord)){
                    monitor.incrementFilesWord();
                }
            }  else if ((stateManager.getState()== ProgramState.STOP)){
                Thread.currentThread().interrupt();
            } else {
                while(stateManager.getState()==ProgramState.PAUSE){
                    try {
                        Thread.sleep(100); // Attendi prima di controllare nuovamente lo stato
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Thread interrotto.");
                    }
                }
            }
        } catch (IOException e) {
            // Gestisce errori di I/O durante l'elaborazione dei file
            System.err.println("Errore durante la lettura del file PDF: " + e.getMessage());
        }finally {
            monitor.workerFinished();
        }
    }
}
