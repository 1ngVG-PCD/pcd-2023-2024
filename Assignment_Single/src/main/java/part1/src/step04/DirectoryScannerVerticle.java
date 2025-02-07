package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.file.FileSystem;

import java.io.File;

public class DirectoryScannerVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        EventBus eventBus = vertx.eventBus();

        // Registra un consumer per ricevere il percorso della directory
        eventBus.consumer("directory.request", this::handleDirectoryRequest);

        // Completa la promise per indicare che il verticle è avviato con successo
        startPromise.complete();
    }

    private void handleDirectoryRequest(Message<String> message) {
        String directoryPath = message.body(); // Percorso della directory inviato tramite event bus

        // Avvia la scansione della directory in modo asincrono
        scanDirectory(directoryPath)
                .onSuccess(reply -> message.reply(reply)) // Rispondi al mittente con il risultato
                .onFailure(err -> message.fail(500, err.getMessage())); // Segnala un errore al mittente
    }

    private Future<String> scanDirectory(String directoryPath) {
        Promise<String> promise = Promise.promise();
        File directory = new File(directoryPath);

        // Verifica che la directory esista e sia una directory valida
        if (!directory.exists() || !directory.isDirectory()) {
            promise.fail("La directory specificata non esiste o non è una directory valida: " + directoryPath);
            return promise.future();
        }

        //PDFBox
        File[] files = directory.listFiles();
        if (files != null) {
            EventBus eventBus = vertx.eventBus();
            for (File file : files) {
                if (file.isDirectory()) {
                    // Ricorsione nelle sottodirectory
                    scanDirectory(file.getAbsolutePath());
                } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                    // Aggiungi il file PDF alla lista
                    eventBus.send("pdf.files.found", file.getAbsolutePath());
                }
            }
        }

        return promise.future();
    }
}