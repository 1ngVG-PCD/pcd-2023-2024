package services;

import org.junit.jupiter.api.Test;
import part1.src.services.ScanDirectory;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestScanDirectory {
    @Test
    void testEmptyDirectory() {
        File emptyDirectory = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Vuota");
        List<File> pdfs = ScanDirectory.getPdfFiles(emptyDirectory);

        // Verifica che la lista sia vuota
        assertEquals(0, pdfs.size(), "La directory vuota dovrebbe restituire una lista vuota di file PDF");
    }

    @Test
    void testNonEmptyDirectory() {
        File directory = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Mixed");
        List<File> pdfs = ScanDirectory.getPdfFiles(directory);

        // Verifica che il numero di file sia corretto
        assertEquals(100, pdfs.size(), "La directory dovrebbe contenere esattamente 100 file PDF");
    }

    @Test
    void testWithSubDirectory() {
        File directory = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet");
        List<File> pdfs = ScanDirectory.getPdfFiles(directory);

        // Verifica che il numero di file sia corretto
        assertEquals(300, pdfs.size(), "La directory dovrebbe contenere esattamente 300 file PDF");
    }
}
