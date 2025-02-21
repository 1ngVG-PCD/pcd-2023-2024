package part1.src.step00;

import part1.src.logic.OutputUpdater;
import part1.src.logic.ProgramState;
import part1.src.logic.Search;
import part1.src.services.ContainsWord;
import part1.src.services.SetState;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static part1.src.step00.ScanDirectory.getPdfFiles;

public class SeqSearch implements Search {

    private volatile ProgramState state = ProgramState.START;

    /**
     * Metodo `run`: Verifica la presenza di una parola in una lista di file PDF.
     *
     * @param directoryPath La directory contenente i file PDF da processare.
     * @param word La parola da cercare.
     * @return Il numero di file in cui la parola è stata trovata.
     */
    public Integer run(File directoryPath, String word, OutputUpdater outputUpdater) {
        List<File> pdfs = getPdfFiles(directoryPath);
        int totalFilesAnalyzed = 0, pdfFilesFound = 0, pdfFilesWithWord = 0;
        ContainsWord search = new ContainsWord();

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState(state));
        inputThread.start();

        for (File pdfFile : pdfs) {

            // Controlla lo stato del programma
            while (state == ProgramState.PAUSE) {
                try {
                    Thread.sleep(100); // Attendi prima di controllare nuovamente lo stato
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrotto.");
                    return pdfFilesWithWord;
                }
            }

            if (state == ProgramState.STOP) {
                break; // Interrompi il ciclo se lo stato è STOP
            }
            totalFilesAnalyzed++;
            try {
                // Chiama containsWord e incrementa il contatore se la parola è presente
                if (search.containsWord(pdfFile, word)) {
                    pdfFilesWithWord++;
                }
                pdfFilesFound++;
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file PDF: " + pdfFile.getName() + ". " + e.getMessage());
            }
            outputUpdater.update(totalFilesAnalyzed, pdfFilesFound, pdfFilesWithWord);
        }

        return pdfFilesWithWord; // Ritorna il numero di file in cui la parola è stata trovata
    }
}
