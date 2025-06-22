package repositories;

import entities.Cliente;
import jakarta.persistence.*;
import utils.JPAUtil;

public class ClienteRepository {

    public void salvar(Cliente cliente) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
    }

    public Long contarCliente() {
        EntityManager em = JPAUtil.getEntityManager();
        Long count = em.createQuery("SELECT COUNT(c) FROM Cliente c", Long.class).getSingleResult();
        em.close();
        return count;
    }
}
