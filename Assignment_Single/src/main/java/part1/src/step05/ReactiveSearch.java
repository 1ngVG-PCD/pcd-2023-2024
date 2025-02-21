package part1.src.step05;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import part1.src.logic.Search;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactiveSearch {

    /**
     * Metodo per eseguire la ricerca reattiva dei file PDF contenenti una parola.
     *
     * @param directory La directory da analizzare.
     * @param word      La parola da cercare nei file PDF.
     * @return Il numero di file in cui la parola è stata trovata.
     * @throws InterruptedException Se il thread principale viene interrotto durante l'attesa dei risultati.
     */
    public Integer run(File directory, String word) throws InterruptedException {
        AtomicInteger resultCount = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);  // Sincronizza il completamento dell'osservabile

        Observable<File> fileStream = DirectoryScanner.scanDirectory(directory.getAbsolutePath())
                .subscribeOn(Schedulers.io());

        ResultCollector.countMatchingFiles(fileStream, word)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.single())
                .subscribe(
                        count -> {
                            resultCount.set(count);
                            latch.countDown();  // Sblocca il main thread quando il conteggio è completato
                        },
                        error -> {
                            System.err.println("Errore: " + error.getMessage());
                            latch.countDown();
                        }
                );

        latch.await(); // Aspetta il completamento dell'elaborazione
        return resultCount.get();
    }
}
