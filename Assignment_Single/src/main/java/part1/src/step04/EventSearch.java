package part1.src.step04;

import io.vertx.core.Vertx;
import io.vertx.core.Promise;
import part1.src.logic.OutputUpdater;
import part1.src.logic.Search;
import part1.src.logic.SetState;


import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class EventSearch implements Search {
    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {
        ResultManager resultManager = new ResultManager(outputUpdater);
        CountDownLatch latch = new CountDownLatch(1);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true); // Imposta come daemon per terminare con il programma
        inputThread.start();

        Vertx vertx = Vertx.vertx();
        Promise<String> deploymentPromise = Promise.promise();

        vertx.deployVerticle(new Verticle(directory, resultManager, word), res -> {
            if (res.succeeded()) {
                System.out.println("Verticle deployed: " + res.result());
                deploymentPromise.complete(res.result());
            } else {
                System.out.println("Deployment failed: " + res.cause().getMessage());
                deploymentPromise.fail(res.cause());
            }
        });

        // Attendi il completamento del verticle
        deploymentPromise.future()
                .compose(deploymentId -> {
                    Promise<Void> completionPromise = Promise.promise();

                    // Chiudi Vertx quando il lavoro è completato
                    vertx.close(closeRes -> {
                        if (closeRes.succeeded()) {
                            System.out.println("Vertx closed successfully");
                            completionPromise.complete();
                        } else {
                            completionPromise.fail(closeRes.cause());
                        }
                    });

                    return completionPromise.future();
                })
                .onComplete(res -> {
                    latch.countDown(); // Rilascia il latch quando tutto è completato
                });

        // Aspetta il completamento con timeout
        if (!latch.await(2, TimeUnit.MINUTES)) {
            System.out.println("Timeout raggiunto, forzando la chiusura");
            vertx.close();
        }
        return resultManager.getResult();
    }
}
