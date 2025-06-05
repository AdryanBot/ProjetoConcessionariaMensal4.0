package services;

import entities.Veiculo;
import repositories.VeiculoRepository;

import java.util.List;

public class VeiculoService {

    // Instância do repositório VeiculoRepository para interação com o banco de dados
    private final VeiculoRepository veiculoRepository;
    
    // Instância estática para reutilização em outras partes do código
    public static VeiculoRepository veiculoRepo = new VeiculoRepository();

    // Construtor que inicializa o repositório VeiculoRepository
    public VeiculoService() {
        this.veiculoRepository = new VeiculoRepository();
    }

    // Metodo para listar todos os veículos cadastrados no banco de dados
    public List<Veiculo> listarTodos() {
        // Chama o metodo do repositório para obter todos os veículos
        return veiculoRepository.findAll();
    }

    // Metodo para buscar veículos pelo modelo (parcial)
    public List<Veiculo> buscarPorModelo(String termo) {
        // Chama o metodo do repositório para buscar veículos cujo modelo corresponde ao termo
        return veiculoRepository.buscarPorModeloParcial(termo);
    }

    // Metodo para buscar um veículo pelo seu ID
    public Veiculo buscarPorId(Long id) {
        // Chama o metodo do repositório para buscar um veículo pelo ID
        return veiculoRepository.findById(id);
    }

    // Metodo para mostrar a quantidade total de veículos cadastrados
    public void mostrarQtdVeiculos() {
        // Chama o metodo do repositório para contar o número total de veículos cadastrados
        Long total = veiculoRepo.contarVeiculos();
        
        // Exibe o total de veículos na saída padrão (console)
        System.out.println("Total de veículos cadastrados: " + total);
    }
}
