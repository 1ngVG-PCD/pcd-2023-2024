package part1.src.step06;

import akka.actor.AbstractActor;
import akka.actor.Props;
import part1.src.services.ContainsWord;

import java.io.IOException;

public class FileAnalyzerActor extends AbstractActor {
    private final String word;
    private final ContainsWord containsWord;
    private final ResultManager resultManager;

    public FileAnalyzerActor(String word, ResultManager resultManager) {
        this.word = word;
        this.resultManager = resultManager;
        this.containsWord = new ContainsWord();
    }

    public static Props props(String word, ResultManager resultManager) {
        return Props.create(FileAnalyzerActor.class, word, resultManager);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Messages.FileFound.class, this::handleFileFound)
                .build();
    }

    private void handleFileFound(Messages.FileFound message) {

        try {
            resultManager.incrementFilesAnalyzed();
            if (containsWord.containsWord(message.file(), word)) {
                resultManager.incrementFilesWord();
            }
            getSender().tell(new Messages.ResultOfSearch(true), getSelf());
            getContext().stop(getSelf());
        } catch (Exception e) {
            resultManager.incrementFilesAnalyzed();
            getSender().tell(new Messages.ResultOfSearch(false), getSelf());
        }finally {
            getContext().stop(getSelf());
        }
    }
}