package repositories;

import entities.Carro;
import jakarta.persistence.EntityManager;
import utils.JPAUtil;

public class CarroRepository {
    
    public void salvar(Carro carro) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(carro);
        em.getTransaction().commit();
        em.close();
    }
}