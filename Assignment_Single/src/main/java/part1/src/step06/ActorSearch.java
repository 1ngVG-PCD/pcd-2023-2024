package part1.src.step06;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import part1.src.logic.Search;

import java.io.File;

public class ActorSearch {

    public Integer run(File directory, String word) throws InterruptedException{
        int result = 0;

        // Crea il sistema di attori
        ActorSystem system = ActorSystem.create("PdfSearchSystem");

        // Crea l'attore coordinatore
        ActorRef coordinator = system.actorOf(CoordinatorActor.props(word), "coordinator");

        // Avvia la scansione della directory
        coordinator.tell(new CoordinatorActor.ScanDirectory(directory.getAbsolutePath()), ActorRef.noSender());


        return result;
    }

}

