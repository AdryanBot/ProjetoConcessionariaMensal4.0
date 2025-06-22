package view;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private NavigationController navigationController;

    public SidebarPanel(NavigationController navigationController) {
        this.navigationController = navigationController;
        setupSidebar();
    }

    private void setupSidebar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.GRAY);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        createHeaderSection();
        add(Box.createVerticalStrut(35));
        createVendasSection();
        add(Box.createVerticalStrut(30));
        createItensSection();
        add(Box.createVerticalStrut(30));
        createCadastroSection();
        add(Box.createVerticalGlue());
        createSairButton();
    }

    private void createHeaderSection() {
        try {
            ImageIcon icon = new ImageIcon("Icon.png");
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);
            
            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            headerPanel.setBackground(Color.GRAY);
            headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            
            JLabel iconLabel = new JLabel(scaledIcon);
            JLabel titleLabel = new JLabel("FW Concessionária");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(Color.BLACK);
            
            headerPanel.add(iconLabel);
            headerPanel.add(titleLabel);
            
            add(headerPanel);
        } catch (Exception e) {
            JLabel titleLabel = new JLabel("FW Concessionária");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setForeground(Color.BLACK);
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(titleLabel);
        }
    }

    private void createVendasSection() {
        JButton btnVendas = createStyledButton("Vendas", 45);
        add(btnVendas);
        add(Box.createVerticalStrut(8));

        JButton btnNovaVenda = createStyledButton("Nova venda", 35);
        add(btnNovaVenda);
        btnNovaVenda.setVisible(false);
        add(Box.createVerticalStrut(5));

        JButton btnVendasRealizadas = createStyledButton("Vendas realizadas", 35);
        add(btnVendasRealizadas);
        btnVendasRealizadas.setVisible(false);

        btnVendas.addActionListener(e -> {
            btnNovaVenda.setVisible(!btnNovaVenda.isVisible());
            btnVendasRealizadas.setVisible(!btnVendasRealizadas.isVisible());
        });

        btnNovaVenda.addActionListener(e -> navigationController.showNovaVenda());
        btnVendasRealizadas.addActionListener(e -> navigationController.showVendasRealizadas());
    }

    private void createItensSection() {
        JButton btnItens = createStyledButton("Itens Cadastrados", 45);
        add(btnItens);
        add(Box.createVerticalStrut(8));

        JButton btnClientes = createStyledButton("Clientes", 35);
        add(btnClientes);
        btnClientes.setVisible(false);
        add(Box.createVerticalStrut(5));

        JButton btnVeiculos = createStyledButton("Veiculos", 35);
        add(btnVeiculos);
        btnVeiculos.setVisible(false);

        btnItens.addActionListener(e -> {
            btnClientes.setVisible(!btnClientes.isVisible());
            btnVeiculos.setVisible(!btnVeiculos.isVisible());
        });

        btnClientes.addActionListener(e -> navigationController.showClientes());
        btnVeiculos.addActionListener(e -> navigationController.showVeiculos());
    }

    private void createCadastroSection() {
        JButton btnCadastro = createStyledButton("Cadastro", 45);
        add(btnCadastro);
        add(Box.createVerticalStrut(8));

        JButton btnAddCliente = createStyledButton("Cadastrar Cliente", 35);
        add(btnAddCliente);
        btnAddCliente.setVisible(false);
        add(Box.createVerticalStrut(5));

        JButton btnRemoverCliente = createStyledButton("Remover Cliente", 35);
        add(btnRemoverCliente);
        btnRemoverCliente.setVisible(false);
        add(Box.createVerticalStrut(5));

        JButton btnAddVeiculo = createStyledButton("Cadastrar Veiculo", 35);
        add(btnAddVeiculo);
        btnAddVeiculo.setVisible(false);
        add(Box.createVerticalStrut(5));

        JButton btnRemoverVeiculo = createStyledButton("Remover Veiculo", 35);
        add(btnRemoverVeiculo);
        btnRemoverVeiculo.setVisible(false);

        btnCadastro.addActionListener(e -> {
            btnAddCliente.setVisible(!btnAddCliente.isVisible());
            btnAddVeiculo.setVisible(!btnAddVeiculo.isVisible());
            btnRemoverCliente.setVisible(!btnRemoverCliente.isVisible());
            btnRemoverVeiculo.setVisible(!btnRemoverVeiculo.isVisible());
        });

        btnAddCliente.addActionListener(e -> navigationController.showCadastroCliente());
        btnAddVeiculo.addActionListener(e -> navigationController.showCadastroVeiculo());
        btnRemoverCliente.addActionListener(e -> navigationController.showRemoverCliente());
        btnRemoverVeiculo.addActionListener(e -> navigationController.showRemoverVeiculo());
    }

    private void createSairButton() {
        JButton btnSair = createStyledButton("Sair", 45);
        add(btnSair);
        btnSair.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(String text, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(Color.LIGHT_GRAY);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                button.setBorderPainted(true);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBorderPainted(false);
            }
        });
        
        return button;
    }
}