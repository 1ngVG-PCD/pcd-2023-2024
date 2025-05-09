package part1.step04;

import part1.src.logic.OutputUpdater;

public class MockOutputUpdater implements OutputUpdater {
    @Override
    public void update(int totalFilesAnalyzed, int pdfFilesFound, int pdfFilesWithWord) {
        // Stampa i valori per debug (opzionale)
        System.out.printf("Aggiornamento: Totali=%d, Trovati=%d, Con Parola=%d%n",
                totalFilesAnalyzed, pdfFilesFound, pdfFilesWithWord);
    }
}