package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import java.io.File;

public class DirectoryScannerVerticle extends AbstractVerticle {
    private File directory;

    @Override
    public void start() {
        String dirPath = config().getString("directory");
        if (dirPath == null) {
            System.err.println("Errore: Directory non specificata!");
            return;
        }
        directory = new File(dirPath);

        vertx.executeBlocking(promise -> {
            scanDirectory(directory);
            System.out.println("Scansione terminata");
            vertx.eventBus().send("scanner.done", "done");
            promise.complete();
        });
    }

    private void scanDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".pdf")) {
                    System.out.println("File trovato: " + file.getAbsolutePath());
                    vertx.eventBus().send("file.found", new JsonObject().put("path", file.getAbsolutePath()));
                }
            }
        }
    }
}