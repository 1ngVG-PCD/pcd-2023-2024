package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import java.util.concurrent.atomic.AtomicInteger;

public class ResultCollectorVerticle extends AbstractVerticle {
    private final AtomicInteger resultCount = new AtomicInteger(0);
    private final AtomicInteger filesProcessed = new AtomicInteger(0);
    private final Promise<Integer> resultPromise;
    private int totalFiles = 0; // Memorizza il numero totale di file

    public ResultCollectorVerticle(Promise<Integer> resultPromise) {
        this.resultPromise = resultPromise;
    }

    @Override
    public void start() {
        // Ascolta i file trovati
        vertx.eventBus().consumer("result.increment", message -> resultCount.incrementAndGet());

        // Ascolta i file elaborati
        vertx.eventBus().consumer("file.processed", message -> {
            if (filesProcessed.incrementAndGet() == totalFiles) {
                System.out.println("Tutti i file sono stati elaborati. Risultato: " + resultCount.get());
                resultPromise.complete(resultCount.get());
                vertx.close(); // Chiudi Vert.x
            }
        });

        // Ascolta la fine della scansione
        vertx.eventBus().consumer("scanner.done", message -> {
            // Richiede la dimensione della coda a LocalQueueVerticle
            vertx.eventBus().request("file.queue.size", "", reply -> {
                if (reply.succeeded()) {
                    totalFiles = (int) reply.result().body(); // Memorizza il numero totale di file
                    System.out.println("Totale file da elaborare: " + totalFiles);

                    // Se non ci sono file da elaborare, completa immediatamente
                    if (totalFiles == 0) {
                        resultPromise.complete(0);
                        vertx.close();
                    }
                } else {
                    System.err.println("Errore nella richiesta della dimensione della coda: " + reply.cause().getMessage());
                    resultPromise.fail(reply.cause());
                }
            });
        });
    }
}