package part1.src.step03;

import part1.src.gui.SearchGUI;

public class GuiMain {
    public static void main(String[] args) {
        ExecutorSearch searchLogic = new ExecutorSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
