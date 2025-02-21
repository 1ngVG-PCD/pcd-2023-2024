package part1.src.step00;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanDirectory {

    /**
     * Cerca tutti i file PDF in una directory e nelle sue sottodirectory.
     *
     * @param directory La directory da scansionare.
     * @return Una lista di file PDF trovati.
     */
    public static List<File> getPdfFiles(File directory) {
        List<File> pdfFiles = new ArrayList<>();
        scan(directory, pdfFiles);
        return pdfFiles;
    }

    /**
     * Metodo privato ricorsivo per scansionare una directory.
     *
     * @param directory La directory corrente.
     * @param pdfFiles  La lista di file PDF trovati.
     */
    private static void scan(File directory, List<File> pdfFiles) {
        if (directory == null || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Ricorsione nelle sottodirectory
                    scan(file, pdfFiles);
                } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                    // Aggiungi il file PDF alla lista
                    pdfFiles.add(file);
                }
            }
        }
    }

    public static List<String> toStringList(List<File> pdfs) {
        List<String> fileNames = new ArrayList<>();
        for (File file : pdfs) {
            fileNames.add(file.getName()); // Aggiunge il nome del file alla lista
        }
        return fileNames;
    }
}
