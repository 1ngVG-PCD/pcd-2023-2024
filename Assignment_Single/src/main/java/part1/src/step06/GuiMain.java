package part1.src.step06;

import part1.src.gui.SearchGUI;

public class GuiMain {
    public static void main(String[] args) {
        ActorSearch searchLogic = new ActorSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
