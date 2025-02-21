package part1.src.gui;

import part1.src.logic.ProgramState;
import part1.src.logic.Search;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SearchGUI extends JFrame {

    private volatile ProgramState state = ProgramState.START; // Stato condiviso
    private JTextField directoryPathField; // Input box per il percorso della directory
    private JTextField wordField; // Input box per la parola da cercare
    private JTextArea outputArea; // Output box per i risultati
    private JButton startButton, stopButton, suspendButton, resumeButton; // Pulsanti di controllo
    private Search searchLogic; // Riferimento alla logica di elaborazione

    public SearchGUI(Search searchLogic) {
        this.searchLogic = searchLogic;

        // Configurazione della finestra
        setTitle("PDF Search Tool");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Pannello per gli input
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Directory Path:"));
        directoryPathField = new JTextField();
        inputPanel.add(directoryPathField);
        inputPanel.add(new JLabel("Word to Search:"));
        wordField = new JTextField();
        inputPanel.add(wordField);
        add(inputPanel, BorderLayout.NORTH);

        // Pannello per i pulsanti
        JPanel buttonPanel = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        suspendButton = new JButton("Suspend");
        resumeButton = new JButton("Resume");
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(suspendButton);
        buttonPanel.add(resumeButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Output box
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Aggiungi listener ai pulsanti
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSearch();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopSearch();
            }
        });

        suspendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suspendSearch();
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeSearch();
            }
        });
    }

    private void startSearch() {
        String directoryPath = directoryPathField.getText();
        String word = wordField.getText();

        if (directoryPath.isEmpty() || word.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserisci sia il percorso della directory che la parola da cercare.", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Resetta i contatori
        outputArea.setText(""); // Pulisci l'output box

        // Avvia il thread per l'elaborazione
        new Thread(() -> {
            state = ProgramState.START;
            int result = 0;
            try {
                result = searchLogic.run(new File(directoryPath), word, this::updateOutput);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(this, "Elaborazione completata. File trovati: " + result, "Completato", JOptionPane.INFORMATION_MESSAGE);
        }).start();
    }

    private void stopSearch() {
        state = ProgramState.STOP;
    }

    private void suspendSearch() {
        state = ProgramState.PAUSE;
    }

    private void resumeSearch() {
        state = ProgramState.START;
    }

    public void updateOutput(int totalFilesAnalyzed, int pdfFilesFound, int pdfFilesWithWord) {
        SwingUtilities.invokeLater(() -> {
            outputArea.setText("File analizzati: " + totalFilesAnalyzed + "\n"
                    + "File PDF trovati: " + pdfFilesFound + "\n"
                    + "File PDF contenenti la parola '" + wordField.getText() + "': " + pdfFilesWithWord);
        });
    }
}