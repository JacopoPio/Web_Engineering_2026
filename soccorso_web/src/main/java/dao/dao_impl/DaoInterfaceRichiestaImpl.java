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
        /*
         * Utile se stai usando sempre lo stesso EntityManager.
         * Evita di vedere dati vecchi rimasti nella cache JPA.
         */
        this.entityManager.clear();

        String jpql = "SELECT r FROM Richiesta r";
        TypedQuery<Richiesta> query = this.entityManager.createQuery(jpql, Richiesta.class);
        return query.getResultList();
    }

    @Override
    public Richiesta findByEmail(String email_segnalante) {
        this.entityManager.clear();
        return this.entityManager.find(Richiesta.class, email_segnalante);
    }

    @Override
    public List<Richiesta> findByStato(String stato) {
        this.entityManager.clear();

        String jpql = "SELECT r FROM Richiesta r WHERE r.stato = :stato";
        TypedQuery<Richiesta> query = this.entityManager.createQuery(jpql, Richiesta.class);
        query.setParameter("stato", stato);

        return query.getResultList();
    }

    @Override
    public Richiesta update(Richiesta richiesta) {
        return this.save(richiesta);
    }

    @Override
    public Richiesta updateStato(String email_segnalante, String nuovoStato) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Richiesta richiesta = this.entityManager.find(Richiesta.class, email_segnalante);

            if (richiesta == null) {
                tx.commit();
                return null;
            }

            richiesta.setStato(nuovoStato);

            Richiesta aggiornata = this.entityManager.merge(richiesta);

            tx.commit();
            return aggiornata;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean delete(String email_segnalante) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Richiesta richiesta = this.entityManager.find(Richiesta.class, email_segnalante);

            if (richiesta != null) {
                this.entityManager.remove(richiesta);
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