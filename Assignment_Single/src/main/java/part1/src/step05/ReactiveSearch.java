package part1.src.step05;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import part1.src.logic.*;
import part1.src.services.ContainsWord;

import java.io.File;

public class ReactiveSearch implements Search {
    private ResultManager resultManager;
    private final ContainsWord containsWord;

    public ReactiveSearch() {
        this.containsWord = new ContainsWord();
    }

    @Override
    public Integer run(File directory, String word, OutputUpdater outputUpdater) {
        resultManager = new ResultManager(outputUpdater);

        // Thread per gestire l'input dell'utente
        Thread inputThread = new Thread(new SetState());
        inputThread.setDaemon(true);
        inputThread.start();

        // Trova e processa i file PDF in modo reattivo
        findPdfFiles(directory)
                .doOnNext(file -> resultManager.incrementFilesFound())
                .filter(file -> ProgramStateManager.getInstance().getState() == ProgramState.START)
                .flatMap(file -> processPdfFile(file, word))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(found -> {
                    if (found) {
                        resultManager.incrementFilesWord();
                    }
                    resultManager.incrementFilesAnalyzed();
                })
                .doOnError(error -> System.err.println("Errore: " + error.getMessage()))
                .doOnComplete(() -> System.out.println("Ricerca completata"))
                .blockingSubscribe();

        return resultManager.getResult();
    }

    private Observable<File> findPdfFiles(File directory) {
        return Observable.fromArray(directory.listFiles())
                .flatMap(file -> {
                    // Controlla lo stato prima di procedere
                    if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                        return Observable.empty();
                    }

                    // Gestione della pausa
                    while (ProgramStateManager.getInstance().getState() == ProgramState.PAUSE) {
                        Thread.sleep(100);
                        if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                            return Observable.empty();
                        }
                    }

                    //Thread.sleep(200);

                    if (file.isDirectory()) {
                        return findPdfFiles(file);
                    } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                        return Observable.just(file);
                    }
                    return Observable.empty();
                });
    }

    private Observable<Boolean> processPdfFile(File file, String word) {
        return Observable.fromCallable(() -> {

            //Thread.sleep(500);

            // Gestione degli stati
            while (ProgramStateManager.getInstance().getState() == ProgramState.PAUSE) {
                Thread.sleep(100);
                if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                    return false;
                }
            }

            if (ProgramStateManager.getInstance().getState() == ProgramState.STOP) {
                return false;
            }

            return containsWord.containsWord(file, word);
        }).subscribeOn(Schedulers.io());
    }
}