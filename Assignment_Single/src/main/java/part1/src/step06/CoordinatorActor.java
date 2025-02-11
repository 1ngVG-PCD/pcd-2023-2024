package part1.src.step06;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.io.File;

public class CoordinatorActor extends AbstractActor {
    private final String word;
    private int totalFiles = 0;
    private int filesWithWord = 0;

    public CoordinatorActor(String word) {
        this.word = word;
    }

    public static Props props(String word) {
        return Props.create(CoordinatorActor.class, word);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ScanDirectory.class, this::onScanDirectory)
                .match(FileProcessed.class, this::onFileProcessed)
                .build();
    }

    private void onScanDirectory(ScanDirectory message) {
        File directory = new File(message.directoryPath);
        if (!directory.isDirectory()) {
            System.err.println("Invalid directory: " + message.directoryPath);
            getContext().getSystem().terminate();
            return;
        }

        // Crea l'attore per la scansione della directory
        ActorRef directoryScanner = getContext().actorOf(DirectoryScannerActor.props(getSelf()), "directoryScanner");
        directoryScanner.tell(new DirectoryScannerActor.ScanDirectory(message.directoryPath), getSelf());
    }

    private void onFileProcessed(FileProcessed message) {
        totalFiles++;
        if (message.containsWord) {
            filesWithWord++;
        }

        if (totalFiles % 100 == 0) {
            System.out.println("Processed " + totalFiles + " files. Found " + filesWithWord + " files with the word.");
        }
    }

    // Messaggi
    public static class ScanDirectory {
        public final String directoryPath;

        public ScanDirectory(String directoryPath) {
            this.directoryPath = directoryPath;
        }
    }

    public static class FileProcessed {
        public final boolean containsWord;

        public FileProcessed(boolean containsWord) {
            this.containsWord = containsWord;
        }
    }
}