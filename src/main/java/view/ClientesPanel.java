package view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import entities.Cliente;
import entities.Vendas;
import services.ClienteService;
import services.VendasService;
import utils.TabelaUtils;

public class ClientesPanel {
    private ClienteService clienteServ = new ClienteService();
    private VendasService vendasServ = new VendasService();

    public void showClientes(JPanel painelPrincipal) {
        painelPrincipal.setLayout(new BorderLayout());

        List<Cliente> clientes = clienteServ.listarTodos();
        String[] colunas = {"Nome", "CPF", "Nascimento"};

        JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, clientes, c -> new Object[]{
                c.getNome(),
                c.getCpf(),
                c.getDateB()
        });

        JPanel vendasPorCliente = createClienteSalesPanel(clientes);
        
        JLabel countLabel = new JLabel("Total de Clientes: " + clientes.size());
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, 14f));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(countLabel, BorderLayout.NORTH);
        topPanel.add(tabelaScroll, BorderLayout.CENTER);

        painelPrincipal.add(topPanel, BorderLayout.NORTH);
        painelPrincipal.add(vendasPorCliente, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createClienteSalesPanel(List<Cliente> clientes) {
        JPanel vendasPorCliente = new JPanel();

        JComboBox<String> seleçãoClientes = new JComboBox<>();
        for (Cliente cliente : clientes) {
            seleçãoClientes.addItem(cliente.getNome());
        }
        
        JButton buscarVendas = new JButton("Buscar vendas realizadas pelo cliente");

        buscarVendas.addActionListener(e -> {
            String idCliente = (String) seleçãoClientes.getSelectedItem();

            List<Vendas> vendas = vendasServ.buscarVendasPorClientes(idCliente);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String[] colunas1 = {"Modelo do Veiculo", "Preço", "Data"};

            JScrollPane tabelaScroll1 = TabelaUtils.gerarTabela(colunas1, vendas, v -> new Object[]{
                    v.getVeiculo().getModelo(),
                    v.getVeiculo().getPreco(),
                    v.getDataVenda().format(formatter)
            });
            
            vendasPorCliente.removeAll();
            vendasPorCliente.add(tabelaScroll1);
            vendasPorCliente.revalidate();
            vendasPorCliente.repaint();
        });

        vendasPorCliente.add(seleçãoClientes);
        vendasPorCliente.add(buscarVendas);

        return vendasPorCliente;
    }
}