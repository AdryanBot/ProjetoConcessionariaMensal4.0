package repositories;

import entities.Cliente; // Importa a classe Cliente da camada de entidades
import jakarta.persistence.*; // Importa as classes de JPA para trabalhar com persistência
import utils.JPAUtil; // Utilitário para obter o EntityManager

import java.util.List; // Importa a classe List para trabalhar com coleções de objetos

public class ClienteRepository {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("meuPU"); // Cria a fábrica de EntityManager com a unidade de persistência "meuPU"

    // Metodo para salvar um novo cliente
    public void salvar(Cliente cliente) {
        EntityManager em = emf.createEntityManager(); // Cria uma instância do EntityManager
        em.getTransaction().begin(); // Inicia a transação
        em.persist(cliente); // Persiste o cliente no banco de dados
        em.getTransaction().commit(); // Confirma a transação
        em.close(); // Fecha o EntityManager após a operação
    }

    // Metodo para contar o número total de clientes
    public Long contarCliente() {
        EntityManager em = JPAUtil.getEntityManager(); // Obtém uma instância do EntityManager utilizando JPAUtil
        Long count = em.createQuery("SELECT COUNT(c) FROM Cliente c", Long.class).getSingleResult(); // Executa uma consulta para contar o número de clientes
        em.close(); // Fecha o EntityManager após a consulta
        return count; // Retorna o número total de clientes
    }
}
