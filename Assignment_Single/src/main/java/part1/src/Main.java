package part1.src;

import part1.src.step00.SeqSearch;
import part1.src.step01.ConcurrentSearch;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import static part1.src.services.ScanDirectory.getPdfFiles;
import static part1.src.services.ScanDirectory.toStringList;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Chiedi il percorso della directory
        System.out.print("Inserisci il percorso della directory: ");
        File directoryPath = new File(scanner.nextLine());

        // Chiedi la parola da cercare
        System.out.print("Inserisci la parola da cercare: ");
        String searchWord = scanner.nextLine();

        System.out.println("Scegli come effettuare l'operaione:");
        System.out.println("0) Sequenziale");
        System.out.println("1) Concorrente con uso di monitor e worker");
        System.out.print("Inserisci numero:   ");
        int searchingMod = scanner.nextInt();
        System.out.println("Metodo" + searchingMod + "  ||Ricerca in corso");

        List<File> pdfs = getPdfFiles(directoryPath);
        if (!directoryPath.exists() || !directoryPath.isDirectory()) {
            System.err.println("La directory inserita non esiste o non è valida.");
            return;
        }
        if (pdfs.isEmpty()) {
            System.err.println("Nessun file PDF trovato nella directory.");
            return;
        }
        List<String> pdfsName = toStringList(pdfs);

        int result = 0;
        switch (searchingMod) {
            case 0:
                SeqSearch seqSearch = new SeqSearch();
                result = seqSearch.run(pdfs, searchWord);
                break;
            case 1:
                ConcurrentSearch concurrentSearch = new ConcurrentSearch();
                result = concurrentSearch.run(pdfs, searchWord);

        }
        System.out.println("La parola \"" + searchWord + "\" è contenuta in " + result + " file PDF.");

    }
}

