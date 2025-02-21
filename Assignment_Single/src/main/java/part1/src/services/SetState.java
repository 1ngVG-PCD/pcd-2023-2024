package part1.src.services;

import part1.src.logic.ProgramState;

import java.util.Scanner;

public class SetState extends Thread{
    private volatile ProgramState state;

    public SetState(ProgramState state){
        this.state = state;
    }

    @Override
    public void run(){

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "start":
                    state = ProgramState.START;
                    System.out.println("Programma avviato/ripreso.");
                    break;
                case "pause":
                    state = ProgramState.PAUSE;
                    System.out.println("Programma in pausa.");
                    break;
                case "stop":
                    state = ProgramState.STOP;
                    System.out.println("Programma terminato.");
                    return; // Termina il thread
                default:
                    System.out.println("Comando non riconosciuto. Usa 'start', 'pause' o 'stop'.");
            }
        }
    }
}
