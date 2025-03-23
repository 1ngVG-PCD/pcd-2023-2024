package part1.src.step02;


import java.io.File;

public class WorkerFactory {

    /**
     * Crea un nuovo Worker configurato con il monitor e la parola da cercare.
     *
     * @param monitor    Il monitor condiviso tra i worker.
     * @return Una nuova istanza di Worker.
     */
    public static Worker createWorker(Monitor monitor, File pdfFile) {
        return new Worker(monitor, pdfFile);
    }

    public static void SartWorker(Worker w){
        Thread.startVirtualThread(w);
    }
}
