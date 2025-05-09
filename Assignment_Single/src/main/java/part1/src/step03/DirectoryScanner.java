package part1.src.step03;

import part1.src.logic.ProgramState;
import part1.src.logic.ProgramStateManager;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DirectoryScanner implements Runnable {
    private final File directory;
    private final ResultManager resultManager;
    private final String word;
    private final ExecutorService workerExecutor;

    public DirectoryScanner(File directory, ResultManager resultManager, String word) {
        this.directory = directory;
        this.resultManager = resultManager;
        this.word = word;

        // Pool dimensionato in base ai core disponibili
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.workerExecutor = Executors.newFixedThreadPool(Math.max(2, availableProcessors));
    }

    @Override
    public void run() {
        try {
            scanDirectory(directory);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            workerExecutor.shutdown();
            try {
                workerExecutor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                workerExecutor.shutdownNow();
            }
        }
    }

    private void scanDirectory(File dir) throws InterruptedException {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                ProgramState state = ProgramStateManager.getInstance().getState();
                if (state == ProgramState.STOP) {
                    workerExecutor.shutdownNow();
                    return;
                }

                if (state == ProgramState.PAUSE) {
                    Thread.sleep(100);
                    continue;
                }

                if (file.isDirectory()) {
                    scanDirectory(file);
                } else if (file.getName().endsWith(".pdf")) {
                    resultManager.incrementFilesFound();
                    workerExecutor.execute(() -> {
                        Worker worker = WorkerFactory.createWorker(resultManager, word);
                        worker.run(file);
                    });
                }
            }
        }
    }
}