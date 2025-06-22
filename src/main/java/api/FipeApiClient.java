package api;

import entities.FipeMarca;
import entities.FipeModelo;
import entities.FipeVeiculo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Cliente para integração com a API FIPE.
 * Responsável por buscar dados de veículos, marcas e modelos.
 */
public class FipeApiClient {

    private final Gson gson = new Gson();

    private String tipoParaString(int tipo) {
        switch (tipo) {
            case 1: return "carros";
            case 2: return "motos";
            case 3: return "caminhoes";
            default: throw new IllegalArgumentException("Tipo inválido: " + tipo);
        }
    }

    /**
     * Busca todas as marcas disponíveis para um tipo de veículo.
     * @param tipo Tipo do veículo (1=Carro, 2=Moto, 3=Caminhão)
     * @return Lista de marcas
     */
    public ArrayList<FipeMarca> buscarMarcas(int tipo) {
        String tipoStr = tipoParaString(tipo);
        String urlMarcas = "https://parallelum.com.br/fipe/api/v1/" + tipoStr + "/marcas";
        try {
            String resposta = requisicaoGET(urlMarcas);
            if (resposta == null || resposta.isEmpty()) {
                System.err.println("Resposta da API é null ou vazia.");
                return new ArrayList<>();
            }
            return gson.fromJson(resposta, new TypeToken<ArrayList<FipeMarca>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erro buscar marcas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca modelos de uma marca específica.
     * @param tipo Tipo do veículo
     * @param codigoMarca Código da marca
     * @param anoCombustivel Ano e combustível (não utilizado)
     * @return Lista de modelos
     */
    public ArrayList<FipeModelo> buscarModelos(int tipo, String codigoMarca, String anoCombustivel) {
        String tipoStr = tipoParaString(tipo);
        String urlModelos = "https://parallelum.com.br/fipe/api/v1/" + tipoStr + "/marcas/" + codigoMarca + "/modelos";
        try {
            String resposta = requisicaoGET(urlModelos);
            if (resposta == null || resposta.isEmpty()) {
                System.err.println("Resposta da API é null ou vazia.");
                return new ArrayList<>();
            }
            // API retorna JSON com {"modelos": [...], "anos": [...]}
            com.google.gson.JsonObject jsonObj = gson.fromJson(resposta, com.google.gson.JsonObject.class);
            com.google.gson.JsonArray modelosJson = jsonObj.getAsJsonArray("modelos");
            return gson.fromJson(modelosJson, new TypeToken<ArrayList<FipeModelo>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erro buscar modelos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public ArrayList<FipeModelo> buscarModelosFiltrados(int tipo, String codigoMarca, String anoDesejado, int combustivelDesejado) {
        ArrayList<FipeModelo> todosModelos = buscarModelos(tipo, codigoMarca, "");
        ArrayList<FipeModelo> modelosFiltrados = new ArrayList<>();
        
        for (FipeModelo modelo : todosModelos) {
            ArrayList<String> anosDisponiveis = buscarAnos(tipo, codigoMarca, modelo.getCodigo());
            
            // Verifica se o modelo tem o ano e combustível desejados
            boolean temAnoECombustivel = false;
            for (String ano : anosDisponiveis) {
                if (ano.contains(anoDesejado) && ano.contains("-" + combustivelDesejado)) {
                    temAnoECombustivel = true;
                    break;
                }
            }
            
            if (temAnoECombustivel) {
                modelosFiltrados.add(modelo);
            }
        }
        
        return modelosFiltrados;
    }

    public ArrayList<String> buscarAnos(int tipo, String codigoMarca, String codigoModelo) {
        String tipoStr = tipoParaString(tipo);
        String urlAnos = "https://parallelum.com.br/fipe/api/v1/" + tipoStr + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos";
        try {
            String resposta = requisicaoGET(urlAnos);
            if (resposta == null || resposta.isEmpty()) {
                return new ArrayList<>();
            }
            ArrayList<com.google.gson.JsonObject> anos = gson.fromJson(resposta, new TypeToken<ArrayList<com.google.gson.JsonObject>>(){}.getType());
            ArrayList<String> codigosAnos = new ArrayList<>();
            for (com.google.gson.JsonObject ano : anos) {
                codigosAnos.add(ano.get("codigo").getAsString());
            }
            return codigosAnos;
        } catch (Exception e) {
            System.err.println("Erro buscar anos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Busca detalhes completos de um veículo específico.
     * @param tipo Tipo do veículo
     * @param codigoMarca Código da marca
     * @param codigoModelo Código do modelo
     * @param anoDesejado Ano desejado
     * @param combustivelDesejado Tipo de combustível
     * @return Detalhes do veículo ou null se não encontrado
     */
    public FipeVeiculo buscarDetalhesVeiculo(int tipo, String codigoMarca, String codigoModelo, String anoDesejado, int combustivelDesejado) {
        ArrayList<String> anosDisponiveis = buscarAnos(tipo, codigoMarca, codigoModelo);
        
        String codigoAno = null;
        for (String ano : anosDisponiveis) {
            if (ano.contains(anoDesejado) && ano.contains("-" + combustivelDesejado)) {
                codigoAno = ano;
                break;
            }
        }
        
        if (codigoAno == null) {
            System.err.println("Ano/combustível não encontrado. Disponíveis: " + anosDisponiveis);
            return null;
        }
        
        String tipoStr = tipoParaString(tipo);
        String urlDetalhes = "https://parallelum.com.br/fipe/api/v1/" + tipoStr + "/marcas/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + codigoAno;
        try {
            String resposta = requisicaoGET(urlDetalhes);
            if (resposta == null || resposta.isEmpty()) {
                return null;
            }
            return gson.fromJson(resposta, FipeVeiculo.class);
        } catch (Exception e) {
            System.err.println("Erro buscar detalhes: " + e.getMessage());
            return null;
        }
    }

    private String requisicaoGET(String urlStr) throws Exception {
        return requisicaoGETComRetry(urlStr, 0);
    }
    
    private String requisicaoGETComRetry(String urlStr, int tentativa) throws Exception {
        System.out.println("URL para requisição: " + urlStr + " (Tentativa: " + (tentativa + 1) + ")");
        
        // Delay progressivo: 2s, 5s, 10s
        if (tentativa > 0) {
            Thread.sleep(2000 + (tentativa * 3000));
        }
        
        URL url = new URL(urlStr);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setConnectTimeout(15000);
        conexao.setReadTimeout(15000);
        conexao.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
        
        int status = conexao.getResponseCode();
        System.out.println("Status da requisição: " + status);
        
        if (status == 429) {
            if (tentativa < 2) {
                System.err.println("Rate limit atingido. Aguardando " + (2 + tentativa * 3) + " segundos...");
                return requisicaoGETComRetry(urlStr, tentativa + 1);
            } else {
                System.err.println("API FIPE indisponível após 3 tentativas. Tente novamente mais tarde.");
                return null;
            }
        }
        
        if (status != 200) {
            System.err.println("Erro na requisição! Código: " + status);
            return null;
        }
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        StringBuilder response = new StringBuilder();
        String linha;
        while ((linha = in.readLine()) != null) {
            response.append(linha);
        }
        in.close();
        return response.toString();
    }
}
