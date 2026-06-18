package dao.dao_impl;

import dao.DaoInterfacePatente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Patente;

public class DaoInterfaceImplPatente implements DaoInterfacePatente {

    private final EntityManager entityManager;

    public DaoInterfaceImplPatente(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Patente save(Patente patente) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Patente salvata = this.entityManager.merge(patente);

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
    public List<Patente> findAll() {
        //this.entityManager.clear();

        String jpql = "SELECT p FROM Patente p ORDER BY p.tipoPatente";

        TypedQuery<Patente> query =
                this.entityManager.createQuery(jpql, Patente.class);

        return query.getResultList();
    }

    @Override
    public Patente findByTipo(String tipoPatente) {
        if (tipoPatente == null || tipoPatente.isBlank()) {
            return null;
        }

        String tipoPulito = tipoPatente.trim().toUpperCase();

        return this.entityManager.find(Patente.class, tipoPulito);
    }

    @Override
    public Patente findOrCreate(String tipoPatente) {
        if (tipoPatente == null || tipoPatente.isBlank()) {
            return null;
        }

        String tipoPulito = tipoPatente.trim().toUpperCase();

        Patente esistente = this.entityManager.find(Patente.class, tipoPulito);

        if (esistente != null) {
            return esistente;
        }

        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Patente nuova = new Patente();
            nuova.setTipoPatente(tipoPulito);

            this.entityManager.persist(nuova);

            tx.commit();

            return nuova;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public Patente update(Patente patente) {
        return this.save(patente);
    }

    @Override
    public boolean delete(String tipoPatente) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            String tipoPulito = tipoPatente.trim().toUpperCase();

            Patente patente =
                    this.entityManager.find(Patente.class, tipoPulito);

            if (patente != null) {
                this.entityManager.remove(patente);
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