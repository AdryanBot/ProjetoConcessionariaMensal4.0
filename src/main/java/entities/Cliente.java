package entities;

import jakarta.persistence.*; // Importa as classes do JPA para persistência de dados
import java.util.ArrayList;
import java.util.List;

@Entity // Marca a classe como uma entidade JPA, ou seja, ela será mapeada para uma tabela no banco de dados
public class Cliente {

    @Id // Define o campo "id" como a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.AUTO) // Indica que o valor do ID será gerado automaticamente pelo banco
    private Long id;

    @Column(name = "name") // Define a coluna "name" no banco de dados
    private String nome;

    @Column(name = "cpf", nullable = false, unique = true) // A coluna "cpf" não pode ser nula e deve ser única no banco de dados
    private String cpf;

    @Column(name = "data_nascimento", nullable = false) // A coluna "data_nascimento" não pode ser nula
    private String nascimento;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true) // Define um relacionamento de um para muitos com a entidade "Vendas", onde cada cliente pode ter várias vendas
    private List<Vendas> venda = new ArrayList<>(); // Lista de vendas associadas ao cliente

    // Construtor padrão necessário para o JPA
    public Cliente() {
    }

    // Construtor com parâmetros para inicializar o cliente com nome, CPF e data de nascimento
    public Cliente(String nome, String cpf, String nascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.nascimento = nascimento;
    }

    // Getters para acessar os valores dos atributos
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getDateB() { return nascimento; }
}
