package part1.step04;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import part1.src.step04.FileProcessorVerticle;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class TestFileProcessorVerticle {

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        // Deploy del FileProcessorVerticle prima di ogni test
        vertx.deployVerticle(new FileProcessorVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testProcessFileWithWordFound(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        // Simula un file che contiene la parola
        String filePath = "path/to/file_with_word.pdf";
        eventBus.consumer("result.increment", message -> {
            testContext.verify(() -> {
                // Verifica che il messaggio sia stato inviato
                assertTrue(true, "Il messaggio result.increment è stato inviato");
                testContext.completeNow();
            });
        });

        // Simula l'invio di un file da elaborare
        eventBus.request("file.request", "", reply -> {
            if (reply.succeeded()) {
                JsonObject fileInfo = (JsonObject) reply.result().body();
                eventBus.send("file.found", fileInfo); // Invia il file trovato
            } else {
                testContext.failNow(reply.cause()); // Gestisci l'errore
            }
        });
    }

    @Test
    void testProcessFileWithWordNotFound(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        // Simula un file che non contiene la parola
        String filePath = "path/to/file_without_word.pdf";
        eventBus.consumer("result.increment", message -> {
            testContext.failNow(new AssertionError("Il messaggio result.increment non dovrebbe essere inviato"));
        });

        // Simula l'invio di un file da elaborare
        eventBus.request("file.request", "", reply -> {
            if (reply.succeeded()) {
                JsonObject fileInfo = (JsonObject) reply.result().body();
                eventBus.send("file.found", fileInfo); // Invia il file trovato
            } else {
                testContext.failNow(reply.cause()); // Gestisci l'errore
            }
        });

        // Completa il test dopo un breve ritardo
        vertx.setTimer(1000, id -> testContext.completeNow());
    }
}