package view;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import entities.Veiculo;
import entities.Vendas;
import jakarta.persistence.EntityManager;
import services.VeiculoService;
import services.VendasService;
import utils.JPAUtil;
import utils.TabelaUtils;

public class VeiculosPanel {
    private VeiculoService veiculoServ = new VeiculoService();
    private VendasService vendasServ = new VendasService();

    public void showVeiculos(JPanel painelPrincipal) {
        painelPrincipal.setLayout(new BorderLayout());

        List<Veiculo> veiculos = veiculoServ.listarTodos();
        String[] colunas = {"Tipo", "Modelo", "Marca", "Ano", "Preço", "Combustível"};

        JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, veiculos, v -> new Object[]{
                v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão",
                v.getModelo(),
                v.getMarca(),
                v.getAno(),
                v.getPreco(),
                v.getCombustivel()
        });

        JPanel filtros = createVehicleFiltersPanel(veiculos);
        
        JLabel countLabel = new JLabel("Total de Veículos: " + veiculos.size());
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        countLabel.setFont(countLabel.getFont().deriveFont(Font.BOLD, 14f));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(tabelaScroll);
        splitPane.setBottomComponent(filtros);

        painelPrincipal.add(countLabel, BorderLayout.NORTH);
        painelPrincipal.add(splitPane, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createVehicleFiltersPanel(List<Veiculo> veiculos) {
        JPanel filtros = new JPanel();
        filtros.setLayout(new BoxLayout(filtros, BoxLayout.Y_AXIS));

        JButton pesquisaParcial = new JButton("Pesquisa Parcial");
        JButton atualizarPreco = new JButton("Atualizar Preço");
        JButton clientesPorVeiculo = new JButton("Clientes que compraram o veículo");

        pesquisaParcial.setAlignmentX(Component.CENTER_ALIGNMENT);
        atualizarPreco.setAlignmentX(Component.CENTER_ALIGNMENT);
        clientesPorVeiculo.setAlignmentX(Component.CENTER_ALIGNMENT);

        filtros.add(pesquisaParcial);
        filtros.add(Box.createVerticalStrut(20));
        filtros.add(atualizarPreco);
        filtros.add(Box.createVerticalStrut(20));
        filtros.add(clientesPorVeiculo);

        setupFilterActions(filtros, pesquisaParcial, atualizarPreco, clientesPorVeiculo, veiculos);

        return filtros;
    }

    private void setupFilterActions(JPanel filtros, JButton pesquisaParcial, JButton atualizarPreco, 
                                  JButton clientesPorVeiculo, List<Veiculo> veiculos) {
        
        pesquisaParcial.addActionListener(e -> showPartialSearchPanel(filtros));
        atualizarPreco.addActionListener(e -> showPriceUpdatePanel(filtros, veiculos));
        clientesPorVeiculo.addActionListener(e -> showClientsByVehiclePanel(filtros, veiculos));
    }

    private void showPartialSearchPanel(JPanel filtros) {
        JPanel inputs = new JPanel();
        JLabel nomeParcial = new JLabel("Nome parcial do veiculo");
        JTextField nomeCarro = new JTextField(20);
        JButton pesquisar = new JButton("Procurar");
        
        inputs.add(nomeParcial);
        inputs.add(nomeCarro);
        inputs.add(pesquisar);

        pesquisar.addActionListener(e -> {
            String nome = nomeCarro.getText();
            if (nome != null && !nome.isEmpty()) {
                List<Veiculo> veiculosEncontrados = veiculoServ.buscarPorModelo(nome);
                String[] colunas1 = {"Tipo", "Modelo", "Marca", "Ano", "Preço", "Combustível"};
                
                JScrollPane tabelaScroll1 = TabelaUtils.gerarTabela(colunas1, veiculosEncontrados, v -> new Object[]{
                        v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão",
                        v.getModelo(),
                        v.getMarca(),
                        v.getAno(),
                        v.getPreco(),
                        v.getCombustivel()
                });
                
                filtros.removeAll();
                filtros.add(tabelaScroll1);
                filtros.revalidate();
                filtros.repaint();
            }
        });

        filtros.removeAll();
        filtros.add(inputs);
        filtros.revalidate();
        filtros.repaint();
    }

    private void showPriceUpdatePanel(JPanel filtros, List<Veiculo> veiculos) {
        JPanel inputs = new JPanel();
        JLabel labelVeiculo = new JLabel("Selecione o veículo");
        JComboBox<String> seleçãoCarros = new JComboBox<>();
        
        // Adiciona uma opção padrão
        seleçãoCarros.addItem("Selecione um veículo...");
        
        // Adiciona os veículos com formato: "Marca Modelo (Ano) - Tipo"
        for (Veiculo veiculo : veiculos) {
            String tipoVeiculo = veiculo.getVeiculoTipo() == 1 ? "Carro" : 
                               veiculo.getVeiculoTipo() == 2 ? "Moto" : "Caminhão";
            String displayText = String.format("%s %s (%s) - %s", 
                veiculo.getMarca(), 
                veiculo.getModelo(), 
                veiculo.getAno(), 
                tipoVeiculo);
            seleçãoCarros.addItem(displayText);
        }
        
        JButton procurar = new JButton("Procurar");
        inputs.add(labelVeiculo);
        inputs.add(seleçãoCarros);
        inputs.add(procurar);

        procurar.addActionListener(e -> {
            int selectedIndex = seleçãoCarros.getSelectedIndex();
            
            if (selectedIndex == 0) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione um veículo!");
                return;
            }
            
            // O índice -1 porque o primeiro item é "Selecione um veículo..."
            Veiculo veiculo = veiculos.get(selectedIndex - 1);

            if (veiculo != null) {
                showPriceUpdateForm(filtros, veiculo);
            } else {
                JOptionPane.showMessageDialog(null, "Veiculo nao encontrado!");
            }
        });

        filtros.removeAll();
        filtros.add(inputs);
        filtros.revalidate();
        filtros.repaint();
    }

