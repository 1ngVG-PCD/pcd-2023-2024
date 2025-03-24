package part1.src.step02;

import part1.src.logic.OutputUpdater;
import part1.src.logic.SearchResult;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class Monitor {
    private boolean isFinished = false; // Flag per segnalare la terminazione
    private final OutputUpdater outputUpdater;
    private final AtomicInteger activeWorkers = new AtomicInteger(0);
    private final Object completionLock = new Object();

    public Monitor(OutputUpdater outputUpdater) {
        this.outputUpdater = outputUpdater;
    }


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
        checkCompletion();
    }

    public void workerStarted() {
        activeWorkers.incrementAndGet();
    }

    public void workerFinished() {
        int remaining = activeWorkers.decrementAndGet();
        if (remaining == 0) {
            checkCompletion();
        }
    }

    private void checkCompletion() {
        synchronized (completionLock) {
            if (isFinished && activeWorkers.get() == 0) {
                completionLock.notifyAll();
            }
        }
    }

    public void waitForCompletion() throws InterruptedException {
        synchronized (completionLock) {
            while (!isFinished || activeWorkers.get() > 0) {
                completionLock.wait();
            }
        }
    }
}

