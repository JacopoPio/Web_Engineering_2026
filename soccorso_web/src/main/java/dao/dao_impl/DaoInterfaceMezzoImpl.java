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
    
  @Override
    public List<Mezzo> findDisponibili() {

        String jpql =
            "SELECT me " +
            "FROM Mezzo me " +
            "WHERE NOT EXISTS (" +
            "    SELECT mi.id " +
            "    FROM Missione mi " +
            "    JOIN mi.mezzi mezzoMissione " +
            "    WHERE mezzoMissione = me " +
            "    AND LOWER(TRIM(mi.richiesta.stato)) = 'in corso'" +
            ") " +
            "ORDER BY me.targa";

        return entityManager
            .createQuery(jpql, Mezzo.class)
            .getResultList();
    }
 @Override
    public boolean isDisponibile(String targa) {

    if (targa == null || targa.isBlank()) {
        return false;
    }

        String jpql =
            "SELECT COUNT(me) " +
            "FROM Mezzo me " +
            "WHERE UPPER(me.targa) = UPPER(:targa) " +
            "AND NOT EXISTS (" +
            "    SELECT mi.id " +
            "    FROM Missione mi " +
            "    JOIN mi.mezzi mezzoMissione " +
            "    WHERE mezzoMissione = me " +
            "    AND LOWER(TRIM(mi.richiesta.stato)) = 'in corso'" +
            ")";

        Long risultato = entityManager
            .createQuery(jpql, Long.class)
            .setParameter("targa", targa.trim())
            .getSingleResult();

        return risultato > 0;
    }
}

