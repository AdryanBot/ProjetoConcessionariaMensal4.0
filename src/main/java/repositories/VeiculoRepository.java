package repositories;

import entities.Veiculo; // Importa a classe Veiculo da camada de entidades
import jakarta.persistence.EntityManager; // Importa a classe EntityManager para interagir com o banco de dados
import jakarta.persistence.TypedQuery; // Importa a classe TypedQuery para criar consultas tipadas com JPA
import utils.JPAUtil; // Utilitário para obter o EntityManager

import java.util.List; // Importa a classe List para trabalhar com coleções de objetos

public class VeiculoRepository {

    // Metodo para buscar todos os veículos do banco de dados
    public List<Veiculo> findAll() {
        // Obtém uma instância do EntityManager usando JPAUtil
        EntityManager em = JPAUtil.getEntityManager();
        // Cria uma consulta JPQL para buscar todos os veículos
        TypedQuery<Veiculo> query = em.createQuery("FROM Veiculo", Veiculo.class);
        // Executa a consulta e obtém os resultados
        List<Veiculo> veiculos = query.getResultList();
        em.close(); // Fecha o EntityManager após a consulta
        return veiculos; // Retorna a lista de veículos encontrados
    }

    // Metodo para buscar um veículo por seu ID
    public Veiculo findById(Long id) {
        // Obtém uma instância do EntityManager usando JPAUtil
        EntityManager em = JPAUtil.getEntityManager();
        // Busca o veículo diretamente pelo ID usando o metodo find
        Veiculo veiculo = em.find(Veiculo.class, id);
        em.close(); // Fecha o EntityManager após a consulta
        return veiculo; // Retorna o veículo encontrado, ou null se não encontrado
    }

    // Metodo para buscar veículos cujo modelo contenha um termo parcial (case-insensitive)
    public List<Veiculo> buscarPorModeloParcial(String termo) {
        // Obtém uma instância do EntityManager usando JPAUtil
        EntityManager em = JPAUtil.getEntityManager();
        // Cria uma consulta JPQL para buscar veículos com modelo que contenha o termo informado
        TypedQuery<Veiculo> query = em.createQuery("SELECT v FROM Veiculo v WHERE LOWER(v.modelo) LIKE LOWER(:termo)", Veiculo.class);
        query.setParameter("termo", "%" + termo + "%"); // Define o parâmetro :termo com um valor de pesquisa parcial
        // Executa a consulta e obtém os resultados
        List<Veiculo> veiculos = query.getResultList();
        em.close(); // Fecha o EntityManager após a consulta
        return veiculos; // Retorna a lista de veículos encontrados com o modelo correspondente ao termo
    }

    // Metodo para contar o número total de veículos no banco de dados
    public Long contarVeiculos() {
        // Obtém uma instância do EntityManager usando JPAUtil
        EntityManager em = JPAUtil.getEntityManager();
        // Cria uma consulta JPQL para contar o número total de veículos
        Long count = em.createQuery("SELECT COUNT(v) FROM Veiculo v", Long.class).getSingleResult();
        em.close(); // Fecha o EntityManager após a consulta
        return count; // Retorna o número total de veículos
    }
}
