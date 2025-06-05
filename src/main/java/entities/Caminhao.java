package entities;

import jakarta.persistence.*; // Importação das anotações e classes do Jakarta Persistence (JPA)
import java.util.ArrayList;
import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA, ou seja, será mapeada para uma tabela no banco de dados
@DiscriminatorValue("3") // Valor que será utilizado para discriminar esta classe quando for usada em uma herança por tabela única (Single Table Inheritance). "3" representa um tipo de veículo (caminhão) no banco de dados.
public class Caminhao extends Veiculo { // A classe Caminhao herda da classe Veiculo, ou seja, é um tipo de Veículo

    public Caminhao() {
        super(); // Chama o construtor da classe pai (Veiculo) sem parâmetros
    }

    // Construtor que inicializa todos os atributos específicos de um Caminhao, passando para o construtor da classe pai os parâmetros necessários
    public Caminhao(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel,
                    String codigoFipe, String mesReferencia, String acronCombustivel) {
        super(veiculoTipo, preco, marcaVeiculo, modelo, anoModelo, combustivel, codigoFipe, mesReferencia, acronCombustivel);
        // Chama o construtor da classe pai (Veiculo), passando os parâmetros necessários para inicializar o veículo
    }
}
