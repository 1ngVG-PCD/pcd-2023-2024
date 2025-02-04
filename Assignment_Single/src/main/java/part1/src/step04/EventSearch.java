package part1.src.step04;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class EventSearch {

    public Integer run(File directory, String word) {
        Vertx vertx = Vertx.vertx();
        Promise<Integer> resultPromise = Promise.promise();

        vertx.deployVerticle(new LocalQueueVerticle(), res -> {
            if (res.failed()) {
                System.err.println("Errore nel deploy di LocalQueueVerticle: " + res.cause());
                resultPromise.fail(res.cause());
            } else {
                System.out.println("LocalQueueVerticle avviato con successo");
            }
        });

        vertx.deployVerticle(new ResultCollectorVerticle(resultPromise), res -> {
            if (res.failed()) {
                System.err.println("Errore nel deploy di ResultCollectorVerticle: " + res.cause());
                resultPromise.fail(res.cause());
            } else {
                System.out.println("ResultCollectorVerticle avviato con successo");
            }
        });

        vertx.deployVerticle(DirectoryScannerVerticle.class.getName(),
                new DeploymentOptions()
                        .setInstances(1)
                        .setConfig(new JsonObject().put("directory", directory.getAbsolutePath())),
                res -> {
                    if (res.failed()) {
                        System.err.println("Errore nel deploy di DirectoryScannerVerticle: " + res.cause().getMessage());
                    } else {
                        System.out.println("DirectoryScannerVerticle avviato con successo");
                    }
                });

        vertx.deployVerticle(FileProcessorVerticle.class.getName(),
                new DeploymentOptions()
                        .setInstances(Runtime.getRuntime().availableProcessors())
                        .setConfig(new JsonObject().put("word", word)),
                res -> {
                    if (res.failed()) {
                        System.err.println("Errore nel deploy di FileProcessorVerticle: " + res.cause().getMessage());
                    } else {
                        System.out.println("FileProcessorVerticle avviato con successo");
                    }
                });

        try {
            return resultPromise.future().toCompletionStage().toCompletableFuture().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }
}