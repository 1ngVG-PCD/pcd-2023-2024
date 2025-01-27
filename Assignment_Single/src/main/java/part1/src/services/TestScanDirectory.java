package part1.src.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static part1.src.services.ScanDirectory.getPdfFiles;

public class TestScanDirectory {
    static File emptyDirectoryPath = new File("C:\\Users\\giann\\Desktop\\PCD\\pcd-2023-2024\\Assignment_Single\\src\\main\\java\\part1\\TestSet\\Vuota");

    public static void testEmpty() {
        List<File> pdfs = getPdfFiles(emptyDirectoryPath);

        // Controlla che la lista sia vuota
        if (!pdfs.isEmpty()) {
            throw new AssertionError("La directory vuota dovrebbe restituire una lista vuota di PDF.");
        } else {
            System.out.println("Test superato: La directory vuota non contiene file PDF.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Inizio test per directory vuota...");
        testEmpty();
        System.out.println("Tutti i test completati con successo.");
    }

}
