package ui;

import javax.swing.*;

public class NavigationController {
    private JPanel painelPrincipal;
    private VendasPanel vendasPanel;
    private ClientesPanel clientesPanel;
    private VeiculosPanel veiculosPanel;
    private CadastroPanel cadastroPanel;

    public NavigationController(JPanel painelPrincipal) {
        this.painelPrincipal = painelPrincipal;
        initializePanels();
    }

    private void initializePanels() {
        vendasPanel = new VendasPanel();
        clientesPanel = new ClientesPanel();
        veiculosPanel = new VeiculosPanel();
        cadastroPanel = new CadastroPanel();
    }

    public void showNovaVenda() {
        clearPanel();
        vendasPanel.showNovaVenda(painelPrincipal);
    }

    public void showVendasRealizadas() {
        clearPanel();
        vendasPanel.showVendasRealizadas(painelPrincipal);
    }

    public void showClientes() {
        clearPanel();
        clientesPanel.showClientes(painelPrincipal);
    }

    public void showVeiculos() {
        clearPanel();
        veiculosPanel.showVeiculos(painelPrincipal);
    }

    public void showCadastroCliente() {
        clearPanel();
        cadastroPanel.showCadastroCliente(painelPrincipal);
    }

    public void showCadastroVeiculo() {
        clearPanel();
        cadastroPanel.showCadastroVeiculo(painelPrincipal);
    }

    public void showRemoverCliente() {
        clearPanel();
        cadastroPanel.showRemoverCliente(painelPrincipal);
    }

    public void showRemoverVeiculo() {
        clearPanel();
        cadastroPanel.showRemoverVeiculo(painelPrincipal);
    }

    private void clearPanel() {
        painelPrincipal.removeAll();
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }
}