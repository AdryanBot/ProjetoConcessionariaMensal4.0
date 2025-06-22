package view;

import javax.swing.*;
import java.awt.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * Janela principal do sistema de gestão de concessionária.
 * Contém a sidebar de navegação e o painel principal de conteúdo.
 */
public class MainWindow extends JFrame {
    private JPanel painelPrincipal;
    private NavigationController navigationController;

    public MainWindow() {
        initializeFrame();
        setupComponents();
        setVisible(true);
    }

    /**
     * Configura as propriedades básicas da janela.
     */
    private void initializeFrame() {
        setTitle("Gestão de Concessionária");
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(1200, 800));
    }

    /**
     * Configura os componentes da interface.
     */
    private void setupComponents() {
        painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setName("painelPrincipal");
        
        // Adiciona mensagem inicial
        JLabel mensagemInicial = new JLabel("Selecione uma opção ao lado", JLabel.CENTER);
        mensagemInicial.setFont(new Font("Arial", Font.PLAIN, 24));
        mensagemInicial.setForeground(Color.GRAY);
        painelPrincipal.add(mensagemInicial, BorderLayout.CENTER);

        navigationController = new NavigationController(painelPrincipal);
        
        SidebarPanel sidebar = new SidebarPanel(navigationController);
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setMinimumSize(new Dimension(280, 0));
        
        add(sidebar, BorderLayout.WEST);
        add(painelPrincipal, BorderLayout.CENTER);
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