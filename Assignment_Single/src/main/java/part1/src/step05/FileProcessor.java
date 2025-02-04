package part1.src.step05;

import io.reactivex.rxjava3.core.Single;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.File;
import java.io.IOException;

public class FileProcessor {

    public static Single<Boolean> containsWord(File pdfFile, String word) {
        return Single.create(emitter -> {
            try (PDDocument document = Loader.loadPDF(pdfFile)) {
                String text = new PDFTextStripper().getText(document);
                emitter.onSuccess(text.toLowerCase().contains(word.toLowerCase()));
            } catch (IOException e) {
                emitter.onError(e);
            }
        });
    }
}
