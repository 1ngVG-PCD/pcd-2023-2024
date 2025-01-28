package part1.src.step04;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AsyncSearch {

    public int run(List<File> pdfs, String word){

        return 0;
    }

    /**
     * Metodo helper per verificare se un file contiene una parola specifica.
     *
     * @param pdfFile Il file PDF da analizzare.
     * @param word    La parola da cercare.
     * @return True se la parola è trovata, false altrimenti.
     * @throws IOException Se si verifica un errore durante la lettura del file.
     */
    private boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }
}
