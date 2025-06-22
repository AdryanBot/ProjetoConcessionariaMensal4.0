package main;

import javax.swing.SwingUtilities;
import view.*;

/**
 * Classe principal do sistema de gestão de concessionária.
 * Responsável por inicializar a aplicação.
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}