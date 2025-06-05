package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Importa o cliente da API da FIPE e a classe responsável pelo cadastro e operações do sistema
import api.FipeApiClient;
import controller.Cadastro;

// Importações do Hibernate para configurar conexão com o banco de dados
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Importações para leitura de dados e redirecionamento de logs
import java.util.Scanner;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;


public class MainTeste extends JFrame{

    public MainTeste(){
        setTitle("Gestão de Concessionária");
        setSize(1080, 720);
        setLocationRelativeTo(null); // Centraliza na tela
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null); // Layout absoluto (posição fixa)

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(Color.GRAY);
        sidebar.setBounds(0, 0, 220, 720);
        add(sidebar);

        // Botão 1
        JButton btnVendas = new JButton("Vendas");
        btnVendas.setFont(new Font("Arial",Font.PLAIN,20));
        btnVendas.setBackground(Color.gray);
        btnVendas.setBounds(10, 50, 200, 40);
        sidebar.add(btnVendas);

        //submenu nova venda
        JButton btnNovaVenda = new JButton("Nova venda");
        btnNovaVenda.setBackground(Color.gray);
        btnNovaVenda.setBounds(10,95,200,30);
        sidebar.add(btnNovaVenda);
        btnNovaVenda.setVisible(false);

        //submenu vendas realizadas
        JButton btnvendasRealizadas = new JButton("Vendas realizadas");
        btnvendasRealizadas.setBackground(Color.gray);
        btnvendasRealizadas.setBounds(10,125,200,30);
        sidebar.add(btnvendasRealizadas);
        btnvendasRealizadas.setVisible(false);

        btnVendas.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnNovaVenda.setVisible(!btnNovaVenda.isVisible());
                btnvendasRealizadas.setVisible(!btnvendasRealizadas.isVisible());
            }
        });

        // Botão 2
        JButton btnItens = new JButton("Itens Cadastrados");
        btnItens.setFont(new Font("Arial",Font.PLAIN,20));
        btnItens.setBackground(Color.gray);
        btnItens.setBounds(10, 200, 200, 40);
        sidebar.add(btnItens);

        //submenu clientes
        JButton btnClientes = new JButton("Clientes");
        btnClientes.setBackground(Color.gray);
        btnClientes.setBounds(10,245,200,30);
        sidebar.add(btnClientes);
        btnClientes.setVisible(false);

        //submenu veiculos
        JButton btnVeiculos = new JButton("Veiculos");
        btnVeiculos.setBackground(Color.gray);
        btnVeiculos.setBounds(10,275,200,30);
        sidebar.add(btnVeiculos);
        btnVeiculos.setVisible(false);


        btnItens.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnClientes.setVisible(!btnClientes.isVisible());
                btnVeiculos.setVisible(!btnVeiculos.isVisible());
            }
        });

        // Botão 3
        JButton btnCadastro = new JButton("Cadastro");
        btnCadastro.setFont(new Font("Arial",Font.PLAIN,20));
        btnCadastro.setBackground(Color.gray);
        btnCadastro.setBounds(10, 350, 200, 40);
        sidebar.add(btnCadastro);

        //submenu adicionar cliente
        JButton btnAddCliente = new JButton("Cadastrar Cliente");
        btnAddCliente.setBackground(Color.gray);
        btnAddCliente.setBounds(10,395,200,30);
        sidebar.add(btnAddCliente);
        btnAddCliente.setVisible(false);

        //submenu remover cliente
        JButton btnRemoverCliente = new JButton("Remover Cliente");
        btnRemoverCliente.setBackground(Color.gray);
        btnRemoverCliente.setBounds(10,425,200,30);
        sidebar.add(btnRemoverCliente);
        btnRemoverCliente.setVisible(false);

        //submenu adicionar veiculo
        JButton btnAddVeiculo = new JButton("Cadastrar Veiculo");
        btnAddVeiculo.setBackground(Color.gray);
        btnAddVeiculo.setBounds(10,455,200,30);
        sidebar.add(btnAddVeiculo);
        btnAddVeiculo.setVisible(false);

        //submenu remover veiculo
        JButton btnRemoverVeiculo = new JButton("Remover Veiculo");
        btnRemoverVeiculo.setBackground(Color.gray);
        btnRemoverVeiculo.setBounds(10,485,200,30);
        sidebar.add(btnRemoverVeiculo);
        btnRemoverVeiculo.setVisible(false);


        btnCadastro.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                btnAddCliente.setVisible(!btnAddCliente.isVisible());
                btnAddVeiculo.setVisible(!btnAddVeiculo.isVisible());
                btnRemoverCliente.setVisible(!btnRemoverCliente.isVisible());
                btnRemoverVeiculo.setVisible(!btnRemoverVeiculo.isVisible());
            }
        });

        // Botão Sair
        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(Color.gray);
        btnSair.setBounds(10, 620, 200, 40);
        sidebar.add(btnSair);
        btnSair.addActionListener(e -> System.exit(0));

        // Painel principal
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setBounds(220, 0, 860, 720);
        add(painelPrincipal);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainTeste::new);
    }
}
