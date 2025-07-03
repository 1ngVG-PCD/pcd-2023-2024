package part1.src.step04;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import part1.src.logic.ProgramState;
import part1.src.logic.ProgramStateManager;
import part1.src.services.ContainsWord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WordFinder {

    static Future<Void> recursivePdfSearch(String directory, ResultManager resultManager,
                                           String word, Vertx vertx,
                                           Promise<Void> startPromise, int totalFiles) {
        return vertx.fileSystem().readDir(directory)
                .compose(items -> {
                    List<Future> futures = new ArrayList<>();

                    for (String item : items) {
                        File file = new File(item);
                        if (file.isDirectory()) {
                            futures.add(recursivePdfSearch(item, resultManager, word, vertx, startPromise, totalFiles));
                        } else if (item.toLowerCase().endsWith(".pdf")) {
                            resultManager.incrementFilesFound();
                            futures.add(analyzePdfAsync(item, resultManager, word, vertx, startPromise, totalFiles));
                        }
                    }

                    return CompositeFuture.all(futures).mapEmpty();
                });
    }

    static void analyzePdf(String filePath, ResultManager resultManager,
                           String word, Vertx vertx,
                           Promise<Void> startPromise, int totalFiles) {
        ContainsWord search = new ContainsWord();
        File pdfFile = new File(filePath);
        vertx.executeBlocking(promise -> {
            try {
                ProgramState state = ProgramStateManager.getInstance().getState();

                // Gestione STOP
                if (state == ProgramState.STOP) {
                    startPromise.complete();
                    return;
                }

                // Gestione PAUSE
                while (state == ProgramState.PAUSE) {
                    Thread.sleep(100); // Pausa breve
                    state = ProgramStateManager.getInstance().getState();
                    if (state == ProgramState.STOP) {
                        startPromise.complete();
                        return;
                    }
                }

                // Procedi solo se START
                if (state == ProgramState.START) {
                    resultManager.incrementFilesAnalyzed();
                    if (search.containsWord(pdfFile, word)) {
                        resultManager.incrementFilesWord();
                    }
                }
                promise.complete();
            } catch (Exception e) {
                promise.fail(e);
            }
        }, false, res -> {
            if (resultManager.getAnalyzed() == totalFiles) {
                startPromise.complete();
            }
        });
    }

    private static Future<Void> analyzePdfAsync(String filePath, ResultManager resultManager,
                                                String word, Vertx vertx,
                                                Promise<Void> startPromise, int totalFiles) {
        Promise<Void> promise = Promise.promise();
        analyzePdf(filePath, resultManager, word, vertx, startPromise, totalFiles);
        return promise.future();
    }
}
