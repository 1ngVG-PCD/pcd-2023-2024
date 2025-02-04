package part1.src.step05;

import io.reactivex.rxjava3.core.Observable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryScanner {

    public static Observable<File> scanDirectory(String directoryPath) {
        return Observable.create(emitter -> {
            List<File> files = getPdfFiles(new File(directoryPath));
            for (File file : files) {
                emitter.onNext(file);
            }
            emitter.onComplete();
        });
    }

    private static List<File> getPdfFiles(File directory) {
        List<File> pdfFiles = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    pdfFiles.addAll(getPdfFiles(file));
                } else if (file.getName().endsWith(".pdf")) {
                    pdfFiles.add(file);
                }
            }
        }
        return pdfFiles;
    }
}
