package entities;

public class FipeVeiculo {
    private String Valor;
    private String Marca;
    private String Modelo;
    private String AnoModelo;
    private String Combustivel;
    private String CodigoFipe;
    private String MesReferencia;
    private String TipoVeiculo;
    private String SiglaCombustivel;

    public String getValor() { return Valor; }
    public void setValor(String valor) { this.Valor = valor; }

    public String getMarca() { return Marca; }
    public void setMarca(String marca) { this.Marca = marca; }

    public String getModelo() { return Modelo; }
    public void setModelo(String modelo) { this.Modelo = modelo; }

    public String getAnoModelo() { return AnoModelo; }
    public void setAnoModelo(String anoModelo) { this.AnoModelo = anoModelo; }

    public String getCombustivel() { return Combustivel; }
    public void setCombustivel(String combustivel) { this.Combustivel = combustivel; }

    public String getCodigoFipe() { return CodigoFipe; }
    public void setCodigoFipe(String codigoFipe) { this.CodigoFipe = codigoFipe; }

    public String getMesReferencia() { return MesReferencia; }
    public void setMesReferencia(String mesReferencia) { this.MesReferencia = mesReferencia; }

    public String getTipoVeiculo() { return TipoVeiculo; }
    public void setTipoVeiculo(String tipoVeiculo) { this.TipoVeiculo = tipoVeiculo; }

    public String getSiglaCombustivel() { return SiglaCombustivel; }
    public void setSiglaCombustivel(String siglaCombustivel) { this.SiglaCombustivel = siglaCombustivel; }
}