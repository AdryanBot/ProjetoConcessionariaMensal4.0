package ui;

import javax.swing.*;
import java.awt.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class MainWindow extends JFrame {
    private JPanel painelPrincipal;
    private NavigationController navigationController;

    public MainWindow() {
        initializeFrame();
        setupComponents();
        setVisible(true);
    }

    private void initializeFrame() {
        setTitle("Gestão de Concessionária");
        setSize(1080, 759);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
    }

    private void setupComponents() {
        painelPrincipal = new JPanel();
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setBounds(220, 0, 860, 720);
        painelPrincipal.setName("painelPrincipal");
        add(painelPrincipal);

        navigationController = new NavigationController(painelPrincipal);
        
        SidebarPanel sidebar = new SidebarPanel(navigationController);
        sidebar.setBounds(0, 0, 220, 720);
        add(sidebar);
    }

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!\n");
            session.close();
            sessionFactory.close();
        } catch (Exception e) {
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        SwingUtilities.invokeLater(MainWindow::new);
    }
}