    private void showPriceUpdateForm(JPanel filtros, Veiculo veiculo) {
        JPanel atualização = new JPanel();
        JLabel text = new JLabel("Digite o novo preço com as siglas (ex: R$ 99.999): ");
        JTextField novoPreco = new JTextField(20);
        JButton atualizar = new JButton("Atualizar");
        
        atualização.add(text);
        atualização.add(novoPreco);
        atualização.add(atualizar);

        atualizar.addActionListener(e -> {
            if (novoPreco.getText() != null && !novoPreco.getText().isEmpty()) {
                veiculo.setPreco(novoPreco.getText());

                EntityManager em = JPAUtil.getEntityManager();
                em.getTransaction().begin();
                em.merge(veiculo);
                em.getTransaction().commit();
                em.close();
                
                JOptionPane.showMessageDialog(null, "Preço atualizado!");
            } else {
                JOptionPane.showMessageDialog(null, "Preencha o campo de preço!");
            }
        });

        filtros.removeAll();
        filtros.add(atualização);
        filtros.revalidate();
        filtros.repaint();
    }

    private void showClientsByVehiclePanel(JPanel filtros, List<Veiculo> veiculos) {
        JPanel inputs = new JPanel();
        JLabel labelVeiculo = new JLabel("Selecione o veículo");
        JComboBox<String> seleçãoCarros = new JComboBox<>();
        
        // Adiciona uma opção padrão
        seleçãoCarros.addItem("Selecione um veículo...");
        
        // Adiciona os veículos com formato: "Marca Modelo (Ano) - Tipo"
        for (Veiculo veiculo : veiculos) {
            String tipoVeiculo = veiculo.getVeiculoTipo() == 1 ? "Carro" : 
                               veiculo.getVeiculoTipo() == 2 ? "Moto" : "Caminhão";
            String displayText = String.format("%s %s (%s) - %s", 
                veiculo.getMarca(), 
                veiculo.getModelo(), 
                veiculo.getAno(), 
                tipoVeiculo);
            seleçãoCarros.addItem(displayText);
        }
        
        JButton procurar = new JButton("Procurar");
        inputs.add(labelVeiculo);
        inputs.add(seleçãoCarros);
        inputs.add(procurar);

        procurar.addActionListener(e -> {
            int selectedIndex = seleçãoCarros.getSelectedIndex();
            
            if (selectedIndex == 0) {
                JOptionPane.showMessageDialog(null, "Por favor, selecione um veículo!");
                return;
            }
            
            // O índice -1 porque o primeiro item é "Selecione um veículo..."
            Veiculo veiculo = veiculos.get(selectedIndex - 1);
            Long id = veiculo.getId();

            List<Vendas> vendas = vendasServ.buscarClientePorVeiculo(id);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String[] colunas1 = {"Nome do cliente", "CPF do cliente", "Data"};

            JScrollPane tabelaScroll1 = TabelaUtils.gerarTabela(colunas1, vendas, v -> new Object[]{
                    v.getCliente().getNome(),
                    v.getCliente().getCpf(),
                    v.getDataVenda().format(formatter)
            });

            filtros.removeAll();
            filtros.add(tabelaScroll1);
            filtros.revalidate();
            filtros.repaint();
        });

        filtros.removeAll();
        filtros.add(inputs);
        filtros.revalidate();
        filtros.repaint();
    }
}