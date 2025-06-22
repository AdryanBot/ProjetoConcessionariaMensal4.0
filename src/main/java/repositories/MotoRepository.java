package repositories;

import entities.Moto;
import jakarta.persistence.EntityManager;
import utils.JPAUtil;

public class MotoRepository {
    
    public void salvar(Moto moto) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(moto);
        em.getTransaction().commit();
        em.close();
    }
}