package part1.src.step04;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import part1.src.logic.Search;

import java.io.File;

public class EventSearch {

    public Integer run(File directory, String word) {
        int result = 0;

        Vertx vertx = Vertx.vertx();

        // Avvio del DirectoryScannerVerticle
        vertx.deployVerticle(new DirectoryScannerVerticle(), deploymentResult -> {
            if (deploymentResult.succeeded()) {
                System.out.println("DirectoryScannerVerticle avviato con successo!");

                // Ottieni l'EventBus
                EventBus eventBus = vertx.eventBus();

                eventBus.request("directory.request", directory, reply -> {
                    if (reply.succeeded()) {
                        System.out.println("Risposta dal verticle: " + reply.result().body());
                    } else {
                        System.err.println("Errore durante la scansione: " + reply.cause().getMessage());
                    }
                });
            } else {
                System.err.println("Errore durante l'avvio del verticle: " + deploymentResult.cause());
            }
        });

        return result;
    }
}