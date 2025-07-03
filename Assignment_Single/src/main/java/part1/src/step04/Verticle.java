package part1.src.step04;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.file.FileSystem;
import part1.src.logic.ProgramState;
import part1.src.logic.ProgramStateManager;
import part1.src.services.ContainsWord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Verticle extends AbstractVerticle {
    private final File directory;
    private final ResultManager resultManager;
    private final String word;

    public Verticle(File directory, ResultManager resultManager, String word) {
        this.directory = directory;
        this.resultManager = resultManager;
        this.word = word;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
            startPromise.complete();
            return;
        }

        FileSystem fs = vertx.fileSystem();
        ContainsWord search = new ContainsWord();

        // Contatore per i file totali da analizzare
        Promise<Integer> totalFilesPromise = Promise.promise();
        countTotalPdfFiles(String.valueOf(directory), vertx, totalFilesPromise);

        totalFilesPromise.future().compose(totalFiles -> {
                    if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                        startPromise.complete();
                        return Future.succeededFuture();
                    }
                    if (totalFiles == 0) {
                        System.out.println("Nessun file PDF trovato nella directory o sottodirectory");
                        startPromise.complete();
                        return Future.succeededFuture();
                    }

                    // Cerca PDF nella directory corrente
                    return fs.readDir(String.valueOf(directory), ".*\\.pdf")
                            .compose(files -> {
                                if (ProgramStateManager.getInstance().getState() != ProgramState.STOP) {
                                    for (String filePath : files) {
                                        resultManager.incrementFilesFound();
                                        WordFinder.analyzePdf(filePath, resultManager, word, vertx, startPromise, totalFiles);
                                    }
                                    // Avvia la ricerca ricorsiva nelle sottodirectory
                                    return WordFinder.recursivePdfSearch(String.valueOf(directory), resultManager, word, vertx, startPromise, totalFiles);
                                }
                                return Future.succeededFuture();
                            });
                })
                .onFailure(err -> {
                    System.out.println("Errore durante la ricerca: " + err.getMessage());
                    startPromise.fail(err);
                });
    }

    // Nuovo metodo per contare tutti i file PDF
    private void countTotalPdfFiles(String directory, Vertx vertx, Promise<Integer> promise) {
        vertx.fileSystem().readDir(directory)
                .compose(items -> {
                    int count = 0;
                    List<Future> futures = new ArrayList<>();

                    for (String item : items) {
                        File file = new File(item);
                        if (file.isDirectory()) {
                            Promise<Integer> dirPromise = Promise.promise();
                            futures.add(dirPromise.future());
                            countTotalPdfFiles(item, vertx, dirPromise);
                        } else if (item.toLowerCase().endsWith(".pdf")) {
                            count++;
                        }
                    }

                    if (futures.isEmpty()) {
                        return Future.succeededFuture(count);
                    }

                    int finalCount = count;
                    return CompositeFuture.all(futures)
                            .map(composite -> {
                                int subCount = composite.list().stream()
                                        .mapToInt(r -> (Integer) r)
                                        .sum();
                                return finalCount + subCount;
                            });
                })
                .onSuccess(promise::complete)
                .onFailure(promise::fail);
    }

}