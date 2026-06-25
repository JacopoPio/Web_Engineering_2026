package dao.dao_impl;

import dao.DaoInterfaceMateriale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Materiale;

public class DaoInterfaceMaterialeImpl implements DaoInterfaceMateriale {

    private final EntityManager entityManager;

    public DaoInterfaceMaterialeImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Materiale save(Materiale materiale) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Materiale salvato = this.entityManager.merge(materiale);

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
    public List<Materiale> findAll() {
        this.entityManager.clear();

        String jpql = "SELECT m FROM Materiale m ORDER BY m.tipo";

        TypedQuery<Materiale> query =
                this.entityManager.createQuery(jpql, Materiale.class);

        return query.getResultList();
    }

    @Override
    public Materiale findByTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return null;
        }

        String jpql = "SELECT m FROM Materiale m WHERE m.tipo = :tipo";

        TypedQuery<Materiale> query =
                this.entityManager.createQuery(jpql, Materiale.class);

        query.setParameter("tipo", tipo.trim());

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Materiale update(Materiale materiale) {
        return this.save(materiale);
    }

    @Override
    public boolean delete(String tipoMateriale) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Materiale materiale = findByTipo(tipoMateriale);

            if (materiale != null) {
                this.entityManager.remove(materiale);
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
    public List<Materiale> findDisponibili() {
        return entityManager.createQuery(
                "SELECT m FROM Materiale m WHERE m.missioni IS EMPTY", Materiale.class)
            .getResultList();
        }
}