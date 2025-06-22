package entities;

import jakarta.persistence.*;

/**
 * Entidade que representa uma moto no sistema.
 * Herda de Veiculo com discriminador "2".
 */
@Entity
@DiscriminatorValue("2")
public class Moto extends Veiculo {

    public Moto() {
        super();
    }

    public Moto(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel,
                 String codigoFipe, String mesReferencia, String acronCombustivel) {
        super(veiculoTipo, preco, marcaVeiculo, modelo, anoModelo, combustivel, codigoFipe, mesReferencia, acronCombustivel);
    }
}