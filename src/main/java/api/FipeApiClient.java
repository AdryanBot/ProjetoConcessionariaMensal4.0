package api;

import entities.FipeMarca;
import entities.FipeModelo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
            var jsonObj = gson.fromJson(resposta, com.google.gson.JsonObject.class);
            var modelosJson = jsonObj.getAsJsonArray("modelos");
            return gson.fromJson(modelosJson, new TypeToken<ArrayList<FipeModelo>>(){}.getType());
        } catch (Exception e) {
            System.err.println("Erro buscar modelos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private String requisicaoGET(String urlStr) throws Exception {
        System.out.println("URL para requisição: " + urlStr);
        URL url = new URL(urlStr);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        int status = conexao.getResponseCode();
        System.out.println("Status da requisição: " + status);
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
