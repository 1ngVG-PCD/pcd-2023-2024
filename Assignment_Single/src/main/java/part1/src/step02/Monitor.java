package part1.src.step02;

import part1.src.logic.OutputUpdater;
import part1.src.logic.SearchResult;

import java.io.File;

public class Monitor {
    private final String searchWord;
    private boolean isFinished = false; // Flag per segnalare la terminazione
    private final OutputUpdater outputUpdater;

    public Monitor(String searchWord, OutputUpdater outputUpdater) {
        this.outputUpdater = outputUpdater;
        this.searchWord = searchWord;
    }

    /**
     * Restituisce un file dal buffer.
     * Se il buffer è vuoto, il thread viene messo in attesa.
     * Restituisce null se il produttore ha terminato e il buffer è vuoto.
     */

    SearchResult result = new SearchResult(0,0,0);
    /**
     * Incrementa il contatore dei risultati.
     */
    public synchronized void incrementFilesFound() {
        result = new SearchResult(result.totalFilesAnalyzed(), result.pdfFilesFound() + 1, result.pdfFilesWithWord());
        outputUpdater.update(result.totalFilesAnalyzed(), result.pdfFilesFound(), result.pdfFilesWithWord());
    }

    public synchronized void incrementFilesAnalyzed() {
        result = new SearchResult(result.totalFilesAnalyzed() + 1, result.pdfFilesFound(), result.pdfFilesWithWord());
        outputUpdater.update(result.totalFilesAnalyzed(), result.pdfFilesFound(), result.pdfFilesWithWord());
    }

    public synchronized void incrementFilesWord() {
        result = new SearchResult(result.totalFilesAnalyzed(), result.pdfFilesFound(), result.pdfFilesWithWord() + 1);
        outputUpdater.update(result.totalFilesAnalyzed(), result.pdfFilesFound(), result.pdfFilesWithWord());
    }

    /**
     * Restituisce il numero totale di risultati.
     */
    public synchronized int getResult() {
        return result.pdfFilesWithWord();
    }

    /**
     * Imposta il flag di terminazione e notifica tutti i thread in attesa.
     */
    public synchronized void setFinished() {
        isFinished = true; // Il produttore ha terminato
        notifyAll(); // Risveglia i thread in attesa
    }

    public synchronized String getWord () {
        return searchWord;
    }
}

