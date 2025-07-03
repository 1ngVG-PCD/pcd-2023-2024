//package part1.src.step05;
//
//import io.reactivex.rxjava3.core.Observable;
//import io.reactivex.rxjava3.core.Scheduler;
//import part1.src.logic.OutputUpdater;
//import part1.src.logic.Search;
//import part1.src.logic.SetState;
//
//import java.io.File;
//
//public class ReactiveSearch implements Search {
//    @Override
//    public Integer run(File directory, String word, OutputUpdater outputUpdater) throws InterruptedException {
//
//        ResultManager resultManager = new ResultManager(outputUpdater);
//
//        // Thread per gestire l'input dell'utente
//        Thread inputThread = new Thread(new SetState());
//        inputThread.setDaemon(true); // Imposta come daemon per terminare con il programma
//        inputThread.start();
//
//
//        Observable<File> pdfFiles = findPdfFiles(directory);
//
//        pdfFiles
//                .subscribeOn(Scheduler.io())
//                .flatMap(file -> processPdfFile(file).subscribe(
//                        result -> System.out.println("Risultato elaborazione: "+ result),
//                        error -> System.err.println("Errore: " + error.getMessage()),
//                        () -> System.out.println("Elaborazione Completata")
//                ));
//
//        return resultManager.getResult();
//        }
//
//    private static Observable<File> findPdfFiles(File directory) {
//        return Observable.fromArray(directory.listFiles())
//                .flatMap(file -> {
//                    if (file.isDirectory()){
//                        return findPdfFiles(file);
//                    } else if (file.getName().toLowerCase().endsWith(".pdf")) {
//                        return Observable.just(file);
//                    }else {
//                        return Observable.empty();
//                    }
//                });
//    }
//
//}
