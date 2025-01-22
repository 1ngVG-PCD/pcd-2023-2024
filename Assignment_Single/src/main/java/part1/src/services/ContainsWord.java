package part1.src.services;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class ContainsWord {
    /**
     * Metodo 1: Cerca una parola specifica in un file PDF.
     *
     * @param pdfFile Il file PDF in cui cercare.
     * @param word    La parola da cercare.
     * @return `true` se la parola è trovata, `false` altrimenti.
     * @throws IOException Se si verifica un errore nella lettura del PDF.
     */
    public boolean containsWord(File pdfFile, String word) throws IOException {
        // Carica il PDF
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            // Controlla i permessi
            AccessPermission ap = document.getCurrentAccessPermission();
            if (!ap.canExtractContent()) {
                throw new IOException("You do not have permission to extract text from this file");
            }

            // Usa PDFTextStripper per estrarre il testo
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true); // Ordina per posizione, utile per layout complessi
            String text = stripper.getText(document);

            // Cerca la parola
            return text.contains(word);
        }catch (IOException e) {
            System.err.println("Errore durante la lettura del file PDF: " + pdfFile.getName());
            return false;
        }
    }
}
