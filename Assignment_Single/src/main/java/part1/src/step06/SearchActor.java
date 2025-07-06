package part1.src.step06;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import part1.src.logic.ProgramState;
import part1.src.logic.ProgramStateManager;

import java.io.File;
import java.util.concurrent.CompletableFuture;

public class SearchActor extends AbstractActor {
    private final ResultManager resultManager;
    private final CompletableFuture<Integer> completionFuture;
    private int pendingTasks = 0;

    public SearchActor(ResultManager resultManager, CompletableFuture<Integer> future) {
        this.resultManager = resultManager;
        this.completionFuture = future;
    }

    public static Props props(ResultManager resultManager, CompletableFuture<Integer> future) {
        return Props.create(SearchActor.class, resultManager, future);
    }

    private void handleStartSearch(Messages.StartSearch message) {
        if (ProgramStateManager.getInstance().getState() != ProgramState.STOP) {
            pendingTasks++;
            self().tell(new Messages.ScanDirectory(message.directory(), message.word()), self());
        }
    }

    private void handleScanDirectory(Messages.ScanDirectory message) throws InterruptedException {
        File directory = message.directory();
        String word = message.word();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                Thread.sleep(200);

                while (ProgramStateManager.getInstance().getState() == ProgramState.PAUSE) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

                if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                    return;
                }

                if (file.isDirectory()) {
                    pendingTasks++; // Incrementa per la directory
                    self().tell(new Messages.ScanDirectory(file, word), self());
                } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                    pendingTasks++;
                    resultManager.incrementFilesFound();
                    ActorRef fileAnalyzer = getContext().actorOf(FileAnalyzerActor.props(word, resultManager));
                    fileAnalyzer.tell(new Messages.FileFound(file), getSelf());
                }
            }
        }
        pendingTasks--;
        checkCompletion();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.StartSearch.class, this::handleStartSearch)
                .match(Messages.ScanDirectory.class, this::handleScanDirectory)
                .match(Messages.ResultOfSearch.class, this::handleResultOfSearch)
                .build();
    }

    private void handleResultOfSearch(Messages.ResultOfSearch message) {
        pendingTasks--;
        checkCompletion();
    }

    private void checkCompletion() {
        if (pendingTasks == 0) {
            completionFuture.complete(resultManager.getResult());
        }
    }
}