package part1.src.step03;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskBasedSearch {

    /**
     * Esegue la ricerca di una parola specificata in una lista di file PDF,
     * utilizzando un approccio a task e un pool di thread.
     *
     * @param pdfs La lista dei file PDF da analizzare.
     * @param word La parola da cercare nei file.
     * @return Il numero di file PDF in cui è stata trovata la parola.
     * @throws InterruptedException Se il thread principale viene interrotto durante l'esecuzione.
     */
    public int run(List<File> pdfs, String word) throws InterruptedException {
        // Crea un ExecutorService con un pool di thread fisso
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        // Lista di Future per raccogliere i risultati
        List<Future<Boolean>> futures = new ArrayList<>();

        try {
            // Invia un task per ogni file al pool di thread
            for (File pdfFile : pdfs) {
                futures.add(executor.submit(() -> containsWord(pdfFile, word)));
            }

            // Raccoglie i risultati dai Future
            int resultCount = 0;
            for (Future<Boolean> future : futures) {
                try {
                    if (future.get()) { // Attende e ottiene il risultato del task
                        resultCount++;
                    }
                } catch (Exception e) {
                    System.err.println("Errore durante l'elaborazione di un task: " + e.getMessage());
                }
            }

            return resultCount; // Restituisce il numero di file trovati
        } finally {
            // Arresta l'ExecutorService e attende la terminazione
            executor.shutdown();
            while (!executor.isTerminated()) {
                Thread.onSpinWait();
            }
        }
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
