package main; // Define o pacote principal da aplicação

// Importa o cliente da API da FIPE e a classe responsável pelo cadastro e operações do sistema
import api.FipeApiClient;
import controller.Cadastro;

// Importações do Hibernate para configurar conexão com o banco de dados
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// Importações para leitura de dados e redirecionamento de logs
import java.util.Scanner;
import java.util.logging.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Main {

    public static void main(String[] args) {
        // Redireciona logs do java.util.logging para SLF4J (compatível com Hibernate)
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        // Testa conexão com o banco de dados usando Hibernate
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!\n");
            session.close();
            sessionFactory.close();
        } catch (Exception e) {
            // Se houver erro ao conectar, exibe e encerra o programa
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Instancia os principais objetos usados no programa
        FipeApiClient fipe = new FipeApiClient(); // Cliente da API FIPE
        Cadastro cadastros = new Cadastro();      // Lógica de cadastros e vendas
        Scanner scanner = new Scanner(System.in); // Entrada de dados do usuário

        int opMenu; // Controle da opção do menu principal

        // Loop principal de execução do sistema
        do {
            // Exibe menu principal
            System.out.println("--------------------------------------------------------");
            System.out.println("Escolha alguma das seguintes opções:");
            System.out.println("1-Vendas");
            System.out.println("2-Itens cadastrados");
            System.out.println("3-Cadastro");
            System.out.println("4-Sair");
            System.out.println("--------------------------------------------------------");

            opMenu = scanner.nextInt();

            switch (opMenu) {
                case 1:
                    // Submenu de vendas
                    System.out.println("1-Nova venda");
                    System.out.println("2-Vendas realizadas");
                    int opVendas = scanner.nextInt();
                    scanner.nextLine(); // Limpa o buffer

                    if(opVendas == 1){
                        cadastros.adicionarVenda(); // Inicia novo processo de venda
                    } else if(opVendas == 2){
                        cadastros.mostrarVendas(); // Lista todas as vendas
                        System.out.println("Procurar vendas de uma data especifica?");
                        System.out.println("1-Sim");
                        System.out.println("2-Nao");
                        int opData = scanner.nextInt();
                        scanner.nextLine();

                        if(opData == 1){
                            cadastros.vendaPorData(); // Filtra vendas por data
                        } else {
                            System.out.println("voltando ao menu...");
                        }
                    } else {
                        System.out.println("Opcao invalida!");
                    }
                    break;

                case 2:
                    // Submenu de visualização de itens cadastrados
                    System.out.println("1-Clientes");
                    System.out.println("2-Veiculos");
                    int opItens = scanner.nextInt();
                    scanner.nextLine();

                    if(opItens == 1){
                        cadastros.mostrarClientes(); // Lista todos os clientes
                        System.out.println("procurar por compras realizadas por algum cliente?");
                        System.out.println("1-Sim/2-Nao");
                        int opClientes = scanner.nextInt();
                        scanner.nextLine();

                        if(opClientes == 1){
                            cadastros.mostrarVendasPorCliente(); // Mostra vendas por cliente
                        } else {
                            System.out.println("Voltando ao menu...");
                        }

                    } else if(opItens == 2){
                        cadastros.mostrarVeiculos(); // Lista os veículos cadastrados
                        System.out.println("--------------------------------------------------------");
                        System.out.println("1-Procurar por nome (parcial ou completo)");
                        System.out.println("2-Atualizar preco de algum dos veiculos");
                        System.out.println("3-Procurar cliente que compraram veiculos");
                        System.out.println("4-Voltar ao menu");

                        int opVeiculos = scanner.nextInt();
                        scanner.nextLine();

                        if(opVeiculos == 1){
                            cadastros.pesquisaParcial(); // Pesquisa veículos por nome
                        } else if(opVeiculos == 2){
                            cadastros.atualizarPreco(); // Atualiza preço do veículo
                        } else if(opVeiculos == 3){
                            cadastros.mostrarClientesPorVeiculo(); // Clientes por veículo
                        } else if (opVeiculos == 4) {
                            System.out.println("voltando ao menu...");
                        } else {
                            System.out.println("Opção inválida!");
                        }
                    } else {
                        System.out.println("Opção inválida!");
                    }
                    break;

                case 3:
                    // Chama metodo que lida com o cadastro de veículos e clientes
                    cadastro(scanner, fipe, cadastros);
                    break;

                case 4:
                    // Encerra a execução do sistema
                    System.out.println("Saindo...");
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

        } while (opMenu != 4); // Repetição até o usuário escolher sair

        scanner.close(); // Fecha o scanner no final do programa
    }

    // Metodo responsável pelas ações de cadastro de clientes e veículos
    public static void cadastro(Scanner scanner, FipeApiClient fipe, Cadastro cadastros) {
        int typeV, IdMarca;
        String Combustivel, Ano, AnoECombs, tipoVeiculo;

        System.out.println("Você entrou na função de cadastros");
        System.out.println("1-Cadastrar veículos no catálogo de vendas");
        System.out.println("2-Remover veículos do catálogo de vendas");
        System.out.println("3-Cadastrar cliente");
        System.out.println("4-Remover cliente");
        System.out.println("5-Voltar ao menu");
        int opCadastro = scanner.nextInt();
        scanner.nextLine(); // Limpa buffer

        switch (opCadastro) {
            case 1:
                // Cadastro de veículo novo com dados da FIPE
                System.out.println("Qual tipo de veículo pretende cadastrar?");
                System.out.println("1-Carros");
                System.out.println("2-Motos");
                System.out.println("3-Caminhões");
                typeV = scanner.nextInt();

                // Define o tipo de veículo de acordo com a escolha
                tipoVeiculo = switch (typeV) {
                    case 1 -> "cars";
                    case 2 -> "motorcycles";
                    case 3 -> "trucks";
                    default -> {
                        System.out.println("Tipo de veículo inválido!");
                        yield null;
                    }
                };

                if (tipoVeiculo == null) {
                    System.out.println("Voltando ao menu");
                    return;
                }

                // Exibe marcas disponíveis para o tipo escolhido
                System.out.println("Buscando marcas para " + tipoVeiculo + "...");
                fipe.mostrarMarcas(tipoVeiculo);

                System.out.println("Digite o Id da marca desejada:");
                IdMarca = scanner.nextInt();
                scanner.nextLine();

                System.out.println("Digite o ano desejado:");
                Ano = scanner.nextLine();

                System.out.println("Digite o tipo de combustível:");
                System.out.println("'1'Para gasolina/'2'Para álcool/'3'Para diesel");
                Combustivel = scanner.nextLine();

                // Formata ano e combustível para consulta na FIPE
                AnoECombs = Ano + "-" + Combustivel;

                // Mostra os modelos disponíveis com base na marca e ano
                fipe.modelosPorMarca(tipoVeiculo, IdMarca, AnoECombs);

                System.out.println("Digite o código do modelo desejado:");
                int IdModelo = scanner.nextInt();
                scanner.nextLine();

                // Mostra os detalhes do veículo escolhido
                fipe.detalhesVeiculo(tipoVeiculo, IdMarca, IdModelo, AnoECombs);
                break;

            case 2:
                cadastros.removerVeiculo(); // Remove veículo cadastrado
                break;

            case 3:
                // Cadastro de cliente
                System.out.println("Cadastro de clientes:");
                System.out.println("Digite o nome do cliente:");
                String nomeCliente = scanner.nextLine();

                System.out.println("Digite o cpf do cliente:");
                String cpf = scanner.nextLine();

                System.out.println("Digite o dia em que o cliente nasceu:");
                String DDD = scanner.nextLine();
                System.out.println("Digite o mes em que o cliente nasceu:");
                String MMM = scanner.nextLine();
                System.out.println("Digite o ano em que o cliente nasceu:");
                String AAA = scanner.nextLine();

                String dataNascimento = DDD + "/" + MMM + "/" + AAA;

                // Adiciona cliente no sistema
                cadastros.adicionarCliente(cpf, dataNascimento, nomeCliente);
                break;

            case 4:
                System.out.println("Remoção de cliente:");
                cadastros.deletarCliente(); // Remove cliente do sistema
                break;

            case 5:
                // Retorna ao menu principal
                System.out.println("Voltando ao menu principal...");
                return;

            default:
                System.out.println("Opção inválida! Tente novamente.");
                break;
        }
    }
}
