package part1.src.step00;

import part1.src.logic.*;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static part1.src.step00.ScanDirectory.getPdfFiles;

public class SeqSearch implements Search {

    private final ProgramStateManager stateManager = ProgramStateManager.getInstance(); // Ottieni l'istanza singleton

    @Override
    public Integer run(File directoryPath, String word, OutputUpdater outputUpdater) throws InterruptedException {
        List<File> pdfs = getPdfFiles(directoryPath);
        SearchResult result = new SearchResult(0, 0, 0); // Inizializza il record con valori iniziali
        ContainsWord search = new ContainsWord();

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.start();

        for (File pdfFile : pdfs) {
            // Controlla lo stato del programma
            while (stateManager.getState() == ProgramState.PAUSE) {
                try {
                    Thread.sleep(100); // Attendi prima di controllare nuovamente lo stato
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrotto.");
                    return result.pdfFilesWithWord();
                }
            }

            if (stateManager.getState() == ProgramState.STOP) {
                break; // Interrompi il ciclo se lo stato è STOP
            }

            result = new SearchResult(result.totalFilesAnalyzed() + 1, result.pdfFilesFound(), result.pdfFilesWithWord());
            try {
                // Chiama containsWord e incrementa il contatore se la parola è presente
                if (search.containsWord(pdfFile, word)) {
                    result = new SearchResult(result.totalFilesAnalyzed(), result.pdfFilesFound() + 1, result.pdfFilesWithWord() + 1);
                } else {
                    result = new SearchResult(result.totalFilesAnalyzed(), result.pdfFilesFound() + 1, result.pdfFilesWithWord());
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file PDF: " + pdfFile.getName() + ". " + e.getMessage());
            }
            outputUpdater.update(result.totalFilesAnalyzed(), result.pdfFilesFound(), result.pdfFilesWithWord());
            Thread.sleep(0);
        }

        return result.pdfFilesWithWord(); // Ritorna il numero di file in cui la parola è stata trovata
    }
}