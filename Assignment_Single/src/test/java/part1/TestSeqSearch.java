package part1;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static part1.src.services.ScanDirectory.getPdfFiles;

public class TestSeqSearch {

    @Test
    void TestAllFileHaveWord() {
        File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\100%");
        List<File> pdfs = getPdfFiles(directoryPath);
        String word = "parola";
    }

    @Test
    void TestNone() {
    }

    @Test
    void TestMixedDirectory() {
    }
}