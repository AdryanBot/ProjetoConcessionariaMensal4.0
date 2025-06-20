package ui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import entities.Cliente;
import entities.Veiculo;
import entities.Vendas;
import jakarta.persistence.EntityManager;
import services.ClienteService;
import services.VeiculoService;
import services.VendasService;
import utils.JPAUtil;
import utils.TabelaUtils;

public class VendasPanel {
    private VeiculoService veiculoServ = new VeiculoService();
    private ClienteService clienteServ = new ClienteService();
    private VendasService vendasServ = new VendasService();

    public void showNovaVenda(JPanel painelPrincipal) {
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

        JPanel seleçãoParaVenda = createVendaSelectionPanel(veiculos, clientes);

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
    }

    private JPanel createVendaSelectionPanel(List<Veiculo> veiculos, List<Cliente> clientes) {
        JPanel seleçãoParaVenda = new JPanel();

        JLabel textCarro = new JLabel("Carro:");
        JComboBox<Long> seleçãoCarros = new JComboBox<>();
        for (Veiculo veiculo : veiculos) {
            seleçãoCarros.addItem(veiculo.getId());
        }

        JLabel textCliente = new JLabel("Cliente:");
        JComboBox<Long> seleçãoClientes = new JComboBox<>();
        for (Cliente cliente : clientes) {
            seleçãoClientes.addItem(cliente.getId());
        }

        JButton btnRealizarVenda = new JButton("Realizar Venda");
        btnRealizarVenda.addActionListener(e -> realizarVenda(seleçãoClientes, seleçãoCarros));

        seleçãoParaVenda.add(textCarro);
        seleçãoParaVenda.add(seleçãoCarros);
        seleçãoParaVenda.add(textCliente);
        seleçãoParaVenda.add(seleçãoClientes);
        seleçãoParaVenda.add(btnRealizarVenda);

        return seleçãoParaVenda;
    }

    private void realizarVenda(JComboBox<Long> seleçãoClientes, JComboBox<Long> seleçãoCarros) {
        long idCliente = (long) seleçãoClientes.getSelectedItem();
        long idVeiculo = (long) seleçãoCarros.getSelectedItem();

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Cliente cliente = em.find(Cliente.class, idCliente);
            Veiculo veiculo = em.find(Veiculo.class, idVeiculo);

            if (cliente == null || veiculo == null) {
                JOptionPane.showMessageDialog(null, "Cliente ou veículo não encontrado.");
                return;
            }

            Vendas venda = new Vendas();
            venda.setCliente(cliente);
            venda.setVeiculo(veiculo);
            venda.setModeloVeiculo(veiculo.getModelo());
            venda.setMarcaVeiculo(veiculo.getMarca());
            venda.setPrecoVeiculo(veiculo.getPreco());
            venda.setNomeCliente(cliente.getNome());

            em.getTransaction().begin();
            em.persist(venda);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar venda: " + ex.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            em.close();
        }
    }

    public void showVendasRealizadas(JPanel painelPrincipal) {
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

        JPanel painelDatas = createDateFilterPanel(formatter);
        JPanel resultadoFiltro = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(tabelaScroll);
        splitPane.setBottomComponent(resultadoFiltro);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(250);

        painelPrincipal.add(painelDatas, BorderLayout.NORTH);
        painelPrincipal.add(splitPane, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createDateFilterPanel(DateTimeFormatter formatter) {
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

        JButton btnBuscarVendas = new JButton("Buscar Vendas");
        painelDatas.add(btnBuscarVendas);

        return painelDatas;
    }
}