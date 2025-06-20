package ui;

import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
    private NavigationController navigationController;

    public SidebarPanel(NavigationController navigationController) {
        this.navigationController = navigationController;
        setupSidebar();
    }

    private void setupSidebar() {
        setLayout(null);
        setBackground(Color.GRAY);

        createVendasSection();
        createItensSection();
        createCadastroSection();
        createSairButton();
    }

    private void createVendasSection() {
        JButton btnVendas = new JButton("Vendas");
        btnVendas.setFont(new Font("Arial", Font.PLAIN, 20));
        btnVendas.setBackground(Color.gray);
        btnVendas.setBounds(10, 50, 200, 40);
        add(btnVendas);

        JButton btnNovaVenda = new JButton("Nova venda");
        btnNovaVenda.setBackground(Color.gray);
        btnNovaVenda.setBounds(10, 95, 200, 30);
        add(btnNovaVenda);
        btnNovaVenda.setVisible(false);

        JButton btnVendasRealizadas = new JButton("Vendas realizadas");
        btnVendasRealizadas.setBackground(Color.gray);
        btnVendasRealizadas.setBounds(10, 125, 200, 30);
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
        JButton btnItens = new JButton("Itens Cadastrados");
        btnItens.setFont(new Font("Arial", Font.PLAIN, 20));
        btnItens.setBackground(Color.gray);
        btnItens.setBounds(10, 200, 200, 40);
        add(btnItens);

        JButton btnClientes = new JButton("Clientes");
        btnClientes.setBackground(Color.gray);
        btnClientes.setBounds(10, 245, 200, 30);
        add(btnClientes);
        btnClientes.setVisible(false);

        JButton btnVeiculos = new JButton("Veiculos");
        btnVeiculos.setBackground(Color.gray);
        btnVeiculos.setBounds(10, 275, 200, 30);
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
        JButton btnCadastro = new JButton("Cadastro");
        btnCadastro.setFont(new Font("Arial", Font.PLAIN, 20));
        btnCadastro.setBackground(Color.gray);
        btnCadastro.setBounds(10, 350, 200, 40);
        add(btnCadastro);

        JButton btnAddCliente = new JButton("Cadastrar Cliente");
        btnAddCliente.setBackground(Color.gray);
        btnAddCliente.setBounds(10, 395, 200, 30);
        add(btnAddCliente);
        btnAddCliente.setVisible(false);

        JButton btnRemoverCliente = new JButton("Remover Cliente");
        btnRemoverCliente.setBackground(Color.gray);
        btnRemoverCliente.setBounds(10, 425, 200, 30);
        add(btnRemoverCliente);
        btnRemoverCliente.setVisible(false);

        JButton btnAddVeiculo = new JButton("Cadastrar Veiculo");
        btnAddVeiculo.setBackground(Color.gray);
        btnAddVeiculo.setBounds(10, 455, 200, 30);
        add(btnAddVeiculo);
        btnAddVeiculo.setVisible(false);

        JButton btnRemoverVeiculo = new JButton("Remover Veiculo");
        btnRemoverVeiculo.setBackground(Color.gray);
        btnRemoverVeiculo.setBounds(10, 485, 200, 30);
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
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(Color.gray);
        btnSair.setBounds(10, 620, 200, 40);
        add(btnSair);
        btnSair.addActionListener(e -> System.exit(0));
    }
}