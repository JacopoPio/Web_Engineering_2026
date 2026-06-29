package dao.dao_impl;

import dao.DaoInterfaceSquadra;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Squadra;

public class DaoInterfaceSquadraImpl
        implements DaoInterfaceSquadra {

    private final EntityManager entityManager;

    public DaoInterfaceSquadraImpl(
            EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    /*
     * Usa questo metodo solamente per creare
     * una nuova squadra.
     */
    @Override
    public Squadra save(Squadra squadra) {

        if (squadra == null) {
            throw new IllegalArgumentException(
                    "La squadra non può essere null"
            );
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            /*
             * persist mantiene gestita la stessa istanza
             * ricevuta come parametro.
             */
            entityManager.persist(squadra);

            /*
             * Forza subito l'INSERT e la generazione
             * dell'identificatore.
             */
            entityManager.flush();

            tx.commit();

            return squadra;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public List<Squadra> findAll() {

        String jpql =
                "SELECT DISTINCT s "
                + "FROM Squadra s "
                + "ORDER BY s.id";

        TypedQuery<Squadra> query =
                entityManager.createQuery(
                        jpql,
                        Squadra.class
                );

        return query.getResultList();
    }

    /*
     * Usa merge solamente per una squadra
     * già esistente nel database.
     */
    @Override
    public Squadra update(Squadra squadra) {

        if (squadra == null) {
            throw new IllegalArgumentException(
                    "La squadra non può essere null"
            );
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Squadra aggiornata =
                    entityManager.merge(squadra);

            entityManager.flush();

            tx.commit();

            return aggiornata;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public boolean delete(int idSquadra) {

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Squadra squadra =
                    entityManager.find(
                            Squadra.class,
                            idSquadra
                    );

            if (squadra == null) {
                tx.commit();
                return false;
            }

            entityManager.remove(squadra);
            entityManager.flush();

            tx.commit();

            return true;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }
}