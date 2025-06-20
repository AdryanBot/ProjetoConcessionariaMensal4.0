package ui;

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
        String[] colunas = {"ID", "Nome", "CPF", "Nascimento"};

        JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, clientes, c -> new Object[]{
                c.getId(),
                c.getNome(),
                c.getCpf(),
                c.getDateB()
        });

        JPanel vendasPorCliente = createClienteSalesPanel(clientes);

        painelPrincipal.add(tabelaScroll, BorderLayout.NORTH);
        painelPrincipal.add(vendasPorCliente, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createClienteSalesPanel(List<Cliente> clientes) {
        JPanel vendasPorCliente = new JPanel();

        JComboBox<Long> seleçãoClientes = new JComboBox<>();
        for (Cliente cliente : clientes) {
            seleçãoClientes.addItem(cliente.getId());
        }
        
        JButton buscarVendas = new JButton("Buscar vendas realizadas pelo cliente");

        buscarVendas.addActionListener(e -> {
            long idCliente = (long) seleçãoClientes.getSelectedItem();

            List<Vendas> vendas = vendasServ.buscarVendasPorClientes(idCliente);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String[] colunas1 = {"ID da Venda", "Modelo do Veiculo", "Preço", "Data"};

            JScrollPane tabelaScroll1 = TabelaUtils.gerarTabela(colunas1, vendas, v -> new Object[]{
                    v.getId(),
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