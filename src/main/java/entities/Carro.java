package entities;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("1")
public class Carro extends Veiculo {

    public Carro() {
        super();
    }

    public Carro(int veiculoTipo, String preco, String marcaVeiculo, String modelo, String anoModelo, String combustivel,
                 String codigoFipe, String mesReferencia, String acronCombustivel) {
        super(veiculoTipo, preco, marcaVeiculo, modelo, anoModelo, combustivel, codigoFipe, mesReferencia, acronCombustivel);
    }
}