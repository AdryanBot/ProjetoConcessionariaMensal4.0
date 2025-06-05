package utils;

// Importações necessárias para trabalhar com JPA (Jakarta Persistence API)
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    // Cria uma única instância do EntityManagerFactory, usando o nome da unidade de persistência definida no persistence.xml
    public static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("meuPU");

    // Metodo utilitário para obter uma nova instância de EntityManager
    // Esse EntityManager será usado para realizar operações com o banco de dados
    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
