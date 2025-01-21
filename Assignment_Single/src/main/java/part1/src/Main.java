package part1.src;

import part1.src.step00.SeqSearch;

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
        System.out.print("Inserisci numero:   ");
        int searchingMod = scanner.nextInt();
        System.out.println("Metodo" + searchingMod + "  ||Ricerca in corso");

        List<File> pdfs = getPdfFiles(directoryPath);
        List<String> pdfsName = toStringList(pdfs);
        int result = 0;
        switch (searchingMod) {
            case 0:
                SeqSearch seqSearch = new SeqSearch();
                result = seqSearch.run(pdfs, searchWord);
                break;

        }
        System.out.println(result);
    }
}

