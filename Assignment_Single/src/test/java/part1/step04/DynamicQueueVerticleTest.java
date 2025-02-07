package part1.step04;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import part1.src.step04.DynamicQueueVerticle;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class DynamicQueueVerticleTest {

    private Vertx vertx;
    private EventBus eventBus;
    private DynamicQueueVerticle verticle;

    @BeforeEach
    public void setUp(VertxTestContext testContext) {
        vertx = Vertx.vertx();
        eventBus = vertx.eventBus();
        verticle = new DynamicQueueVerticle();

        // Deploy del verticle
        vertx.deployVerticle(verticle, deployResult -> {
            if (deployResult.succeeded()) {
                testContext.completeNow();
            } else {
                testContext.failNow(deployResult.cause());
            }
        });
    }

    @Test
    public void testAddToQueue(VertxTestContext testContext) {
        // Invia un file PDF al verticle
        eventBus.request("pdf.file.found", "/path/to/file1.pdf", reply -> {
            if (reply.succeeded()) {
                // Verifica che il file sia stato aggiunto alla coda
                Queue<String> queue = verticle.getFileQueue();
                assertTrue(queue.contains("/path/to/file1.pdf"));
                assertEquals(1, queue.size());
                testContext.completeNow();
            } else {
                testContext.failNow(reply.cause());
            }
        });
    }
}