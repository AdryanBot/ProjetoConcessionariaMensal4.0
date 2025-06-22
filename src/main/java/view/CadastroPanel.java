package view;

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

        JPanel linhaNascimento = createDateFieldPanel("Data de Nascimento:");
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

    public void showRemoverCliente(JPanel painelPrincipal, Runnable onUpdate) {
        painelPrincipal.setLayout(new BorderLayout());

        List<Cliente> clientes = clienteServ.listarTodos();
        String[] colunas = {"Nome", "CPF", "Nascimento"};

        JScrollPane tabelaScroll = TabelaUtils.gerarTabela(colunas, clientes, c -> new Object[]{
                c.getNome(),
                c.getCpf(),
                c.getDateB()
        });

        JPanel painelRemoverCliente = createRemoveClientPanel(clientes, onUpdate, painelPrincipal);

        painelPrincipal.add(tabelaScroll, BorderLayout.NORTH);
        painelPrincipal.add(painelRemoverCliente, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    public void showRemoverVeiculo(JPanel painelPrincipal, Runnable onUpdate) {
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

        JPanel painelRemoverVeiculo = createRemoveVehiclePanel(veiculos, onUpdate, painelPrincipal);

        painelPrincipal.add(tabelaScroll, BorderLayout.NORTH);
        painelPrincipal.add(painelRemoverVeiculo, BorderLayout.CENTER);
        painelPrincipal.revalidate();
        painelPrincipal.repaint();
    }

    private JPanel createFieldPanel(String labelText, int fieldSize) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(220, 25));
        lbl.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JTextField field = new JTextField(fieldSize);
        field.setPreferredSize(new Dimension(300, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        
        linha.add(lbl);
        linha.add(field);
        linha.setBackground(Color.WHITE);
        
        return linha;
    }

    private JPanel createDateFieldPanel(String labelText) {
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(labelText);
        lbl.setPreferredSize(new Dimension(220, 25));
        lbl.setFont(new Font("Arial", Font.PLAIN, 20));
        
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(300, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setText("dd/mm/aaaa");
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals("dd/mm/aaaa")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText("dd/mm/aaaa");
                    field.setForeground(Color.GRAY);
                }
            }
        });
        
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String text = field.getText().replaceAll("[^0-9]", "");
                if (text.length() > 8) {
                    text = text.substring(0, 8);
                }
                
                if (text.length() >= 2) {
                    text = text.substring(0, 2) + "/" + text.substring(2);
                }
                if (text.length() >= 5) {
                    text = text.substring(0, 5) + "/" + text.substring(5);
                }
                
                field.setText(text);
                field.setCaretPosition(text.length());
            }
        });
        
        linha.add(lbl);
        linha.add(field);
        linha.setBackground(Color.WHITE);
        
        return linha;
    }

    private JPanel createRemoveClientPanel(List<Cliente> clientes, Runnable onUpdate, JPanel painelPrincipal) {
        JPanel painelRemoverCliente = new JPanel();

        JComboBox<String> seleçãoClientes = new JComboBox<>();
        for (Cliente cliente : clientes) {
            seleçãoClientes.addItem(cliente.getNome());
        }
        
        JButton btnRemover = new JButton("Remover Cliente");

        painelRemoverCliente.add(new JLabel("Selecione o cliente para remover:"));
        painelRemoverCliente.add(seleçãoClientes);
        painelRemoverCliente.add(btnRemover);

        btnRemover.addActionListener(e -> {
            int index = seleçãoClientes.getSelectedIndex();
            if (index >= 0) {
                Long id = clientes.get(index).getId();
                EntityManager em = JPAUtil.getEntityManager();
                
                try {
                    Cliente cliente = em.find(Cliente.class, id);

                if (cliente != null) {
                    // Verificar se há vendas vinculadas
                    Long vendasCount = em.createQuery("SELECT COUNT(v) FROM Vendas v WHERE v.cliente.id = :clienteId", Long.class)
                                        .setParameter("clienteId", id)
                                        .getSingleResult();
                    
                    if (vendasCount > 0) {
                        int resposta = JOptionPane.showConfirmDialog(null, 
                            "Este cliente possui " + vendasCount + " venda(s) vinculada(s).\n" +
                            "Deseja remover mesmo assim? O histórico de vendas será preservado.",
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                        
                        if (resposta != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    
                    em.getTransaction().begin();
                    em.remove(cliente);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "Cliente removido com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Cliente com ID " + id + " não encontrado.");
                    }

                    em.close();
                    
                    // Atualizar apenas a tabela
                    List<Cliente> clientesAtualizados = clienteServ.listarTodos();
                    String[] colunas = {"Nome", "CPF", "Nascimento"};
                    JScrollPane novaTabelaScroll = TabelaUtils.gerarTabela(colunas, clientesAtualizados, c -> new Object[]{
                            c.getNome(),
                            c.getCpf(),
                            c.getDateB()
                    });
                    
                    painelPrincipal.removeAll();
                    painelPrincipal.setLayout(new BorderLayout());
                    painelPrincipal.add(novaTabelaScroll, BorderLayout.NORTH);
                    painelPrincipal.add(createRemoveClientPanel(clientesAtualizados, onUpdate, painelPrincipal), BorderLayout.CENTER);
                    painelPrincipal.revalidate();
                    painelPrincipal.repaint();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao remover Cliente: " + ex.getMessage());
                }
            }
        });

        return painelRemoverCliente;
    }

    private JPanel createRemoveVehiclePanel(List<Veiculo> veiculos, Runnable onUpdate, JPanel painelPrincipal) {
        JPanel painelRemoverVeiculo = new JPanel();

        JComboBox<String> seleçãoCarros = new JComboBox<>();
        for (Veiculo veiculo : veiculos) {
            seleçãoCarros.addItem(veiculo.getModelo() + " - " + veiculo.getMarca());
        }
        
        JButton btnRemover = new JButton("Remover Veiculo");

        painelRemoverVeiculo.add(new JLabel("Selecione o veiculo para remover:"));
        painelRemoverVeiculo.add(seleçãoCarros);
        painelRemoverVeiculo.add(btnRemover);

        btnRemover.addActionListener(e -> {
            int index = seleçãoCarros.getSelectedIndex();
            if (index >= 0) {
                Long id = veiculos.get(index).getId();
                EntityManager em = JPAUtil.getEntityManager();
                
                try {
                    Veiculo veiculo = em.find(Veiculo.class, id);

                if (veiculo != null) {
                    // Verificar se há vendas vinculadas
                    Long vendasCount = em.createQuery("SELECT COUNT(v) FROM Vendas v WHERE v.veiculo.id = :veiculoId", Long.class)
                                        .setParameter("veiculoId", id)
                                        .getSingleResult();
                    
                    if (vendasCount > 0) {
                        int resposta = JOptionPane.showConfirmDialog(null, 
                            "Este veículo possui " + vendasCount + " venda(s) vinculada(s).\n" +
                            "Deseja remover mesmo assim? O histórico de vendas será preservado.",
                            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
                        
                        if (resposta != JOptionPane.YES_OPTION) {
                            return;
                        }
                    }
                    
                    em.getTransaction().begin();
                    em.remove(veiculo);
                    em.getTransaction().commit();
                    JOptionPane.showMessageDialog(null, "Veiculo removido com sucesso!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Veiculo com ID " + id + " não encontrado.");
                    }

                    em.close();
                    
                    // Atualizar apenas a tabela
                    List<Veiculo> veiculosAtualizados = veiculoServ.listarTodos();
                    String[] colunas = {"Tipo", "Modelo", "Marca", "Ano", "Preço", "Combustível"};
                    JScrollPane novaTabelaScroll = TabelaUtils.gerarTabela(colunas, veiculosAtualizados, v -> new Object[]{
                            v.getVeiculoTipo() == 1 ? "Carro" : v.getVeiculoTipo() == 2 ? "Moto" : "Caminhão",
                            v.getModelo(),
                            v.getMarca(),
                            v.getAno(),
                            v.getPreco(),
                            v.getCombustivel()
                    });
                    
                    painelPrincipal.removeAll();
                    painelPrincipal.setLayout(new BorderLayout());
                    painelPrincipal.add(novaTabelaScroll, BorderLayout.NORTH);
                    painelPrincipal.add(createRemoveVehiclePanel(veiculosAtualizados, onUpdate, painelPrincipal), BorderLayout.CENTER);
                    painelPrincipal.revalidate();
                    painelPrincipal.repaint();

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao remover Veiculo: " + ex.getMessage());
                }
            }
        });

        return painelRemoverVeiculo;
    }
}