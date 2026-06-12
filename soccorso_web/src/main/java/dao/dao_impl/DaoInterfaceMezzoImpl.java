package dao.dao_impl;

import dao.DaoInterfaceMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Mezzo;

public class DaoInterfaceMezzoImpl implements DaoInterfaceMezzo {
    private final EntityManager entityManager;

    public DaoInterfaceMezzoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Mezzo save(Mezzo mezzo) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Mezzo salvato = this.entityManager.merge(mezzo);
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
    public List<Mezzo> findAll() {
        String jpql = "SELECT m FROM Mezzo m";
        TypedQuery<Mezzo> query = this.entityManager.createQuery(jpql, Mezzo.class);
        return query.getResultList();
    }

    @Override
    public Mezzo update(Mezzo mezzo) {
        return this.save(mezzo); 
    }

    @Override
    public boolean delete(String targaMezzo) {
        EntityTransaction tx = this.entityManager.getTransaction();
        try {
            tx.begin();
            Mezzo mezzo = entityManager.find(Mezzo.class, targaMezzo);
            if (mezzo != null) {
                entityManager.remove(mezzo);
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
    @Override
    public Mezzo findByTarga(String targa) {
        if (targa == null || targa.isBlank()) {
            return null;
        }

        this.entityManager.clear();

        return this.entityManager.find(Mezzo.class, targa.trim().toUpperCase());
    }
}

