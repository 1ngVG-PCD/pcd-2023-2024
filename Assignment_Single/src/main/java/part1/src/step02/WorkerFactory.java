package part1.src.step02;


public class WorkerFactory {

    /**
     * Crea un nuovo Worker configurato con il monitor e la parola da cercare.
     *
     * @param monitor    Il monitor condiviso tra i worker.
     * @param searchWord La parola da cercare nei file PDF.
     * @return Una nuova istanza di Worker.
     */
    public static Worker createWorker(Monitor monitor, String searchWord) {
        return new Worker(monitor, searchWord);
    }
}
