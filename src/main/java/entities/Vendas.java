package entities;

import jakarta.persistence.*; // Importa as classes do JPA para persistência de dados
import java.time.LocalDateTime; // Importa a classe LocalDateTime para armazenar a data e hora da venda

@Entity // Marca a classe como uma entidade JPA, mapeada para uma tabela no banco de dados
@Table(name = "vendas") // Define o nome da tabela no banco de dados como "vendas"
public class Vendas {

    @Id // Define o campo "id" como chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) // Gera o valor do ID automaticamente
    private Long id;

    @Column(name = "data_venda", columnDefinition = "TIMESTAMP") // Define a coluna "data_venda" no banco com tipo "TIMESTAMP"
    private LocalDateTime dataVenda; // Armazena a data e hora da venda

    @ManyToOne // Define o relacionamento muitos-para-um entre Vendas e Cliente
    @JoinColumn(name = "cliente_id", nullable = false) // Define a coluna "cliente_id" para associar a venda ao cliente
    private Cliente cliente;

    @ManyToOne // Define o relacionamento muitos-para-um entre Vendas e Veiculo
    @JoinColumn(name = "veiculo_id", nullable = false) // Define a coluna "veiculo_id" para associar a venda ao veículo
    private Veiculo veiculo;

    private String nomeCliente; // Nome do cliente, armazenado separadamente
    private String modeloVeiculo; // Modelo do veículo, armazenado separadamente
    private String marcaVeiculo; // Marca do veículo, armazenado separadamente
    private String precoVeiculo; // Preço do veículo, armazenado separadamente

    // ===================== CONSTRUTORES =====================
    public Vendas() {
        this.dataVenda = LocalDateTime.now(); // Define automaticamente a data/hora atual ao criar uma venda
    }

    // Construtor para inicializar a venda com cliente, veículo, e outros detalhes
    public Vendas(Cliente cliente, Veiculo veiculo,
                  String nomeCliente, String modeloVeiculo, String marcaVeiculo, String precoVeiculo) {
        this.dataVenda = LocalDateTime.now(); // Define automaticamente a data/hora atual
        this.cliente = cliente; // Associa o cliente à venda
        this.veiculo = veiculo; // Associa o veículo à venda
        this.nomeCliente = nomeCliente; // Armazena o nome do cliente
        this.modeloVeiculo = modeloVeiculo; // Armazena o modelo do veículo
        this.marcaVeiculo = marcaVeiculo; // Armazena a marca do veículo
        this.precoVeiculo = precoVeiculo; // Armazena o preço do veículo
    }

    public Vendas(LocalDateTime now, Cliente cliente, Veiculo veiculo) {
        // Este construtor parece estar incompleto ou não é utilizado. Fica à disposição para ajustes.
    }

    // ===================== GETTERS E SETTERS =====================
    public Long getId() {
        return id; // Retorna o ID da venda
    }

    public LocalDateTime getDataVenda() {
        return dataVenda; // Retorna a data e hora da venda
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda; // Permite definir a data e hora da venda
    }

    public Cliente getCliente() {
        return cliente; // Retorna o cliente associado à venda
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente; // Permite definir o cliente associado à venda
    }

    public Veiculo getVeiculo() {
        return veiculo; // Retorna o veículo associado à venda
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo; // Permite definir o veículo associado à venda
    }

    public String getNomeCliente() {
        return nomeCliente; // Retorna o nome do cliente
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente; // Permite definir o nome do cliente
    }

    public String getModeloVeiculo() {
        return modeloVeiculo; // Retorna o modelo do veículo
    }

    public void setModeloVeiculo(String modeloVeiculo) {
        this.modeloVeiculo = modeloVeiculo; // Permite definir o modelo do veículo
    }

    public String getMarcaVeiculo() {
        return marcaVeiculo; // Retorna a marca do veículo
    }

    public void setMarcaVeiculo(String marcaVeiculo) {
        this.marcaVeiculo = marcaVeiculo; // Permite definir a marca do veículo
    }

    public String getPrecoVeiculo() {
        return precoVeiculo; // Retorna o preço do veículo
    }

    public void setPrecoVeiculo(String precoVeiculo) {
        this.precoVeiculo = precoVeiculo; // Permite definir o preço do veículo
    }
}
