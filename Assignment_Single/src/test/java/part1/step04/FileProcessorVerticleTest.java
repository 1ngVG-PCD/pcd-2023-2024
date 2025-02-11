package part1.step04;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import part1.src.step04.FileProcessorVerticle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class FileProcessorVerticleTest {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short\\Parola.pdf");
    String word = "parola";

    private Vertx vertx;
    private EventBus eventBus;

    @BeforeEach
    public void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        // Avvio del FileProcessorVerticle
        vertx.deployVerticle(new FileProcessorVerticle(), deployResult -> {
            if (deployResult.succeeded()) {
                testContext.completeNow();
            } else {
                testContext.failNow(deployResult.cause());
            }
        });
    }

    @Test
    public void testSearchWord(VertxTestContext testContext) throws IOException {
        // Crea un file PDF temporaneo con contenuto noto

        // Simula l'aggiunta del file alla coda del DynamicQueueVerticle
        eventBus.send("get.file.from.queue", directoryPath.toString());

        // Registra un consumer per ricevere il risultato della ricerca
        eventBus.consumer("word.found", message -> {
            String filePath = message.body().toString();
            System.out.println("Parola trovata nel file: " + filePath);
            testContext.verify(() -> {
                assertEquals(filePath, directoryPath.toString()); // Verifica che il file sia quello corretto
                testContext.completeNow();
            });
        });

        // Invia una richiesta per cercare la parola
        eventBus.send("word", word);
    }

    @Test
    public void testEmptyQueue(VertxTestContext testContext) {
        // Simula una coda vuota
        eventBus.send("get.file.from.queue", "");

        // Invia una richiesta per cercare una parola
        eventBus.request("word", word, reply -> {
            if (reply.succeeded()) {
                testContext.failNow("La richiesta avrebbe dovuto fallire per una coda vuota");
            } else {
                System.out.println("Errore atteso: " + reply.cause().getMessage());
                testContext.completeNow();
            }
        });
    }
}