package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import entities.Cliente;
import entities.Veiculo;
import jakarta.persistence.EntityManager;
import repositories.ClienteRepository;
import services.ClienteService;
import services.VeiculoService;
import utils.JPAUtil;
import utils.TabelaUtils;

public class CadastroPanel {
    private ClienteService clienteServ = new ClienteService();
    private VeiculoService veiculoServ = new VeiculoService();

    public void showCadastroCliente(JPanel painelPrincipal) {
        painelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel painelCadastroCliente = new JPanel();
        painelCadastroCliente.setLayout(new BoxLayout(painelCadastroCliente, BoxLayout.Y_AXIS));
        painelCadastroCliente.setBackground(Color.WHITE);

        JPanel linhaNome = createFieldPanel("Nome:", 20);
        JTextField nomeCliente = (JTextField) linhaNome.getComponent(1);

        JPanel linhaCpf = createFieldPanel("CPF:", 20);
        JTextField cpfCliente = (JTextField) linhaCpf.getComponent(1);

        JPanel linhaNascimento = createFieldPanel("Nascimento:", 20);
        JTextField nascimentoCliente = (JTextField) linhaNascimento.getComponent(1);

        JButton btnCadastrarCliente = new JButton("Cadastrar Cliente");
        btnCadastrarCliente.setPreferredSize(new Dimension(200, 40));
        btnCadastrarCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCadastrarCliente.setFont(new Font("Arial", Font.BOLD, 16));

        btnCadastrarCliente.addActionListener(e -> {
            String nome = nomeCliente.getText();
            String cpf = cpfCliente.getText();
            String nascimento = nascimentoCliente.getText();

            if (nome.isEmpty() || cpf.isEmpty() || nascimento.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos para cadastrar o cliente.");
                return;
            }

            Cliente cliente = new Cliente(nome, cpf, nascimento);
            new ClienteRepository().salvar(cliente);
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");
        });

        painelCadastroCliente.add(linhaNome);
        painelCadastroCliente.add(Box.createVerticalStrut(15));
        painelCadastroCliente.add(linhaCpf);
        painelCadastroCliente.add(Box.createVerticalStrut(15));
        painelCadastroCliente.add(linhaNascimento);
        painelCadastroCliente.add(Box.createVerticalStrut(55));
        painelCadastroCliente.add(btnCadastrarCliente);

        painelPrincipal.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 200));
        painelPrincipal.add(painelCadastroCliente, BorderLayout.CENTER);

        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    public void showCadastroVeiculo(JPanel painelPrincipal) {
        painelPrincipal.setLayout(new BorderLayout());

        CadastroVeiculoPanel cadastroVeiculoPanel = new CadastroVeiculoPanel();
        painelPrincipal.add(cadastroVeiculoPanel, BorderLayout.CENTER);

        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    public void showRemoverCliente(JPanel painelPrincipal) {
        painelPrincipal.setLayout(new BorderLayout());

        List<Cliente> clientes = clienteServ.listarTodos();
        String[] colunas = {"ID", "Nome", "CPF", "Nascimento"};

        JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, clientes, c -> new Object[]{
                c.getId(),
                c.getNome(),
                c.getCpf(),
                c.getDateB()
        });

        JPanel painelRemoverCliente = createRemoveClientPanel(clientes);

        painelPrincipal.add(tabelaScroll, BorderLayout.NORTH);
        painelPrincipal.add(painelRemoverCliente, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    public void showRemoverVeiculo(JPanel painelPrincipal) {
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

        JPanel painelRemoverVeiculo = createRemoveVehiclePanel(veiculos);

        painelPrincipal.add(tabelaScroll, BorderLayout.NORTH);
        painelPrincipal.add(painelRemoverVeiculo, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createFieldPanel(String labelText, int fieldSize) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(130, 25));
        lbl.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JTextField field = new JTextField(fieldSize);
        field.setPreferredSize(new Dimension(300, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        
        linha.add(lbl);
        linha.add(field);
        linha.setBackground(Color.WHITE);
        
        return linha;
    }

    private JPanel createRemoveClientPanel(List<Cliente> clientes) {
        JPanel painelRemoverCliente = new JPanel();

        JComboBox<Long> seleçãoClientes = new JComboBox<>();
        for (Cliente cliente : clientes) {
            seleçãoClientes.addItem(cliente.getId());
        }
        
        JButton btnRemover = new JButton("Remover Cliente");

        painelRemoverCliente.add(new JLabel("Selecione o cliente para remover:"));
        painelRemoverCliente.add(seleçãoClientes);
        painelRemoverCliente.add(btnRemover);

        btnRemover.addActionListener(e -> {
            Long id = (Long) seleçãoClientes.getSelectedItem();
            EntityManager em = JPAUtil.getEntityManager();
            
            try {
                Cliente cliente = em.find(Cliente.class, id);

                if (cliente != null) {
                    em.getTransaction().begin();
                    em.remove(cliente);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente com ID " + id + " não encontrado.");
                }

                em.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover Cliente: " + ex.getMessage());
            }
        });

        return painelRemoverCliente;
    }

    private JPanel createRemoveVehiclePanel(List<Veiculo> veiculos) {
        JPanel painelRemoverVeiculo = new JPanel();

        JComboBox<Long> seleçãoCarros = new JComboBox<>();
        for (Veiculo veiculo : veiculos) {
            seleçãoCarros.addItem(veiculo.getId());
        }
        
        JButton btnRemover = new JButton("Remover Veiculo");

        painelRemoverVeiculo.add(new JLabel("Selecione o veiculo para remover:"));
        painelRemoverVeiculo.add(seleçãoCarros);
        painelRemoverVeiculo.add(btnRemover);

        btnRemover.addActionListener(e -> {
            Long id = (Long) seleçãoCarros.getSelectedItem();
            EntityManager em = JPAUtil.getEntityManager();
            
            try {
                Veiculo veiculo = em.find(Veiculo.class, id);

                if (veiculo != null) {
                    em.getTransaction().begin();
                    em.remove(veiculo);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "Veiculo removido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Veiculo com ID " + id + " não encontrado.");
                }

                em.close();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao remover Veiculo: " + ex.getMessage());
            }
        });

        return painelRemoverVeiculo;
    }
}