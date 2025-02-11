package part1.src.step06;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;

public class FileProcessorActor extends AbstractActor {
    private final ActorRef coordinator;

    public FileProcessorActor(ActorRef coordinator) {
        this.coordinator = coordinator;
    }

    public static Props props(ActorRef coordinator) {
        return Props.create(FileProcessorActor.class, coordinator);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ProcessFile.class, this::onProcessFile)
                .build();
    }

    private void onProcessFile(ProcessFile message) {
        File file = new File(message.filePath);
        try {
            String word = coordinator.path().name();
            boolean result = containsWord(file, word);// Usa la parola dal coordinatore
            coordinator.tell(new CoordinatorActor.FileProcessed(result), getSelf());
        } catch (IOException e) {
            System.err.println("Error processing file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
    }

    // Messaggi
    public static class ProcessFile {
        public final String filePath;

        public ProcessFile(String filePath) {
            this.filePath = filePath;
        }
    }

    private boolean containsWord(File pdfFile, String word) throws IOException {
        return new ContainsWord().containsWord(pdfFile, word);
    }
}