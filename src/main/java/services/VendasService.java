package services;

import entities.Cliente;
import entities.Vendas;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import entities.Veiculo;
import repositories.VendasRepository;
import utils.JPAUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class VendasService {

    // Instância do repositório de vendas
    private static VendasRepository vendaRepo = new VendasRepository();

    // Metodo para listar todas as vendas
    public static List<Vendas> listarTodas() {
        // Chama o metodo findAll do repositório para obter todas as vendas
        return vendaRepo.findAll();
    }

    // Metodo para buscar vendas em um período específico
    public List<Vendas> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        // Chama o metodo buscarPorData do repositório para buscar vendas dentro do intervalo
        return vendaRepo.buscarPorData(inicio, fim);
    }

    // Metodo para mostrar as vendas de um cliente específico
    public void mostrarVendasPorCliente(Long idCliente) {
        // Chama o repositório para buscar todas as vendas associadas a um cliente
        List<Vendas> vendas = vendaRepo.buscarVendasComVeiculoPorCliente(idCliente);

        // Verifica se há vendas e exibe os detalhes
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda encontrada para o cliente com ID: " + idCliente);
        } else {
            for (Vendas v : vendas) {
                System.out.println("--------------------------------------------------------");
                System.out.println("ID da Venda: " + v.getId());
                System.out.println("Modelo do Veículo: " + v.getVeiculo().getModelo());
                System.out.println("Preço do Veículo: " + v.getVeiculo().getPreco());
                System.out.println("Data da Venda: " + v.getDataVenda());
            }
        }
    }

    // Metodo para pedir o ID do cliente e exibir suas vendas
    public void pedirIdCliente() {
        Scanner scanner = new Scanner(System.in);

        // Solicita o ID do cliente
        System.out.print("Digite o ID do cliente para visualizar suas vendas: ");
        Long idCliente = scanner.nextLong();
        scanner.nextLine();  // Limpa o buffer de linha

        // Chama o metodo para exibir as vendas desse cliente
        mostrarVendasPorCliente(idCliente);
    }

    // Metodo para mostrar os clientes que compraram um determinado veículo
    public void mostrarClientesPorVeiculo(Long idVeiculo) {
        // Chama o repositório para buscar as vendas relacionadas ao veículo
        List<Vendas> vendas = vendaRepo.buscarVendasPorVeiculo(idVeiculo);

        // Verifica se há vendas e exibe os detalhes
        if (vendas.isEmpty()) {
            System.out.println("Nenhum cliente comprou o veículo com ID: " + idVeiculo);
        } else {
            for (Vendas v : vendas) {
                System.out.println("--------------------------------------------------------");
                System.out.println("ID da Venda: " + v.getId());
                System.out.println("Nome do Cliente: " + v.getCliente().getNome());
                System.out.println("CPF do Cliente: " + v.getCliente().getCpf());
                System.out.println("Data da Venda: " + v.getDataVenda());
            }
        }
    }

    // Metodo para pedir o ID do veículo e exibir os clientes que compraram ele
    public void pedirIdVeiculo() {
        Scanner scanner = new Scanner(System.in);
        
        // Solicita o ID do veículo
        System.out.print("Digite o ID do veículo para ver quem comprou: ");
        Long idVeiculo = scanner.nextLong();
        scanner.nextLine(); // Limpa o buffer

        // Chama o metodo para exibir os clientes que compraram o veículo
        mostrarClientesPorVeiculo(idVeiculo);
    }

    // Metodo para mostrar o total de vendas realizadas
    public void mostrarQuantidadeDeVendas() {
        // Chama o metodo contarVendas do repositório para contar o total de vendas
        Long total = vendaRepo.contarVendas();
        System.out.println("Total de vendas: " + total);
    }

}
