package entities;

public class FipeMarca {
    private String codigo;
    private String nome;

    public FipeMarca(String codigo, String nome) {
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
