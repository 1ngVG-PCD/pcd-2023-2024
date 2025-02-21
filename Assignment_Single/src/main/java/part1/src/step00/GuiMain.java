package part1.src.step00;

import part1.src.gui.SearchGUI;

public class GuiMain {
    public static void main(String[] args) {
        SeqSearch searchLogic = new SeqSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
