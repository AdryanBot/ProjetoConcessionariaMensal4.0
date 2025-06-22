package services;

import entities.Veiculo;
import repositories.VeiculoRepository;
import java.util.List;

/**
 * Serviço para operações relacionadas a veículos.
 */
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoService() {
        this.veiculoRepository = new VeiculoRepository();
    }

    /**
     * Lista todos os veículos cadastrados.
     * @return Lista de veículos
     */
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    /**
     * Busca veículos por modelo parcial.
     * @param termo Termo de busca
     * @return Lista de veículos encontrados
     */
    public List<Veiculo> buscarPorModelo(String termo) {
        return veiculoRepository.buscarPorModeloParcial(termo);
    }

    /**
     * Busca veículo por ID.
     * @param id ID do veículo
     * @return Veículo encontrado ou null
     */
    public Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id);
    }

    public Long contarVeiculos() {
        return veiculoRepository.contarVeiculos();
    }
}
