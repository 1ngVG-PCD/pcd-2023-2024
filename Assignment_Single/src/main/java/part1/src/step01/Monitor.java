package part1.src.step01;

import java.io.File;

public class Monitor {
    private final File[] buffer; // Array circolare per i file
    private int first = 0, last = 0, count = 0; // Indici per il buffer e contatore degli elementi
    private final int maxSize;
    private int resultCount = 0;
    private boolean isFinished = false; // Flag per segnalare la terminazione

    public Monitor(int maxSize) {
        this.maxSize = maxSize;
        this.buffer = new File[maxSize];
    }

    /**
     * Aggiunge un file al buffer.
     * Se il buffer è pieno, il thread viene messo in attesa.
     */
    public synchronized void addFile(File file) throws InterruptedException {
        while (count == maxSize) { // Il buffer è pieno
            wait();
        }
        buffer[last] = file;
        last = (last + 1) % maxSize; // Incrementa l'indice circolarmente
        count++;
        notifyAll(); // Notifica i consumatori
    }

    /**
     * Restituisce un file dal buffer.
     * Se il buffer è vuoto, il thread viene messo in attesa.
     * Restituisce null se il produttore ha terminato e il buffer è vuoto.
     */
    public synchronized File getFile() throws InterruptedException {
        while (count == 0 && !isFinished) { // Il buffer è vuoto ma non è ancora terminato
            wait();
        }
        if (count == 0 && isFinished) {
            return null; // Nessun file da elaborare e produttore terminato
        }
        File file = buffer[first];
        first = (first + 1) % maxSize; // Incrementa l'indice circolarmente
        count--;
        notifyAll(); // Notifica i produttori
        return file;
    }

    /**
     * Incrementa il contatore dei risultati.
     */
    public synchronized void incrementResultCount() {
        resultCount++;
    }

    /**
     * Restituisce il numero totale di risultati.
     */
    public synchronized int getResultCount() {
        return resultCount;
    }

    /**
     * Imposta il flag di terminazione e notifica tutti i thread in attesa.
     */
    public synchronized void setFinished() {
        isFinished = true; // Il produttore ha terminato
        notifyAll(); // Risveglia i thread in attesa
    }
}

