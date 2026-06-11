package dao.dao_impl;

import dao.DaoInterfaceAmministratore;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.Amministratore;
import org.mindrot.jbcrypt.BCrypt;

public class DaoInterfaceAmministratoreImpl implements DaoInterfaceAmministratore {

    private final EntityManager entityManager;

    public DaoInterfaceAmministratoreImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Amministratore save(Amministratore amministratore) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Amministratore salvato = this.entityManager.merge(amministratore);

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
    public List<Amministratore> findAll() {
       
        this.entityManager.clear();

        String jpql = "SELECT am FROM Amministratore am";

        TypedQuery<Amministratore> query =
                this.entityManager.createQuery(jpql, Amministratore.class);

        return query.getResultList();
    }

    @Override
    public Amministratore findByEmail(String email) {
        try {
            this.entityManager.clear();

            String jpql = "SELECT am FROM Amministratore am WHERE am.email = :email";

            TypedQuery<Amministratore> query =
                    this.entityManager.createQuery(jpql, Amministratore.class);

            query.setParameter("email", email);

            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Amministratore update(Amministratore amministratore) {
        return this.save(amministratore);
    }

    @Override
    public boolean delete(String emailAdmin) {
        EntityTransaction tx = this.entityManager.getTransaction();

        try {
            tx.begin();

            Amministratore amministratore =
                    this.entityManager.find(Amministratore.class, emailAdmin);

            if (amministratore != null) {
                this.entityManager.remove(amministratore);
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
    public boolean verificaLogin(String username, String passwordInChiaro) {
    // 1. Cerca l'utente tramite JPA
    Amministratore amministratore = entityManager.createQuery("SELECT u FROM Utente u WHERE u.username = :username", Amministratore.class)
                      .setParameter("username", username)
                      .getSingleResult();

    if (amministratore == null) {
        return false;
    }

    // 2. Verifica se la password inserita corrisponde all'hash sul DB
    // Nota: Il convertitore mantiene l'hash dentro l'oggetto utente.getPassword()
    return BCrypt.checkpw(passwordInChiaro, amministratore.getPassword());
}
}