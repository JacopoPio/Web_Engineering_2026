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
        this.entityManager.clear();

        String jpql = "SELECT o FROM Operatore o";

        TypedQuery<Operatore> query =
                this.entityManager.createQuery(jpql, Operatore.class);

        return query.getResultList();
    }

    @Override
    public Operatore findByEmail(String email) {
        this.entityManager.clear();

        return this.entityManager.find(Operatore.class, email);
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

            Operatore operatore =
                    this.entityManager.find(Operatore.class, emailOp);

            if (operatore != null) {
                this.entityManager.remove(operatore);
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
    public List<Operatore> findDisponibili() {
        return entityManager.createQuery(
                "SELECT o FROM Operatore o WHERE o.squadra IS NULL", Operatore.class)
            .getResultList();
        }

    @Override
    public boolean isCaposquadra(String email) { // Evita di caricare record inutili
        Long count = entityManager.createQuery(
                "SELECT COUNT(o) FROM Operatore o WHERE o.email = :email AND o.caposquadra = true", Long.class)
            .setParameter("email", email)
            .getSingleResult();
            return count > 0;
    }
}