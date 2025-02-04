package part1.step04;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import part1.src.step04.LocalQueueVerticle;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(VertxExtension.class)
public class TestLocalQueueVerticle {

    String filePath = "C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short\\Parola.pdf";

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        // Deploy del LocalQueueVerticle prima di ogni test
        vertx.deployVerticle(new LocalQueueVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testFileQueue(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        eventBus.send("file.found", new JsonObject().put("path", filePath));

        // Richiedi il file dalla coda
        eventBus.request("file.request", "", reply -> {
            if (reply.succeeded()) {
                JsonObject fileInfo = (JsonObject) reply.result().body();
                assertNotNull(fileInfo, "Il file non dovrebbe essere null");
                assertEquals(filePath, fileInfo.getString("path"), "Il percorso del file non corrisponde");
                testContext.completeNow();
            } else {
                testContext.failNow(reply.cause());
            }
        });
    }
}