package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import api.FipeApiClient;
import controller.Cadastro;
import entities.Cliente;
import entities.Veiculo;
import entities.Vendas;
import jakarta.persistence.EntityManager;
import repositories.*;
import services.*;
import utils.JPAUtil;
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
    VendasService vendasServ = new VendasService();

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

        btnNovaVenda.addActionListener(e ->{
            painelPrincipal.removeAll();
            painelPrincipal.setLayout(new BorderLayout());

            JPanel painelCarros = new JPanel();
            List<Veiculo> veiculos = veiculoServ.listarTodos();
            String[] colunas = {"ID", "Tipo", "Modelo", "Marca", "Ano", "Preço", "Combustível"};

            JScrollPane tabelaScrollCarros = TabelaUtils.gerarTabela(colunas, veiculos, v -> new Object[]{
                    v.getId(),
                    v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão",
                    v.getModelo(),
                    v.getMarca(),
                    v.getAno(),
                    v.getPreco(),
                    v.getCombustivel()
            });
            painelCarros.add(tabelaScrollCarros);

            JPanel painelClientes = new JPanel();
            List<Cliente> clientes = clienteServ.listarTodos();
            String[] colunasC = {"ID", "Nome", "CPF", "Nascimento"};

            JScrollPane tabelaScrollClientes = TabelaUtils.gerarTabela(colunasC, clientes, c -> new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpf(),
                    c.getDateB()
            });
            painelClientes.add(tabelaScrollClientes);

            JPanel seleçãoParaVenda = new JPanel();

            JComboBox<Long> seleçãoCarros = new JComboBox<>();
            for (Veiculo veiculo : veiculos) {
                seleçãoCarros.addItem(veiculo.getId());
            }

            JComboBox<Long> seleçãoClientes = new JComboBox<>();
            for (Cliente cliente : clientes) {
                seleçãoClientes.addItem(cliente.getId());
            }

            JButton btnRealizarVenda = new JButton("Realizar Venda");

            btnRealizarVenda.addActionListener(ev ->{
                long idCliente = (long) seleçãoClientes.getSelectedItem();
                long idVeiculo = (long) seleçãoCarros.getSelectedItem();

                EntityManager em = JPAUtil.getEntityManager();

            try {
                Cliente cliente = em.find(Cliente.class, idCliente);
                Veiculo veiculo = em.find(Veiculo.class, idVeiculo);

                if (cliente == null || veiculo == null) {
                    JOptionPane.showMessageDialog(this, "Cliente ou veículo não encontrado.");
                    return;
                }

                // Cria objeto de venda e preenche os dados
                Vendas venda = new Vendas();
                venda.setCliente(cliente);
                venda.setVeiculo(veiculo);
                venda.setModeloVeiculo(veiculo.getModelo());
                venda.setMarcaVeiculo(veiculo.getMarca());
                venda.setPrecoVeiculo(veiculo.getPreco());
                venda.setNomeCliente(cliente.getNome());

                // Persiste no banco
                em.getTransaction().begin();
                em.persist(venda);
                em.getTransaction().commit();

                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
                

            } catch (Exception ev1) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + ev1.getMessage());
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                em.close();
            }
                });

            seleçãoParaVenda.add(seleçãoCarros);
            seleçãoParaVenda.add(seleçãoClientes);
            seleçãoParaVenda.add(btnRealizarVenda);

            JSplitPane splitPaneTabelas = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPaneTabelas.setTopComponent(painelCarros);
            splitPaneTabelas.setBottomComponent(painelClientes);
            splitPaneTabelas.setResizeWeight(0.5);
            splitPaneTabelas.setDividerLocation(200);

            JSplitPane splitPaneMaior = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPaneMaior.setTopComponent(splitPaneTabelas);
            splitPaneMaior.setBottomComponent(seleçãoParaVenda);
            splitPaneMaior.setResizeWeight(0.5);
            splitPaneMaior.setDividerLocation(400);
            
            painelPrincipal.add(splitPaneMaior, BorderLayout.NORTH);

            painelPrincipal.revalidate();
            painelPrincipal.repaint();
        });

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

            DatePickerSettings configuracoesInicial = new DatePickerSettings();
            configuracoesInicial.setLocale(new java.util.Locale("pt", "BR"));
            configuracoesInicial.setFormatForDatesCommonEra("dd/MM/yyyy");

            DatePickerSettings configuracoesFinal = new DatePickerSettings();
            configuracoesFinal.setLocale(new java.util.Locale("pt", "BR"));
            configuracoesFinal.setFormatForDatesCommonEra("dd/MM/yyyy");

            DatePicker dataInicialPicker = new DatePicker(configuracoesInicial);
            DatePicker dataFinalPicker = new DatePicker(configuracoesFinal);

            JPanel painelDatas = new JPanel();
                painelDatas.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

                painelDatas.add(new JLabel("Data Inicial:"));
                painelDatas.add(dataInicialPicker);

                painelDatas.add(new JLabel("Data Final:"));
                painelDatas.add(dataFinalPicker);

                JButton btnBuscarVendas = new JButton();
                btnBuscarVendas.setText("Buscar Vendas");
                painelDatas.add(btnBuscarVendas);
            
            JPanel resultadoFiltro = new JPanel(new BorderLayout());

                btnBuscarVendas.addActionListener(ev ->{
                    LocalDate dataInicio = dataInicialPicker.getDate();
                    LocalDate dataFim = dataFinalPicker.getDate();

                    if(dataInicio == null || dataFim == null){
                        JOptionPane.showMessageDialog(this, "Preencha os campos de datas para realizar a busca.");
                        return;
                    }

                    if (dataInicio.isAfter(dataFim)) {
                        JOptionPane.showMessageDialog(painelPrincipal, "Data inicial não pode ser depois da data final.");
                        return;
                    }

                     resultadoFiltro.removeAll();

                    LocalDateTime inicio = dataInicio.atStartOfDay();
                    LocalDateTime fim = dataFim.atTime(LocalTime.MAX);

                    List<Vendas> vendas1 = vendasServ.buscarPorPeriodo(inicio, fim);

                    JScrollPane tabelaScroll1 = TabelaUtils.gerarTabela(colunas, vendas1, ve -> new Object[]{
                            ve.getId(),
                            ve.getNomeCliente(),
                            ve.getModeloVeiculo(),
                            ve.getMarcaVeiculo(),
                            ve.getPrecoVeiculo(),
                            ve.getDataVenda().format(formatter)
                    }); 
                    
                    JLabel label = new JLabel("Resultado da busca entre os dias: "+ inicio.format(formatter)+" e "+fim.format(formatter));

                    resultadoFiltro.add(label, BorderLayout.NORTH);
                    resultadoFiltro.add(tabelaScroll1);
                    resultadoFiltro.revalidate();
                    resultadoFiltro.repaint();
                });
            
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(tabelaScroll);
            splitPane.setBottomComponent(resultadoFiltro);
            splitPane.setResizeWeight(0.5);
            splitPane.setDividerLocation(250);

            painelPrincipal.setLayout(new BorderLayout());
            painelPrincipal.add(painelDatas, BorderLayout.NORTH);
            painelPrincipal.add(splitPane, BorderLayout.CENTER);
            
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
