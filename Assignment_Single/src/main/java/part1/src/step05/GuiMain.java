package part1.src.step05;

import part1.src.gui.SearchGUI;

public class GuiMain {
    public static void main(String[] args) {
        ReactiveSearch searchLogic = new ReactiveSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
