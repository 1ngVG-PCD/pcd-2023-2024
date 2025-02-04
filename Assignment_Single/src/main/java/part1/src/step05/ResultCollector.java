package part1.src.step05;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import java.io.File;

public class ResultCollector {

    public static Single<Integer> countMatchingFiles(Observable<File> fileStream, String word) {
        return fileStream
                .flatMapSingle(file -> FileProcessor.containsWord(file, word)
                        .map(contains -> contains ? 1 : 0))
                .reduce(0, Integer::sum);
    }
}
