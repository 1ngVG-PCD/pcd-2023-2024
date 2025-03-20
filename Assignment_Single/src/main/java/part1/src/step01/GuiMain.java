package part1.src.step01;

import part1.src.gui.SearchGUI;


public class GuiMain {
    public static void main(String[] args) {
        ConcurrentSearch searchLogic = new ConcurrentSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
