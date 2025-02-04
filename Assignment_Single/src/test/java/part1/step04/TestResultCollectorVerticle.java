package part1.step04;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import part1.src.step04.ResultCollectorVerticle;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestResultCollectorVerticle {

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        // Deploy del ResultCollectorVerticle prima di ogni test
        vertx.deployVerticle(new ResultCollectorVerticle(Promise.promise()), testContext.succeeding(id -> testContext.completeNow()));
    }

    @Test
    void testIncrementCounter(Vertx vertx, VertxTestContext testContext) {
        EventBus eventBus = vertx.eventBus();

        // Simula l'invio di 3 messaggi result.increment
        eventBus.send("result.increment", "");
        eventBus.send("result.increment", "");
        eventBus.send("result.increment", "");

        // Verifica che il contatore sia 3
        vertx.setTimer(500, id -> {
            eventBus.request("get.result.count", "", reply -> {
                if (reply.succeeded()) {
                    int count = (int) reply.result().body();
                    assertEquals(3, count, "Il contatore dovrebbe essere 3");
                    testContext.completeNow();
                } else {
                    testContext.failNow(reply.cause());
                }
            });
        });
    }
}