package dao.dao_impl;

import dao.DaoInterfaceMissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Missione;
import model.Richiesta;

public class DaoInterfaceMissioneImpl
        implements DaoInterfaceMissione {

    private final EntityManager entityManager;

    public DaoInterfaceMissioneImpl(
            EntityManager entityManager
    ) {

        if (entityManager == null) {
            throw new IllegalArgumentException(
                    "EntityManager non può essere null"
            );
        }

        this.entityManager = entityManager;
    }

    /*
     * Crea una nuova missione.
     */
    @Override
    public Missione save(Missione missione) {

        if (missione == null) {
            throw new IllegalArgumentException(
                    "La missione non può essere null"
            );
        }

        if (missione.getSquadra() == null) {
            throw new IllegalArgumentException(
                    "La missione deve essere associata a una squadra"
            );
        }

        if (missione.getSquadra().getId() == null) {
            throw new IllegalArgumentException(
                    "La squadra deve essere salvata prima della missione"
            );
        }

        if (missione.getRichiesta() == null) {
            throw new IllegalArgumentException(
                    "La missione deve essere associata a una richiesta"
            );
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            /*
             * La missione è una nuova entità.
             * persist() conserva la stessa istanza e,
             * dopo flush(), valorizza l'ID generato.
             */
            entityManager.persist(missione);

            /*
             * Esegue subito l'INSERT nel database.
             * In questo modo eventuali errori sui vincoli
             * vengono rilevati prima del commit.
             */
            entityManager.flush();

            tx.commit();

            return missione;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    /*
     * Restituisce tutte le missioni.
     *
     * Carica anche squadra e richiesta perché le relative
     * relazioni possono essere LAZY e l'EntityManager viene
     * normalmente chiuso prima del rendering del template.
     */
    @Override
    public List<Missione> findAll() {

        String jpql =
                "SELECT DISTINCT m "
                + "FROM Missione m "
                + "LEFT JOIN FETCH m.squadra "
                + "LEFT JOIN FETCH m.richiesta "
                + "ORDER BY m.id";

        TypedQuery<Missione> query =
                entityManager.createQuery(
                        jpql,
                        Missione.class
                );

        return query.getResultList();
    }

    /*
     * Aggiorna una missione già esistente.
     */
    @Override
    public Missione update(Missione missione) {

        if (missione == null) {
            throw new IllegalArgumentException(
                    "La missione non può essere null"
            );
        }

        if (missione.getSquadra() == null) {
            throw new IllegalArgumentException(
                    "La missione deve essere associata a una squadra"
            );
        }

        if (missione.getRichiesta() == null) {
            throw new IllegalArgumentException(
                    "La missione deve essere associata a una richiesta"
            );
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Missione missioneAggiornata =
                    entityManager.merge(missione);

            entityManager.flush();

            tx.commit();

            return missioneAggiornata;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    /*
     * Elimina una missione tramite il suo identificatore.
     */
    @Override
    public boolean delete(int idMissione) {

        if (idMissione <= 0) {
            return false;
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Missione missione =
                    entityManager.find(
                            Missione.class,
                            idMissione
                    );

            if (missione == null) {
                tx.commit();
                return false;
            }

            entityManager.remove(missione);

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

    /*
     * Verifica se esiste già una missione collegata
     * alla richiesta ricevuta.
     */
    @Override
    public boolean existsByRichiesta(
            Richiesta richiesta
    ) {

        if (richiesta == null) {
            return false;
        }

        String jpql =
                "SELECT COUNT(m) "
                + "FROM Missione m "
                + "WHERE m.richiesta = :richiesta";

        Long numeroMissioni =
                entityManager.createQuery(
                        jpql,
                        Long.class
                )
                .setParameter(
                        "richiesta",
                        richiesta
                )
                .getSingleResult();

        return numeroMissioni > 0;
    }
}