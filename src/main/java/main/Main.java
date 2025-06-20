package main;

import ui.MainWindow;
import javax.swing.SwingUtilities;

/**
 * Refactored version of MainTeste - now clean and simple
 * All UI logic has been separated into dedicated classes in the ui package
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}