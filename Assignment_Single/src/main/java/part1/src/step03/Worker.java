package part1.src.step03;

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
    private final ResultManager resultManager; // Riferimento al monitor condiviso
    private final String searchWord; // Parola da cercare nei file PDF

    /**
     * Crea un nuovo Worker.
     *
     * @param resultManager Classe che gestisce il record
     * @param searchWord La parola da cercare all'interno dei file PDF.
     */
    public Worker(ResultManager resultManager, String searchWord) {
        this.resultManager = resultManager;
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

        ProgramStateManager stateManager = ProgramStateManager.getInstance();
        if (stateManager.getState() != ProgramState.START) {
            return;
        }

        ContainsWord search = new ContainsWord();
        try {
            resultManager.incrementFilesAnalyzed();
            if (search.containsWord(pdfFile, searchWord)) {
                resultManager.incrementFilesWord();
            }
        } catch (IOException e) {
            System.err.println("Errore durante la lettura del file PDF: " + e.getMessage());
        }
    }
}
