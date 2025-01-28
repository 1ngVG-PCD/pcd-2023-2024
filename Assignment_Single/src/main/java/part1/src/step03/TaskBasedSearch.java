package part1.src.step03;

import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * La classe {@code TaskBasedSearch} implementa una ricerca concorrente basata su task,
 * sfruttando {@code ExecutorService} per gestire la scansione della directory e la ricerca nei file PDF.
 */
public class TaskBasedSearch {

    //Creiamo una coda 4 volte il numero di processori per evitare he si formino code, essendo che i PDF sono file "veloci".
    private static final int QUEUE_CAPACITY = Runtime.getRuntime().availableProcessors() * 4;

    /**
     * Esegue la ricerca di una parola nei file PDF di una directory,
     * gestendo i task tramite un pool di thread e una coda condivisa.
     *
     * @param directory La directory da analizzare.
     * @param word      La parola da cercare nei file.
     * @return Il numero di file PDF in cui è stata trovata la parola.
     * @throws InterruptedException Se il thread principale viene interrotto.
     */
    public int run(File directory, String word) throws InterruptedException {
        BlockingQueue<File> fileQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger resultCount = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Task per la scansione della directory
        executor.submit(new DirectoryScanner(directory, fileQueue, latch));

        // Task per processare i file PDF
        int numWorkers = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < numWorkers; i++) {
            executor.submit(new FileProcessor(fileQueue, word, resultCount));
        }

        // Aspetta la fine della scansione
        latch.await();

        // Aggiunge token di terminazione per i worker
        for (int i = 0; i < numWorkers; i++) {
            fileQueue.put(FileSentinel.getSentinel());
        }

        // Arresta il pool di thread e aspetta la terminazione
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        return resultCount.get();
    }
}

