package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import part1.src.services.ContainsWord;
import java.io.File;
import java.io.IOException;

public class FileProcessorVerticle extends AbstractVerticle {
    private String searchWord;

    @Override
    public void start() {
        searchWord = config().getString("word");
        if (searchWord == null) {
            System.err.println("Errore: Parola di ricerca non specificata!");
            return;
        }
        requestNextFile();
    }

    private void requestNextFile() {
        vertx.eventBus().request("file.request", "", reply -> {
            if (reply.succeeded() && reply.result().body() != null) {
                JsonObject json = (JsonObject) reply.result().body();
                String filePath = json.getString("path");
                processFile(filePath);
            } else {
                System.out.println("Nessun file da processare, terminazione...");
                vertx.eventBus().send("file.processed", "");
            }
        });
    }

    private void processFile(String filePath) {
        vertx.executeBlocking(promise -> {
            boolean found = false;
            try {
                found = containsWord(new File(filePath), searchWord);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            promise.complete(found);
        }, result -> {
            if (result.succeeded() && (boolean) result.result()) {
                vertx.eventBus().send("result.increment", "");
            }
            vertx.eventBus().send("file.processed", "");
            requestNextFile();
        });
    }

    private boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }
}