package dao.dao_impl;

import dao.DaoInterfaceRichiesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Richiesta;


public class DaoInterfaceRichiestaImpl implements DaoInterfaceRichiesta {
    private final EntityManager entityManager;
    
    public DaoInterfaceRichiestaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Richiesta save(Richiesta richiesta) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Richiesta salvata = this.entityManager.merge(richiesta);
            tx.commit();
            return salvata;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Richiesta> findAll() {
        String jpql = "SELECT r FROM Richiesta r";
        TypedQuery<Richiesta> query = this.entityManager.createQuery(jpql, Richiesta.class);
        return query.getResultList();
    }

    @Override
    public Richiesta update(Richiesta richiesta) {
        return this.save(richiesta); 
    }

    @Override
    public boolean delete(String email_segnalante) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Richiesta richiesta = entityManager.find(Richiesta.class, email_segnalante);
            if (richiesta != null) {
                entityManager.remove(richiesta);
                tx.commit();
                return true;
            }
            tx.commit();
            return false;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
    
}
