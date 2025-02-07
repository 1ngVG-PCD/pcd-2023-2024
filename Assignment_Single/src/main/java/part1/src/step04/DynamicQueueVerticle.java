package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DynamicQueueVerticle extends AbstractVerticle {

    private Queue<String> fileQueue;

    @Override
    public void start(Promise<Void> startPromise) {
        fileQueue = new ConcurrentLinkedQueue<>();

        EventBus eventBus = vertx.eventBus();

        // Registra un consumer per ricevere il percorso della directory
        eventBus.consumer("pdf.file.found", this::addToQueue);

        // Completa la promise per indicare che il verticle è avviato con successo
        startPromise.complete();
    }

    private void addToQueue(Message<String> message) {
        String filePath = message.body();

        fileQueue.add(filePath);
        System.out.println("File aggiunto alla coda: " + filePath);

        // Opzionale: Notifica che il file è stato aggiunto alla coda
        message.reply("File aggiunto alla coda: " + filePath);
    }

    public Queue<String> getFileQueue() {
        return fileQueue;
    }

}

