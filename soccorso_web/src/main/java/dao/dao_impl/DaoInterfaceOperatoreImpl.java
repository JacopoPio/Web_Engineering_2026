package dao.dao_impl;

import dao.DaoInterfaceOperatore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Operatore;

public class DaoInterfaceOperatoreImpl
        implements DaoInterfaceOperatore {

    private final EntityManager entityManager;

    public DaoInterfaceOperatoreImpl(
            EntityManager entityManager
    ) {
        this.entityManager = entityManager;
    }

    @Override
    public Operatore save(Operatore operatore) {

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Operatore salvato =
                    entityManager.merge(operatore);

            tx.commit();

            return salvato;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public List<Operatore> findAll() {

        entityManager.clear();

        /*
         * LEFT JOIN FETCH permette al template FreeMarker
         * di controllare operatore.squadra anche dopo la
         * chiusura dell'EntityManager.
         */
        String jpql =
                "SELECT DISTINCT o "
                + "FROM Operatore o "
                + "LEFT JOIN FETCH o.squadra "
                + "ORDER BY o.cognome, o.nome";

        TypedQuery<Operatore> query =
                entityManager.createQuery(
                        jpql,
                        Operatore.class
                );

        return query.getResultList();
    }

    @Override
    public Operatore findByEmail(String email) {

        if (email == null || email.isBlank()) {
            return null;
        }

        entityManager.clear();

        return entityManager.find(
                Operatore.class,
                email.trim()
        );
    }

    @Override
    public Operatore update(Operatore operatore) {
        return save(operatore);
    }

    @Override
    public boolean delete(String emailOp) {

        if (emailOp == null || emailOp.isBlank()) {
            return false;
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Operatore operatore =
                    entityManager.find(
                            Operatore.class,
                            emailOp.trim()
                    );

            if (operatore == null) {
                tx.commit();
                return false;
            }

            /*
             * Rimuove prima i collegamenti con le
             * tabelle associative.
             */
            operatore.setSquadra(null);
            operatore.setCaposquadra(false);

            operatore.getPatenti().clear();
            operatore.getAbilita().clear();

            entityManager.flush();

            entityManager.remove(operatore);

            tx.commit();

            return true;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }

    @Override
    public List<Operatore> findDisponibili() {

        String jpql =
                "SELECT o "
                + "FROM Operatore o "
                + "WHERE o.squadra IS NULL "
                + "AND o.attivo = true "
                + "ORDER BY o.cognome, o.nome";

        return entityManager
                .createQuery(jpql, Operatore.class)
                .getResultList();
    }

    @Override
    public boolean isCaposquadra(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String jpql =
                "SELECT COUNT(o) "
                + "FROM Operatore o "
                + "WHERE o.email = :email "
                + "AND o.caposquadra = true";

        Long risultato = entityManager
                .createQuery(jpql, Long.class)
                .setParameter("email", email.trim())
                .getSingleResult();

        return risultato > 0;
    }

    @Override
    public boolean isDisponibile(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String jpql =
                "SELECT COUNT(o) "
                + "FROM Operatore o "
                + "WHERE o.email = :email "
                + "AND o.attivo = true "
                + "AND o.squadra IS NULL";

        Long risultato = entityManager
                .createQuery(jpql, Long.class)
                .setParameter("email", email.trim())
                .getSingleResult();

        return risultato > 0;
    }

    @Override
    public boolean rimuoviDaSquadra(String email) {

        if (email == null || email.isBlank()) {
            return false;
        }

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            Operatore operatore =
                    entityManager.find(
                            Operatore.class,
                            email.trim()
                    );

            if (operatore == null) {
                tx.commit();
                return false;
            }

            if (operatore.getSquadra() == null) {
                tx.commit();
                return false;
            }

            /*
             * Operatore è il lato proprietario
             * della relazione ManyToOne.
             *
             * Questo aggiorna id_squadra a NULL.
             */
            operatore.setSquadra(null);

            /*
             * Un operatore senza squadra non può
             * continuare a essere caposquadra.
             */
            operatore.setCaposquadra(false);

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

    @Override
    public int rimuoviTuttiDaSquadra(int idSquadra) {

        EntityTransaction tx =
                entityManager.getTransaction();

        try {
            tx.begin();

            String jpql =
                    "UPDATE Operatore o "
                    + "SET o.squadra = null, "
                    + "o.caposquadra = false "
                    + "WHERE o.squadra.id = :idSquadra";

            int operatoriAggiornati =
                    entityManager
                            .createQuery(jpql)
                            .setParameter(
                                    "idSquadra",
                                    idSquadra
                            )
                            .executeUpdate();

            tx.commit();

            /*
             * Le query UPDATE JPQL non aggiornano
             * automaticamente gli oggetti nella cache.
             */
            entityManager.clear();

            return operatoriAggiornati;

        } catch (RuntimeException e) {

            if (tx.isActive()) {
                tx.rollback();
            }

            throw e;
        }
    }
    @Override
    public List<Operatore> findBySquadra(Integer idSquadra) {

    if (idSquadra == null || idSquadra <= 0) {
        return List.of();
    }

    String jpql =
            "SELECT o "
            + "FROM Operatore o "
            + "WHERE o.squadra.id = :idSquadra "
            + "ORDER BY o.cognome, o.nome";

    return entityManager
            .createQuery(jpql, Operatore.class)
            .setParameter("idSquadra", idSquadra)
            .getResultList(); 
    }
}