package part1.src.step04;

import part1.src.gui.SearchGUI;

public class GuiMain {
    public static void main(String[] args) {
        EventSearch searchLogic = new EventSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
