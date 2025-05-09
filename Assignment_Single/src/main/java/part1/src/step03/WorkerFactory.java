package part1.src.step03;


public class WorkerFactory {

    /**
     * Crea un nuovo Worker configurato con il monitor e la parola da cercare.
     *
     * @param resultManager Classe per la gestione del risultato.
     * @param searchWord La parola da cercare nei file PDF.
     * @return Una nuova istanza di Worker.
     */
    public static Worker createWorker(ResultManager resultManager, String searchWord) {
        return new Worker(resultManager, searchWord);
    }
}
