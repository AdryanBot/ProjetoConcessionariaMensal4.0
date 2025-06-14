package entities;

public class FipeModelo {
    private String codigo;
    private String nome;

    public FipeModelo(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }
}

