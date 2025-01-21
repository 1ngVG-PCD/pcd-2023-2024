package part1.src.step00;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SeqSearch {


    /**
     * Metodo `run`: Verifica la presenza di una parola in una lista di file PDF.
     *
     * @param pdfs La lista di file PDF da processare.
     * @param word La parola da cercare.
     * @return Il numero di file in cui la parola è stata trovata.
     */
    public int run(List<File> pdfs, String word) {
        int counter = 0; // Contatore per i file in cui la parola è trovata
        ContainsWord search = new ContainsWord();

        for (File pdfFile : pdfs) {
            try {
                // Chiama containsWord e incrementa il contatore se la parola è presente
                if (search.containsWord(pdfFile, word)) {
                    counter++;
                }
            } catch (IOException e) {
                System.err.println("Errore durante la lettura del file PDF: " + pdfFile.getName() + ". " + e.getMessage());
            }
        }

        return counter; // Ritorna il numero di file in cui la parola è stata trovata
    }

}
