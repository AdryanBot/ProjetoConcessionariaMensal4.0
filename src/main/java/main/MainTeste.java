package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Importa o cliente da API da FIPE e a classe responsável pelo cadastro e operações do sistema
import api.FipeApiClient;
import controller.Cadastro;
import entities.Cliente;
import entities.Veiculo;
import entities.Vendas;
import repositories.*;
import services.*;

// Importações do Hibernate para configurar conexão com o banco de dados
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
// Importações para leitura de dados e redirecionamento de logs
import java.util.Scanner;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;


public class MainTeste extends JFrame{
    FipeApiClient fipe = new FipeApiClient();
    Cadastro cadastros = new Cadastro(); 
    

    ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
    ArrayList<Vendas> listaVendas = new ArrayList<>();
    ArrayList<Cliente> listaCliente = new ArrayList<>();

    VeiculoService veiculoServ = new VeiculoService();
    ClienteService clienteServ = new ClienteService();

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

        

        //alterna a visibilidade das vendas
        btnvendasRealizadas.addActionListener(e -> {
            painelPrincipal.removeAll(); // limpa tudo do painel
    
            List<Vendas> vendas = VendasService.listarTodas();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int y = 10;

            for (Vendas v : vendas) {
                String texto = "ID: " + v.getId() +
                        " | Cliente: " + v.getNomeCliente() +
                        " | Veículo: " + v.getModeloVeiculo() + " - " + v.getMarcaVeiculo() +
                        " | Preço: " + v.getPrecoVeiculo() +
                        " | Data: " + v.getDataVenda().format(formatter);

                JLabel label = new JLabel(texto);
                label.setBounds(10, y, 550, 20);
                painelPrincipal.add(label);
                y += 25;
            }

            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });
        
        btnClientes.addActionListener(e ->{
            painelPrincipal.removeAll();
            
            List<Cliente> clientes = clienteServ.listarTodos();

            for (Cliente c : clientes) {
                String texto = "ID: " + c.getId() +
                        " | Nome: " + c.getNome() +
                        " | CPF: " + c.getCpf() +
                        " | Data de Nascimento: " + c.getDateB();
                JLabel label = new JLabel(texto);
                label.setBounds(10, 0, 550, 20);
                painelPrincipal.add(label);
            }

            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });
        
        //alterna a visibilidade das veiculos
        btnVeiculos.addActionListener(e -> {
            painelPrincipal.removeAll(); // limpa tudo do painel
            
            List<Veiculo> veiculos = veiculoServ.listarTodos();

            for(Veiculo v: veiculos){
                String texto = "ID: " + v.getId() +
                        " | Tipo: " + (v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão") +
                        " | Modelo: " + v.getModelo() +
                        " | Marca: " + v.getMarca() +
                        " | Ano: " + v.getAno() +
                        " | Preço: " + v.getPreco() +
                        " | Combustível: " + v.getCombustivel();
                JLabel label = new JLabel(texto);
                label.setBounds(10, 0, 550, 20); // Ajuste conforme o tamanho do painel
                painelPrincipal.add(label);
            }
            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        setVisible(true);
    }
    
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        // Testa conexão com o banco de dados usando Hibernate
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!\n");
            session.close();
            sessionFactory.close();
        } catch (Exception e) {
            // Se houver erro ao conectar, exibe e encerra o programa
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        SwingUtilities.invokeLater(MainTeste::new);
    }
}
