package services;

import entities.Cliente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import repositories.ClienteRepository;
import utils.JPAUtil;

import java.util.List;

public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService() {
        this.clienteRepository = new ClienteRepository();
    }

    public List<Cliente> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        TypedQuery<Cliente> query = em.createQuery("FROM Cliente", Cliente.class);
        List<Cliente> clientes = query.getResultList();
        em.close();
        return clientes;
    }

    public Long contarClientes() {
        return clienteRepository.contarCliente();
    }
}
