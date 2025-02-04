package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocalQueueVerticle extends AbstractVerticle {
    private final ConcurrentLinkedQueue<String> fileQueue = new ConcurrentLinkedQueue<>();

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().consumer("file.found", message -> {
            String filePath = ((JsonObject) message.body()).getString("path");
            fileQueue.offer(filePath);
        });

        vertx.eventBus().consumer("file.request", message -> {
            String nextFile = fileQueue.poll();
            if (nextFile != null) {
                message.reply(new JsonObject().put("path", nextFile));
            } else {
                message.reply(null);
            }
        });

        vertx.eventBus().consumer("file.queue.size", message -> {
            message.reply(fileQueue.size());
        });

        startPromise.complete();
    }
}