package entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*; // Importa as classes do JPA para persistência de dados

@Entity // Marca a classe como uma entidade JPA, ou seja, ela será mapeada para uma tabela no banco de dados
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Define a estratégia de herança para a entidade. SINGLE_TABLE significa que todas as subclasses (como Carro, Moto, Caminhao) terão uma única tabela no banco
@DiscriminatorColumn(name = "veiculoTipo", discriminatorType = DiscriminatorType.INTEGER) // Define a coluna "veiculoTipo" como a coluna de discriminação para distinguir os tipos de veículo na tabela
public abstract class Veiculo { // Classe abstrata que serve como base para os tipos específicos de veículos

    @Id // Define o campo "id" como a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) // Indica que o valor do ID será gerado automaticamente pelo banco
    private Long id;

    @Column(insertable = false, updatable = false) // O valor de "veiculoTipo" é controlado automaticamente pelo discriminador (não é inserido ou atualizado diretamente)
    protected int veiculoTipo;

    @Column(nullable = false) // Indica que a coluna não pode ser nula no banco de dados
    protected String preco;

    @Column(nullable = false)
    protected String marcaVeiculo;

    @Column(nullable = false)
    protected String modelo;

    @Column(nullable = false)
    protected String anoModelo;

    @Column(nullable = false)
    protected String combustivel;

    @Column(nullable = false)
    protected String codigoFipe;

    @Column(nullable = false)
    protected String mesReferencia;

    @Column(nullable = false)
    protected String acronCombustivel;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true) // Relacionamento de um para muitos entre Veiculo e Vendas. Toda venda é associada a um veículo. 
    private List<Vendas> venda = new ArrayList<>(); // Lista de vendas associadas ao veículo

    public Veiculo() {} // Construtor padrão (necessário para entidades JPA)

    // Construtor que inicializa os atributos do veículo
    public Veiculo(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel, 
                   String codigoFipe, String mesReferencia, String acronCombustivel) {
        this.veiculoTipo = veiculoTipo;
        this.preco = preco;
        this.marcaVeiculo = marcaVeiculo;
        this.modelo = modelo;
        this.anoModelo = anoModelo;
        this.combustivel = combustivel;
        this.codigoFipe = codigoFipe;
        this.mesReferencia = mesReferencia;
        this.acronCombustivel = acronCombustivel;
    }

    // Getters comuns para acessar os atributos da classe
    public Long getId() { return id; }
    public int getVeiculoTipo() { return veiculoTipo; }
    public String getPreco() { return preco; }
    public String getMarca() { return marcaVeiculo; }
    public String getModelo() { return modelo; }
    public String getAno() { return anoModelo; }
    public String getCombustivel() { return combustivel; }
    public String getCodigoFipe() { return codigoFipe; }
    public String getMesReferencia() { return mesReferencia; }
    public String getAcronCombustivel() { return acronCombustivel; }

    // Setters para modificar os valores dos atributos
    public void setPreco(String preco) { this.preco = preco; }
    public void setMarcaVeiculo(String marcaVeiculo) { this.marcaVeiculo = marcaVeiculo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setAnoModelo(String anoModelo) { this.anoModelo = anoModelo; }
    public void setCombustivel(String combustivel) { this.combustivel = combustivel; }
    public void setCodigoFipe(String codigoFipe) { this.codigoFipe = codigoFipe; }
    public void setMesReferencia(String mesReferencia) { this.mesReferencia = mesReferencia; }
    public void setAcronCombustivel(String acronCombustivel) { this.acronCombustivel = acronCombustivel; }
}
