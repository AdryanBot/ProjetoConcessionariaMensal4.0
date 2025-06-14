package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

import api.FipeApiClient;
import controller.Cadastro;
import entities.Cliente;
import entities.Veiculo;
import entities.Vendas;
import repositories.*;
import services.*;
import main.TabelaUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

// Import do painel que criamos para cadastro de veículos via API FIPE
import main.CadastroVeiculoPanel;

public class MainTeste extends JFrame {
    FipeApiClient fipe = new FipeApiClient();
    Cadastro cadastros = new Cadastro();

    ArrayList<Veiculo> listaVeiculos = new ArrayList<>();
    ArrayList<Vendas> listaVendas = new ArrayList<>();
    ArrayList<Cliente> listaCliente = new ArrayList<>();

    VeiculoService veiculoServ = new VeiculoService();
    ClienteService clienteServ = new ClienteService();

    public MainTeste() {
        setTitle("Gestão de Concessionária");
        setSize(1080, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(Color.GRAY);
        sidebar.setBounds(0, 0, 220, 720);
        add(sidebar);

        JButton btnVendas = new JButton("Vendas");
        btnVendas.setFont(new Font("Arial", Font.PLAIN, 20));
        btnVendas.setBackground(Color.gray);
        btnVendas.setBounds(10, 50, 200, 40);
        sidebar.add(btnVendas);

        JButton btnNovaVenda = new JButton("Nova venda");
        btnNovaVenda.setBackground(Color.gray);
        btnNovaVenda.setBounds(10, 95, 200, 30);
        sidebar.add(btnNovaVenda);
        btnNovaVenda.setVisible(false);

        JButton btnvendasRealizadas = new JButton("Vendas realizadas");
        btnvendasRealizadas.setBackground(Color.gray);
        btnvendasRealizadas.setBounds(10, 125, 200, 30);
        sidebar.add(btnvendasRealizadas);
        btnvendasRealizadas.setVisible(false);

        btnVendas.addActionListener(e -> {
            btnNovaVenda.setVisible(!btnNovaVenda.isVisible());
            btnvendasRealizadas.setVisible(!btnvendasRealizadas.isVisible());
        });

        JButton btnItens = new JButton("Itens Cadastrados");
        btnItens.setFont(new Font("Arial", Font.PLAIN, 20));
        btnItens.setBackground(Color.gray);
        btnItens.setBounds(10, 200, 200, 40);
        sidebar.add(btnItens);

        JButton btnClientes = new JButton("Clientes");
        btnClientes.setBackground(Color.gray);
        btnClientes.setBounds(10, 245, 200, 30);
        sidebar.add(btnClientes);
        btnClientes.setVisible(false);

        JButton btnVeiculos = new JButton("Veiculos");
        btnVeiculos.setBackground(Color.gray);
        btnVeiculos.setBounds(10, 275, 200, 30);
        sidebar.add(btnVeiculos);
        btnVeiculos.setVisible(false);

        btnItens.addActionListener(e -> {
            btnClientes.setVisible(!btnClientes.isVisible());
            btnVeiculos.setVisible(!btnVeiculos.isVisible());
        });

        JButton btnCadastro = new JButton("Cadastro");
        btnCadastro.setFont(new Font("Arial", Font.PLAIN, 20));
        btnCadastro.setBackground(Color.gray);
        btnCadastro.setBounds(10, 350, 200, 40);
        sidebar.add(btnCadastro);

        JButton btnAddCliente = new JButton("Cadastrar Cliente");
        btnAddCliente.setBackground(Color.gray);
        btnAddCliente.setBounds(10, 395, 200, 30);
        sidebar.add(btnAddCliente);
        btnAddCliente.setVisible(false);

        JButton btnRemoverCliente = new JButton("Remover Cliente");
        btnRemoverCliente.setBackground(Color.gray);
        btnRemoverCliente.setBounds(10, 425, 200, 30);
        sidebar.add(btnRemoverCliente);
        btnRemoverCliente.setVisible(false);

        JButton btnAddVeiculo = new JButton("Cadastrar Veiculo");
        btnAddVeiculo.setBackground(Color.gray);
        btnAddVeiculo.setBounds(10, 455, 200, 30);
        sidebar.add(btnAddVeiculo);
        btnAddVeiculo.setVisible(false);

        JButton btnRemoverVeiculo = new JButton("Remover Veiculo");
        btnRemoverVeiculo.setBackground(Color.gray);
        btnRemoverVeiculo.setBounds(10, 485, 200, 30);
        sidebar.add(btnRemoverVeiculo);
        btnRemoverVeiculo.setVisible(false);

        btnCadastro.addActionListener(e -> {
            btnAddCliente.setVisible(!btnAddCliente.isVisible());
            btnAddVeiculo.setVisible(!btnAddVeiculo.isVisible());
            btnRemoverCliente.setVisible(!btnRemoverCliente.isVisible());
            btnRemoverVeiculo.setVisible(!btnRemoverVeiculo.isVisible());
        });

        // *** INTEGRAÇÃO DO CADASTRO VEÍCULO AQUI ***
        btnAddVeiculo.addActionListener(e -> {
            // Limpa painel principal e insere nosso painel customizado de cadastro via FIPE
            JPanel painelPrincipal = getPainelPrincipal();
            painelPrincipal.removeAll();
            painelPrincipal.setLayout(new BorderLayout());

            CadastroVeiculoPanel cadastroVeiculoPanel = new CadastroVeiculoPanel();
            painelPrincipal.add(cadastroVeiculoPanel, BorderLayout.CENTER);

            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        JButton btnSair = new JButton("Sair");
        btnSair.setBackground(Color.gray);
        btnSair.setBounds(10, 620, 200, 40);
        sidebar.add(btnSair);
        btnSair.addActionListener(e -> System.exit(0));

        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setBackground(Color.WHITE);
        painelPrincipal.setBounds(220, 0, 860, 720);
        painelPrincipal.setName("painelPrincipal");  // damos nome para recuperar depois
        add(painelPrincipal);

        btnvendasRealizadas.addActionListener(e -> {
            painelPrincipal.removeAll();
            painelPrincipal.setLayout(new BorderLayout());

            List<Vendas> vendas = VendasService.listarTodas();
            String[] colunas = {"ID", "Cliente", "Modelo", "Marca", "Preço", "Data"};
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, vendas, v -> new Object[]{
                    v.getId(),
                    v.getNomeCliente(),
                    v.getModeloVeiculo(),
                    v.getMarcaVeiculo(),
                    v.getPrecoVeiculo(),
                    v.getDataVenda().format(formatter)
            });

            painelPrincipal.add(tabelaScroll, BorderLayout.CENTER);
            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        btnClientes.addActionListener(e -> {
            painelPrincipal.removeAll();
            painelPrincipal.setLayout(new BorderLayout());

            List<Cliente> clientes = clienteServ.listarTodos();
            String[] colunas = {"ID", "Nome", "CPF", "Nascimento"};

            JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, clientes, c -> new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpf(),
                    c.getDateB()
            });

            painelPrincipal.add(tabelaScroll, BorderLayout.CENTER);
            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        btnVeiculos.addActionListener(e -> {
            painelPrincipal.removeAll();
            painelPrincipal.setLayout(new BorderLayout());

            List<Veiculo> veiculos = veiculoServ.listarTodos();
            String[] colunas = {"ID", "Tipo", "Modelo", "Marca", "Ano", "Preço", "Combustível"};

            JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, veiculos, v -> new Object[]{
                    v.getId(),
                    v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão",
                    v.getModelo(),
                    v.getMarca(),
                    v.getAno(),
                    v.getPreco(),
                    v.getCombustivel()
            });

            painelPrincipal.add(tabelaScroll, BorderLayout.CENTER);
            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

        setVisible(true);
    }

    // Método auxiliar para recuperar painelPrincipal, que criamos com setName
    private JPanel getPainelPrincipal() {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JPanel && "painelPrincipal".equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null;
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
        SwingUtilities.invokeLater(MainTeste::new);
    }
}
