package dao.dao_impl;

import dao.DaoInterfaceSquadra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Squadra;


public class DaoInterfaceSquadraImpl implements DaoInterfaceSquadra {
    private final EntityManager entityManager;
    
    public DaoInterfaceSquadraImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Squadra save(Squadra squadra) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Squadra salvata = this.entityManager.merge(squadra);
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
    public List<Squadra> findAll() {
        String jpql = "SELECT s FROM Squadra s";
        TypedQuery<Squadra> query = this.entityManager.createQuery(jpql, Squadra.class);
        return query.getResultList();
    }

    @Override
    public Squadra update(Squadra squadra) {
        return this.save(squadra); 
    }

    @Override
    public boolean delete(int idSquadra) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Squadra squadra = entityManager.find(Squadra.class, idSquadra);
            if (squadra != null) {
                entityManager.remove(squadra);
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
