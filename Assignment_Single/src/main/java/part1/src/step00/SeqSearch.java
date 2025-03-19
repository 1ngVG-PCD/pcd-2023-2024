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
        int totalFilesAnalyzed = 0, pdfFilesFound = 0, pdfFilesWithWord = 0;
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
                    return pdfFilesWithWord;
                }
            }

            if (stateManager.getState() == ProgramState.STOP) {
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
            Thread.sleep(100);
        }

        return pdfFilesWithWord; // Ritorna il numero di file in cui la parola è stata trovata
    }
}