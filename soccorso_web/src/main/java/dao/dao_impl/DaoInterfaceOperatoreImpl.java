package dao.dao_impl;

import dao.DaoInterfaceOperatore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
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
    @Override
public boolean isDisponibile(String email) {

    if (email == null || email.isBlank()) {
        return false;
    }

    String jpql =
            "SELECT COUNT(op) "
            + "FROM Operatore op "
            + "WHERE op.email = :email "
            + "AND op.attivo = true "
            + "AND op.squadra IS NULL";

    Long risultato = entityManager
            .createQuery(jpql, Long.class)
            .setParameter(
                    "email",
                    email.trim().toLowerCase()
            )
            .getSingleResult();

    return risultato > 0; 
    }
@Override
public boolean rimuoviDaSquadra(String email) {

    EntityTransaction tx =
            entityManager.getTransaction();

    try {
        tx.begin();

        Operatore operatore =
                entityManager.find(
                        Operatore.class,
                        email
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
         * Operatore è il lato proprietario della relazione.
         * Impostando squadra a null viene aggiornato
         * il campo id_squadra nel database.
         */
        operatore.setSquadra(null);

        entityManager.flush();

        tx.commit();

        return true;

    } catch (Exception e) {

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

        int operatoriAggiornati =
                entityManager.createQuery(
                        "UPDATE Operatore o "
                        + "SET o.squadra = null "
                        + "WHERE o.squadra.id = :idSquadra"
                )
                .setParameter(
                        "idSquadra",
                        idSquadra
                )
                .executeUpdate();

        /*
         * Una query UPDATE JPQL non aggiorna automaticamente
         * gli oggetti già presenti nella cache.
         */
        entityManager.clear();

        tx.commit();

        return operatoriAggiornati;

    } catch (Exception e) {

        if (tx.isActive()) {
            tx.rollback();
        }

        throw e;
    }
}

}