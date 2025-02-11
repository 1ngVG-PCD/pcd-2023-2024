package part1.src.step00;

import part1.src.ProgramState;
import part1.src.Search;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static part1.src.services.ScanDirectory.getPdfFiles;

public class SeqSearch implements Search {

    private volatile ProgramState state = ProgramState.START;

    /**
     * Metodo `run`: Verifica la presenza di una parola in una lista di file PDF.
     *
     * @param directoryPath La directory contenente i file PDF da processare.
     * @param word La parola da cercare.
     * @return Il numero di file in cui la parola è stata trovata.
     */
    public Integer run(File directoryPath, String word) {
        List<File> pdfs = getPdfFiles(directoryPath);

        int counter = 0; // Contatore per i file in cui la parola è trovata
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
                    return counter;
                }
            }

            if (state == ProgramState.STOP) {
                break; // Interrompi il ciclo se lo stato è STOP
            }

            try {
                // Chiama containsWord e incrementa il contatore se la parola è presente
                if (search.containsWord(pdfFile, word)) {
                    counter++;
                    System.out.println("File trovati: " + counter);
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file PDF: " + pdfFile.getName() + ". " + e.getMessage());
            }
        }

        return counter; // Ritorna il numero di file in cui la parola è stata trovata
    }
}
