package part1.src.step06;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import java.io.File;

public class DirectoryScannerActor extends AbstractActor {
    private final ActorRef coordinator;

    public DirectoryScannerActor(ActorRef coordinator) {
        this.coordinator = coordinator;
    }

    public static Props props(ActorRef coordinator) {
        return Props.create(DirectoryScannerActor.class, coordinator);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ScanDirectory.class, this::onScanDirectory)
                .build();
    }

    private void onScanDirectory(ScanDirectory message) {
        File directory = new File(message.directoryPath);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Crea un nuovo attore per la sottodirectory
                    ActorRef subDirectoryScanner = getContext().actorOf(DirectoryScannerActor.props(coordinator));
                    subDirectoryScanner.tell(new ScanDirectory(file.getAbsolutePath()), getSelf());
                } else if (file.getName().endsWith(".pdf")) {
                    // Invia il file PDF all'attore di elaborazione
                    ActorRef pdfProcessor = getContext().actorOf(FileProcessorActor.props(coordinator));
                    pdfProcessor.tell(new FileProcessorActor.ProcessFile(file.getAbsolutePath()), getSelf());
                }
            }
        }
    }

    // Messaggi
    public static class ScanDirectory {
        public final String directoryPath;

        public ScanDirectory(String directoryPath) {
            this.directoryPath = directoryPath;
        }
    }
}