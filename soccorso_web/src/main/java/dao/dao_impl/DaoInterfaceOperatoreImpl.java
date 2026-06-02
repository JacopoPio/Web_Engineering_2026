package dao.dao_impl;

import dao.DaoInterfaceOperatore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Operatore;


public class DaoInterfaceOperatoreImpl implements DaoInterfaceOperatore {
    private final EntityManager entityManager;
    
    public DaoInterfaceOperatoreImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Operatore save(Operatore operatore) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Operatore salvato = this.entityManager.merge(operatore);
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
    public List<Operatore> findAll() {
        String jpql = "SELECT o FROM Operatore o";
        TypedQuery<Operatore> query = this.entityManager.createQuery(jpql, Operatore.class);
        return query.getResultList();
    }

    @Override
    public Operatore update(Operatore operatore) {
        return this.save(operatore); 
    }

    @Override
    public boolean delete(String emailOp) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Operatore operatore = entityManager.find(Operatore.class, emailOp);
            if (operatore != null) {
                entityManager.remove(operatore);
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
