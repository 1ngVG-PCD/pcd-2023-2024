package part1.src.step02;

import part1.src.gui.SearchGUI;


public class GuiMain {
    public static void main(String[] args) {
        VirtualThreadSearch searchLogic = new VirtualThreadSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
