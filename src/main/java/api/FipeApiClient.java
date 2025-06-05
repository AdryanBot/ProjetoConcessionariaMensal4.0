package api;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import controller.Cadastro;

public class FipeApiClient {
    Cadastro cadastros = new Cadastro();
    Scanner scanner = new Scanner(System.in);

    // Função para mostrar as marcas de um tipo de veículo
    public void mostrarMarcas(String tipoVeiculo) {
        String urlMarcas = "https://parallelum.com.br/api/v2/" + tipoVeiculo + "/brands";
        try {
            System.out.println("Tentando acessar a URL: " + urlMarcas);  // Log para depuração
            String resposta = fazerRequisicao(urlMarcas);
    
            // Remover os colchetes externos e processar o conteúdo
            resposta = resposta.replace("[", "").replace("]", "");
    
            // Separar as marcas usando "},{" como delimitador
            String[] marcasSeparadas = resposta.split("},\\{");
    
            System.out.println("\nMarcas disponíveis:\n");
    
            for (String marca : marcasSeparadas) {
                // Remover chaves extras e dividir "code" e "name"
                marca = marca.replace("{", "").replace("}", "").replace("\"", "");
                String[] atributos = marca.split(",");
    
                String codigo = "";
                String nome = "";
    
                for (String atributo : atributos) {
                    if (atributo.startsWith("code:")) {
                        codigo = atributo.split(":")[1];
                    } else if (atributo.startsWith("name:")) {
                        nome = atributo.split(":")[1];
                    }
                }
    
                // Exibir as marcas
                System.out.println(" - Código: " + codigo + " | Marca: " + nome);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar marcas: " + e.getMessage());
        }
    }

    // Função para mostrar os modelos de um veículo, com base no tipo, marca, ano e tipo de combustível
    public void modelosPorMarca(String tipoVeiculo, int IdMarca, String AnoECombs) {
        try {
            String urlModelos = "https://parallelum.com.br/api/v2/" + tipoVeiculo + "/brands/" + IdMarca + "/years/"+ AnoECombs+"/models";
    
            System.out.println("Tentando acessar a URL: " + urlModelos); // Depuração
            String respostaModelos = fazerRequisicao(urlModelos);
    
            // Removendo os colchetes externos da resposta JSON
            respostaModelos = respostaModelos.replace("[", "").replace("]", "");
    
            // Separando cada modelo individualmente usando "},{" como delimitador
            String[] modelosSeparados = respostaModelos.split("},\\{");
    
            System.out.println("\nModelos disponíveis para o tipo " + tipoVeiculo + ", marca " + IdMarca + ", ano e combustível " + AnoECombs + ":\n");
    
            for (String modelo : modelosSeparados) {
                // Removendo chaves extras e dividindo "code" e "name"
                modelo = modelo.replace("{", "").replace("}", "").replace("\"", "");
                String[] atributos = modelo.split(",");
    
                String codigo = "";
                String nome = "";
    
                for (String atributo : atributos) {
                    if (atributo.startsWith("code:")) {
                        codigo = atributo.split(":")[1];
                    } else if (atributo.startsWith("name:")) {
                        nome = atributo.split(":")[1];
                    }
                }
    
                System.out.println(" - Código: " + codigo + " | Modelo: " + nome);
            }
    
        } catch (Exception e) {
            System.out.println("Erro ao buscar modelos: " + e.getMessage());
        }
    }

    // Função para buscar detalhes completos de um veículo
    public void detalhesVeiculo(String tipoVeiculo, int IdMarca, int IdModelo, String AnoECombs) {
        try {
            String urlDetalhes = "https://parallelum.com.br/api/v2/" + tipoVeiculo + "/brands/" + IdMarca + "/models/" + IdModelo + "/years/" + AnoECombs;
            System.out.println("\n Acessando URL: " + urlDetalhes);
    
            String resposta = fazerRequisicao(urlDetalhes);
    
            // Removendo as chaves externas da resposta JSON
            resposta = resposta.replace("{", "").replace("}", "").replace("\"", "");
    
            // Separando os atributos usando vírgula como delimitador
            String[] atributos = resposta.split(",");
    
            // Variáveis para armazenar os valores extraídos
            String marca = "", modelo = "", ano = "", codigoFipe = "", preco = "";
            String combustivel = "", acronCombustivel = "", mesReferencia = "";
    
            // Extraindo cada informação
            for (String atributo : atributos) {
                if (atributo.startsWith("brand:")) {
                    marca = atributo.split(":")[1];
                } else if (atributo.startsWith("model:")) {
                    modelo = atributo.split(":")[1];
                } else if (atributo.startsWith("modelYear:")) {
                    ano = atributo.split(":")[1];
                } else if (atributo.startsWith("codeFipe:")) {
                    codigoFipe = atributo.split(":")[1];
                } else if (atributo.startsWith("price:")) {
                    preco = atributo.split(":")[1];
                } else if (atributo.startsWith("fuel:")) {
                    combustivel = atributo.split(":")[1];
                } else if (atributo.startsWith("fuelAcronym:")) {
                    acronCombustivel = atributo.split(":")[1];
                } else if (atributo.startsWith("referenceMonth:")) {
                    mesReferencia = atributo.split(":")[1];
                }
            }
    
            // Exibir os detalhes formatados
            System.out.println("\n**Detalhes do Veículo:**\n");
            System.out.println(" Marca: " + marca);
            System.out.println(" Modelo: " + modelo);
            System.out.println(" Ano: " + ano);
            System.out.println(" Código FIPE: " + codigoFipe);
            System.out.println(" Preço: " + preco);
            System.out.println(" Combustível: " + combustivel + " (" + acronCombustivel + ")");
            System.out.println(" Mês de Referência: " + mesReferencia);
            System.out.println("\n");

            System.out.println("Deseja cadastrar o veiculo?");
            System.out.println("1-Sim/2-Não");
            int op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
                case 1:
                    cadastros.adicionarVeiculo( tipoVeiculo ,marca, modelo, ano, codigoFipe, preco, combustivel, acronCombustivel, mesReferencia);
                    break;
            
                default:
                    break;
            }
    
        } catch (Exception e) {
            System.out.println(" Erro ao buscar detalhes do veículo: " + e.getMessage());
        }
    }

    // Metodo para fazer requisições HTTP
    public String fazerRequisicao(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");
    
            int codigoResposta = conexao.getResponseCode();
            if (codigoResposta != 200) {
                System.out.println("Erro na requisição! Código: " + codigoResposta);
                return null;
            }
    
            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;
            
            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            
            leitor.close();
            return resposta.toString();
            
        } catch (Exception e) {
            System.out.println("Erro ao fazer requisição: " + e.getMessage());
            return null;
        }
    }
}
