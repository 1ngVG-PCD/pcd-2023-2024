package part1.src.step03;

import part1.src.gui.SearchGUI;
import part1.src.step02.VirtualThreadSearch;


public class GuiMain {
    public static void main(String[] args) {
        VirtualThreadSearch searchLogic = new VirtualThreadSearch();
        SearchGUI gui = new SearchGUI(searchLogic);
        gui.setVisible(true);
    }
}
