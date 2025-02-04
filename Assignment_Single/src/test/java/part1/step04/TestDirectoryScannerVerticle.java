package part1.step04;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import part1.src.step04.DirectoryScannerVerticle;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestDirectoryScannerVerticle {

    File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short");
    File file = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short\\Parola.pdf");
    String fileReference = file.toString();
    String filePath = null;
    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer("file.found", message -> {
            JsonObject fileInfo = (JsonObject) message.body();
            String filePath = fileInfo.getString("path");
            System.out.println("Messaggio ricevuto: " + filePath);
        });

        eventBus.consumer("scanner.done", message -> {
            System.out.println("Scansione completata, test può terminare");
            testContext.completeNow();
        });

        // Configura il DirectoryScannerVerticle con una directory di test
        JsonObject config = new JsonObject().put("directory", directoryPath.getAbsolutePath());
        vertx.deployVerticle(new DirectoryScannerVerticle(), new DeploymentOptions().setConfig(config), testContext.succeeding(id -> testContext.completeNow()));

    }

    @Test
    void testScanDirectory(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        var fileFoundCheckpoint = testContext.checkpoint();
        var scanDoneCheckpoint = testContext.checkpoint();

        eventBus.consumer("file.found", message -> {
            JsonObject fileInfo = (JsonObject) message.body();
            String receivedPath = fileInfo.getString("path");
            System.out.println("📩 File trovato: " + receivedPath);

            assertEquals(fileReference, receivedPath, "Il percorso del file non corrisponde!");

            fileFoundCheckpoint.flag();
        });

        eventBus.consumer("scanner.done", message -> {
            System.out.println("Scansione completata!");
            scanDoneCheckpoint.flag();
        });

        vertx.eventBus().send("start.scan", "");
    }

}