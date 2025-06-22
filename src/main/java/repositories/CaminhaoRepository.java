package repositories;

import entities.Caminhao;
import jakarta.persistence.EntityManager;
import utils.JPAUtil;

public class CaminhaoRepository {
    
    public void salvar(Caminhao caminhao) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(caminhao);
        em.getTransaction().commit();
        em.close();
    }
}