package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;

public class FileProcessorVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        EventBus eventBus = vertx.eventBus();

        // Registra un consumer per ricevere il percorso della directory
        eventBus.consumer("word", this::search);

        // Completa la promise per indicare che il verticle è avviato con successo
        startPromise.complete();
    }

    private void search(Message<String> message){
        String searchWord = message.body();

        // Richiedi un file dalla coda del DynamicQueueVerticle
        vertx.eventBus().request("get.file.from.queue", "", reply -> {
            if (reply.succeeded()) {
                String filePath = reply.result().body().toString();
                if (filePath.isEmpty()) {
                    System.out.println("Nessun file disponibile nella coda.");
                    return;
                }

                File file = new File(filePath);
                try {
                    if (containsWord(file, searchWord)) {
                        // Invia un segnale per incrementare il contatore
                        vertx.eventBus().send("word.found", file.getAbsolutePath());
                        System.out.println("Parola trovata nel file: " + file.getAbsolutePath());
                    } else {
                        System.out.println("Parola non trovata nel file: " + file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    System.err.println("Errore durante la lettura del file: " + e.getMessage());
                }
            } else {
                System.err.println("Errore durante la richiesta di un file dalla coda: " + reply.cause().getMessage());
            }
        });
    }

    private boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }

}
