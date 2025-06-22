package repositories;

import entities.Veiculo;
import jakarta.persistence.EntityManager;
import utils.JPAUtil;
import java.util.List;

public class VeiculoRepository {
    
    public void salvar(Veiculo veiculo) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(veiculo);
        em.getTransaction().commit();
        em.close();
    }
    
    public List<Veiculo> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Veiculo> veiculos = em.createQuery("SELECT v FROM Veiculo v", Veiculo.class).getResultList();
        em.close();
        return veiculos;
    }
    
    public Veiculo findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Veiculo veiculo = em.find(Veiculo.class, id);
        em.close();
        return veiculo;
    }
    
    public List<Veiculo> buscarPorModeloParcial(String termo) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Veiculo> veiculos = em.createQuery(
            "SELECT v FROM Veiculo v WHERE LOWER(v.modelo) LIKE LOWER(:termo)", Veiculo.class)
            .setParameter("termo", "%" + termo + "%")
            .getResultList();
        em.close();
        return veiculos;
    }
    
    public Long contarVeiculos() {
        EntityManager em = JPAUtil.getEntityManager();
        Long count = em.createQuery("SELECT COUNT(v) FROM Veiculo v", Long.class).getSingleResult();
        em.close();
        return count;
    }
}