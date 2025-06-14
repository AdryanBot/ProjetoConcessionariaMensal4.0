package main;

import api.FipeApiClient;
import entities.FipeMarca;
import entities.FipeModelo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CadastroVeiculoPanel extends JPanel {

    private JComboBox<String> tipoComboBox;
    private JTextField anoCombsField;
    private JButton buscarMarcasBtn;

    private JTable tabelaMarcas;
    private JTable tabelaModelos;

    private DefaultTableModel modeloMarcas;
    private DefaultTableModel modeloModelos;

    private FipeApiClient apiClient = new FipeApiClient();

    public CadastroVeiculoPanel() {
        setLayout(new BorderLayout());

        // Inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Tipo de Veículo:"));
        String[] tipos = {"1 - Carro", "2 - Moto", "3 - Caminhão"};
        tipoComboBox = new JComboBox<>(tipos);
        inputPanel.add(tipoComboBox);

        inputPanel.add(new JLabel("Ano e combustível (ex: 2014-1):"));
        anoCombsField = new JTextField();
        inputPanel.add(anoCombsField);

        buscarMarcasBtn = new JButton("Buscar Marcas");
        inputPanel.add(buscarMarcasBtn);

        add(inputPanel, BorderLayout.NORTH);

        // Tabela de marcas
        modeloMarcas = new DefaultTableModel(new String[]{"Código", "Marca"}, 0);
        tabelaMarcas = new JTable(modeloMarcas);
        JScrollPane scrollMarcas = new JScrollPane(tabelaMarcas);
        scrollMarcas.setBorder(BorderFactory.createTitledBorder("Marcas"));

        // Tabela de modelos
        modeloModelos = new DefaultTableModel(new String[]{"Código", "Modelo"}, 0);
        tabelaModelos = new JTable(modeloModelos);
        JScrollPane scrollModelos = new JScrollPane(tabelaModelos);
        scrollModelos.setBorder(BorderFactory.createTitledBorder("Modelos"));

        JPanel tabelasPanel = new JPanel(new GridLayout(1, 2));
        tabelasPanel.add(scrollMarcas);
        tabelasPanel.add(scrollModelos);
        add(tabelasPanel, BorderLayout.CENTER);

        // Ações
        buscarMarcasBtn.addActionListener(e -> carregarMarcas());

        tabelaMarcas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = tabelaMarcas.getSelectedRow();
                if (row >= 0) {
                    String codigoMarca = modeloMarcas.getValueAt(row, 0).toString();
                    carregarModelos(codigoMarca);
                }
            }
        });
    }

    private void carregarMarcas() {
        modeloMarcas.setRowCount(0);
        modeloModelos.setRowCount(0);

        int tipoSelecionado = tipoComboBox.getSelectedIndex() + 1;
        String anoCombs = anoCombsField.getText().trim();

        if (anoCombs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o campo Ano e combustível.");
            return;
        }

        ArrayList<FipeMarca> marcas = apiClient.buscarMarcas(tipoSelecionado);
        for (FipeMarca marca : marcas) {
            modeloMarcas.addRow(new String[]{marca.getCodigo(), marca.getNome()});
        }
    }

    private void carregarModelos(String codigoMarca) {
        modeloModelos.setRowCount(0);

        int tipoSelecionado = tipoComboBox.getSelectedIndex() + 1;
        String anoCombs = anoCombsField.getText().trim();

        ArrayList<FipeModelo> modelos = apiClient.buscarModelos(tipoSelecionado, codigoMarca, anoCombs);
        for (FipeModelo modelo : modelos) {
            modeloModelos.addRow(new String[]{modelo.getCodigo(), modelo.getNome()});
        }
    }
}
