package dao.dao_impl;

import dao.DaoInterfaceAmministratore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Amministratore;


public class DaoInterfaceAmministratoreImpl implements DaoInterfaceAmministratore {
    private final EntityManager entityManager;
    
    public DaoInterfaceAmministratoreImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Amministratore save(Amministratore amministratore) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Amministratore salvato = this.entityManager.merge(amministratore);
            tx.commit();
            return salvato;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Amministratore> findAll() {
        String jpql = "SELECT am FROM Amministratore am";
        TypedQuery<Amministratore> query = this.entityManager.createQuery(jpql, Amministratore.class);
        return query.getResultList();
    }

    @Override
    public Amministratore update(Amministratore amministratore) {
        return this.save(amministratore); 
    }

    @Override
    public boolean delete(String emailAdmin) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Amministratore amministratore = entityManager.find(Amministratore.class, emailAdmin);
            if (amministratore != null) {
                entityManager.remove(amministratore);
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
