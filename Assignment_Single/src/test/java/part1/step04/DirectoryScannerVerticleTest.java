package part1.step04;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import part1.src.step04.DirectoryScannerVerticle;

import java.nio.file.Files;
import java.nio.file.Path;


import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class DirectoryScannerVerticleTest {

    private Vertx vertx;
    private EventBus eventBus;

    @BeforeEach
    public void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();

        // Deploy del verticle
        vertx.deployVerticle(new DirectoryScannerVerticle(), deployResult -> {
            if (deployResult.succeeded()) {
                testContext.completeNow();
            } else {
                testContext.failNow(deployResult.cause());
            }
        });
    }

    @Test
    public void testDirectoryScanning(VertxTestContext testContext) throws Exception {
        // Crea una directory temporanea e aggiungi alcuni file PDF
        Path tempDir = Files.createTempDirectory("testDir");
        Files.createFile(tempDir.resolve("file1.pdf"));
        Files.createFile(tempDir.resolve("file2.pdf"));
        Files.createFile(tempDir.resolve("file3.txt")); // File non PDF

        // Registra un consumer per ricevere i file PDF trovati
        eventBus.consumer("pdf.files.found", (Message<String> message) -> {
            String filePath = message.body();
            System.out.println("File PDF trovato: " + filePath);
            testContext.verify(() -> {
                assertTrue(filePath.endsWith(".pdf")); // Verifica che il file sia un PDF
                testContext.completeNow();
            });
        });

        // Invia una richiesta per scansionare la directory temporanea
        eventBus.request("directory.request", tempDir.toString(), reply -> {
            if (reply.succeeded()) {
                System.out.println("Risposta dal verticle: " + reply.result().body());
            } else {
                testContext.failNow(reply.cause());
            }
        });
    }

    @Test
    public void testInvalidDirectory(VertxTestContext testContext) {
        // Percorso di una directory inesistente
        String invalidDirectoryPath = "/invalid/directory/path";

        // Invia una richiesta per scansionare una directory inesistente
        eventBus.request("directory.request", invalidDirectoryPath, reply -> {
            if (reply.succeeded()) {
                testContext.failNow("La richiesta avrebbe dovuto fallire per una directory inesistente");
            } else {
                System.out.println("Errore atteso: " + reply.cause().getMessage());
                testContext.completeNow();
            }
        });
    }
}