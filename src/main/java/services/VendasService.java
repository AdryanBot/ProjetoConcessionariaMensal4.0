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
    
    public List<Vendas> buscarVendasPorClientes(String idCliente) {
        return vendaRepo.buscarVendasComVeiculoPorCliente(idCliente);
    }

    // Metodo para pedir o ID do cliente e exibir suas vendas
   

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

    public List<Vendas> buscarClientePorVeiculo(Long idVeiculo) {
        return vendaRepo.buscarVendasPorVeiculo(idVeiculo);
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

    public Long contarVendas() {
        return vendaRepo.contarVendas();
    }

}
