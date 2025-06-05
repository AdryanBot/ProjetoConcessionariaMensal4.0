package services;

import entities.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repositories.ClienteRepository;
import utils.JPAUtil;

import java.util.List;

public class ClienteService {

    // Instância do repositório ClienteRepository para interação com o banco de dados
    public static ClienteRepository clienteRepo = new ClienteRepository();

    // Metodo para listar todos os clientes do banco de dados
    public List<Cliente> listarTodos() {
        // Obtém o EntityManager para realizar operações no banco de dados
        EntityManager em = JPAUtil.getEntityManager();
        
        // Cria uma consulta JPQL para selecionar todos os clientes
        TypedQuery<Cliente> query = em.createQuery("FROM Cliente", Cliente.class);
        
        // Executa a consulta e retorna a lista de clientes
        List<Cliente> cliente = query.getResultList();
        
        // Fecha o EntityManager após a operação
        em.close();
        
        // Retorna a lista de clientes
        return cliente;
    }

    // Metodo para mostrar a quantidade total de clientes cadastrados
    public void mostrarQtdCliente() {
        // Chama o metodo para contar o número total de clientes no banco
        Long total = clienteRepo.contarCliente();
        
        // Exibe o total de clientes na saída padrão (console)
        System.out.println("Total de clientes cadastrados: " + total);
    }
}
