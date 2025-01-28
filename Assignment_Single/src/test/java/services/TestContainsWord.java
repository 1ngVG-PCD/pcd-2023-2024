package services;

import org.junit.jupiter.api.Test;
import part1.src.services.ContainsWord;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static part1.src.services.ScanDirectory.getPdfFiles;

public class TestContainsWord {

    @Test
    void testContainsWorldYes() throws IOException {
        File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short");
        List<File> pdfs = getPdfFiles(directoryPath);
        String word = "parola";
        ContainsWord search = new ContainsWord();

        for (File pdfFile : pdfs){
            assertTrue(search.containsWord(pdfFile, word));
        }
    }

    @Test
    void testContainsWorldNo() throws IOException {
        File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short");
        List<File> pdfs = getPdfFiles(directoryPath);
        String word = "pigna";
        ContainsWord search = new ContainsWord();

        for (File pdfFile : pdfs){
            assertFalse(search.containsWord(pdfFile, word));
        }
    }

    @Test
    void testContainsWorldCaseInsensitive() throws IOException {
        File directoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Short");
        List<File> pdfs = getPdfFiles(directoryPath);
        String word = "pArOlA";
        ContainsWord search = new ContainsWord();

        for (File pdfFile : pdfs){
            assertTrue(search.containsWord(pdfFile, word));
        }
    }
}
