package entities;

import jakarta.persistence.*; // Importação das anotações e classes do Jakarta Persistence (JPA)
import java.util.ArrayList;
import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA, ou seja, será mapeada para uma tabela no banco de dados
@DiscriminatorValue("2") // Valor que será utilizado para discriminar esta classe quando for usada em uma herança por tabela única (Single Table Inheritance). "2" representa um tipo de veículo (moto) no banco de dados.
public class Moto extends Veiculo { // A classe Moto herda da classe Veiculo, ou seja, é um tipo de Veículo

    public Moto() {
        super(); // Chama o construtor da classe pai (Veiculo) sem parâmetros
    }

    // Construtor que inicializa todos os atributos específicos de uma Moto, passando para o construtor da classe pai os parâmetros necessários
    public Moto(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel,
                 String codigoFipe, String mesReferencia, String acronCombustivel) {
        super(veiculoTipo, preco, marcaVeiculo, modelo, anoModelo, combustivel, codigoFipe, mesReferencia, acronCombustivel);
        // Chama o construtor da classe pai (Veiculo), passando os parâmetros necessários para inicializar o veículo
    }
}
