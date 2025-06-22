package view;

import api.FipeApiClient;
import entities.FipeMarca;
import entities.FipeModelo;
import entities.FipeVeiculo;
import controller.Cadastro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CadastroVeiculoPanel extends JPanel {

    private JComboBox<String> tipoComboBox;
    private JTextField anoField;
    private JComboBox<String> combustivelComboBox;
    private JButton buscarMarcasBtn;

    private JTable tabelaMarcas;
    private JTable tabelaModelos;

    private DefaultTableModel modeloMarcas;
    private DefaultTableModel modeloModelos;

    private FipeApiClient apiClient = new FipeApiClient();
    Cadastro cadastro = new Cadastro();
    
    private ArrayList<FipeMarca> marcasCarregadas = new ArrayList<>();
    private ArrayList<FipeModelo> modelosCarregados = new ArrayList<>();

    public CadastroVeiculoPanel() {
        setLayout(new BorderLayout());

        // Inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Tipo de Veículo:"));
        String[] tipos = {"Carro", "Moto", "Caminhão"};
        tipoComboBox = new JComboBox<>(tipos);
        inputPanel.add(tipoComboBox);

        inputPanel.add(new JLabel("Ano:"));
        anoField = new JTextField();
        inputPanel.add(anoField);

        inputPanel.add(new JLabel("Combustível:"));
        String[] combustiveis = {"Gasolina", "Etanol", "Diesel"};
        combustivelComboBox = new JComboBox<>(combustiveis);
        inputPanel.add(combustivelComboBox);

        buscarMarcasBtn = new JButton("Buscar Marcas");
        inputPanel.add(buscarMarcasBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Tabela de marcas
        modeloMarcas = new DefaultTableModel(new String[]{"Marca"}, 0);
        tabelaMarcas = new JTable(modeloMarcas);
        JScrollPane scrollMarcas = new JScrollPane(tabelaMarcas);
        scrollMarcas.setBorder(BorderFactory.createTitledBorder("Marcas"));

        // Tabela de modelos
        modeloModelos = new DefaultTableModel(new String[]{"Modelo", "Preço"}, 0);
        tabelaModelos = new JTable(modeloModelos);
        JScrollPane scrollModelos = new JScrollPane(tabelaModelos);
        scrollModelos.setBorder(BorderFactory.createTitledBorder("Modelos"));

        JPanel tabelasPanel = new JPanel(new GridLayout(1, 2));
        tabelasPanel.add(scrollMarcas);
        tabelasPanel.add(scrollModelos);
        add(tabelasPanel, BorderLayout.CENTER);

        // Botão Cadastrar Veículo
        JButton cadastrarVeiculoBtn = new JButton("Cadastrar Veículo");
        cadastrarVeiculoBtn.setEnabled(false);
        add(cadastrarVeiculoBtn, BorderLayout.SOUTH);

        // Ações
        buscarMarcasBtn.addActionListener(e -> carregarMarcas());

        tabelaMarcas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = tabelaMarcas.getSelectedRow();
                if (row >= 0) {
                    carregarModelos(row);
                }
            }
        });

        tabelaModelos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (tabelaModelos.getSelectedRow() >= 0) {
                    cadastrarVeiculoBtn.setEnabled(true);
                }
            }
        });

        cadastrarVeiculoBtn.addActionListener(e -> {
            int rowModelo = tabelaModelos.getSelectedRow();
            int rowMarca = tabelaMarcas.getSelectedRow();

            if (rowModelo < 0 || rowMarca < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma marca e um modelo.");
                return;
            }

            String tipoSelecionado = tipoComboBox.getSelectedItem().toString();
            int tipoVeiculo = tipoComboBox.getSelectedIndex() + 1;
            String tipo = tipoSelecionado.equals("Carro") ? "cars"
                    : tipoSelecionado.equals("Moto") ? "motorcycles"
                    : "trucks";

            String marca = modeloMarcas.getValueAt(rowMarca, 0).toString();
            String modelo = modeloModelos.getValueAt(rowModelo, 0).toString();
            
            // Buscar códigos internos
            String codigoMarca = marcasCarregadas.get(rowMarca).getCodigo();
            String codigoModelo = modelosCarregados.get(rowModelo).getCodigo();
            String ano = anoField.getText().trim();
            int combustivelIndex = combustivelComboBox.getSelectedIndex() + 1;
            String anoCombs = ano + "-" + combustivelIndex;

            try {
                // Buscar dados completos da API FIPE
                FipeVeiculo detalhes = apiClient.buscarDetalhesVeiculo(tipoVeiculo, codigoMarca, codigoModelo, ano, combustivelIndex);
                
                if (detalhes != null) {
                    String preco = detalhes.getValor();
                    String combustivel = detalhes.getCombustivel();
                    String anoModelo = detalhes.getAnoModelo();
                    String mesRef = detalhes.getMesReferencia();

                    cadastro.adicionarVeiculo(tipo, marca, modelo, anoCombs, codigoModelo, preco, combustivel, anoModelo, mesRef);
                    JOptionPane.showMessageDialog(this, "Veículo cadastrado com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: Ano/combustível não disponível para este modelo.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar veículo: " + ex.getMessage());
            }
        });
    }

    private void carregarMarcas() {
        modeloMarcas.setRowCount(0);
        modeloModelos.setRowCount(0);
        marcasCarregadas.clear();
        modelosCarregados.clear();

        int tipoSelecionado = tipoComboBox.getSelectedIndex() + 1;
        String ano = anoField.getText().trim();

        if (ano.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o campo Ano.");
            return;
        }

        try {
            marcasCarregadas = apiClient.buscarMarcas(tipoSelecionado);
            if (marcasCarregadas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "API FIPE indisponível. Tente novamente em alguns segundos.");
                return;
            }
            for (FipeMarca marca : marcasCarregadas) {
                modeloMarcas.addRow(new String[]{marca.getNome()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: API FIPE com limite de requisições. Aguarde alguns segundos e tente novamente.");
        }
    }

    private void carregarModelos(int indiceMarca) {
        modeloModelos.setRowCount(0);
        modelosCarregados.clear();

        int tipoSelecionado = tipoComboBox.getSelectedIndex() + 1;
        String ano = anoField.getText().trim();
        int combustivelIndex = combustivelComboBox.getSelectedIndex() + 1;
        String codigoMarca = marcasCarregadas.get(indiceMarca).getCodigo();

        // Usar o método filtrado para mostrar apenas modelos disponíveis
        modelosCarregados = apiClient.buscarModelosFiltrados(tipoSelecionado, codigoMarca, ano, combustivelIndex);
        
        if (modelosCarregados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum modelo encontrado para o ano " + ano + " e combustível selecionado.");
            return;
        }
        
        // Buscar preço para cada modelo
        for (FipeModelo modelo : modelosCarregados) {
            try {
                FipeVeiculo detalhes = apiClient.buscarDetalhesVeiculo(tipoSelecionado, codigoMarca, modelo.getCodigo(), ano, combustivelIndex);
                String preco = detalhes != null ? detalhes.getValor() : "Consultar";
                modeloModelos.addRow(new String[]{modelo.getNome(), preco});
            } catch (Exception e) {
                modeloModelos.addRow(new String[]{modelo.getNome(), "Consultar"});
            }
        }
    }
}
