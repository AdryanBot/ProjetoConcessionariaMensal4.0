package entities;

import jakarta.persistence.*;

/**
 * Entidade que representa um caminhão no sistema.
 * Herda de Veiculo com discriminador "3".
 */
@Entity
@DiscriminatorValue("3")
public class Caminhao extends Veiculo {

    public Caminhao() {
        super();
    }

    public Caminhao(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel,
                    String codigoFipe, String mesReferencia, String acronCombustivel) {
        super(veiculoTipo, preco, marcaVeiculo, modelo, anoModelo, combustivel, codigoFipe, mesReferencia, acronCombustivel);
    }
}