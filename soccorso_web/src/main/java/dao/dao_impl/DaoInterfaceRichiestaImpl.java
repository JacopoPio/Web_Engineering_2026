package dao.dao_impl;

import dao.DaoInterfaceRichiesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.List;
import model.Richiesta;

public class DaoInterfaceRichiestaImpl implements DaoInterfaceRichiesta {

    private final EntityManager entityManager;

    public DaoInterfaceRichiestaImpl(EntityManager entityManager) {
        if (entityManager == null) {
            throw new IllegalArgumentException("EntityManager non può essere null");
        }
        this.entityManager = entityManager;
    }

    @Override
    public Richiesta save(Richiesta richiesta) {
        if (richiesta == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        String email = normalizzaEmail(richiesta.getEmail_segnalante());
        if (email.isBlank()) {
            throw new IllegalArgumentException("L'email del segnalante è obbligatoria");
        }
        richiesta.setEmail_segnalante(email);

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            /*
             * Con email_segnalante come PK usiamo persist():
             * una seconda richiesta con la stessa email non deve
             * sovrascrivere quella già presente.
             */
            entityManager.persist(richiesta);
            entityManager.flush();

            tx.commit();
            return richiesta;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Richiesta> findAll() {
        entityManager.clear();
        return entityManager.createQuery(
                "SELECT r FROM Richiesta r "
                + "ORDER BY r.dataCreazione DESC, r.email_segnalante",
                Richiesta.class
        ).getResultList();
    }

    @Override
    public Richiesta findByEmail(String emailSegnalante) {
        String email = normalizzaEmail(emailSegnalante);
        if (email.isBlank()) {
            return null;
        }
        entityManager.clear();
        return entityManager.find(Richiesta.class, email);
    }

    @Override
    public Richiesta findByToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return entityManager.createQuery(
                    "SELECT r FROM Richiesta r WHERE r.tokenConferma = :token",
                    Richiesta.class
            ).setParameter("token", token.trim())
             .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Richiesta> findByStato(String stato) {
        if (stato == null || stato.isBlank()) {
            return List.of();
        }
        return entityManager.createQuery(
                "SELECT r FROM Richiesta r "
                + "WHERE LOWER(TRIM(r.stato)) = LOWER(TRIM(:stato)) "
                + "ORDER BY r.dataCreazione DESC, r.email_segnalante",
                Richiesta.class
        ).setParameter("stato", stato.trim())
         .getResultList();
    }

    @Override
    public boolean existsIpRecente(byte[] ipOrigine, LocalDateTime limite) {
        if (ipOrigine == null || limite == null) {
            return false;
        }
        Long count = entityManager.createQuery(
                "SELECT COUNT(r) FROM Richiesta r "
                + "WHERE r.ip_origine = :ip "
                + "AND r.dataCreazione >= :limite",
                Long.class
        ).setParameter("ip", ipOrigine)
         .setParameter("limite", limite)
         .getSingleResult();
        return count > 0;
    }

    @Override
    public Richiesta update(Richiesta richiesta) {
        if (richiesta == null) {
            throw new IllegalArgumentException("La richiesta non può essere null");
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Richiesta aggiornata = entityManager.merge(richiesta);
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
    public Richiesta updateStato(String emailSegnalante, String nuovoStato) {
        String email = normalizzaEmail(emailSegnalante);
        if (email.isBlank() || nuovoStato == null || nuovoStato.isBlank()) {
            return null;
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Richiesta richiesta = entityManager.find(Richiesta.class, email);
            if (richiesta == null) {
                tx.commit();
                return null;
            }
            richiesta.setStato(nuovoStato.trim().toLowerCase());
            entityManager.flush();
            tx.commit();
            return richiesta;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean delete(String emailSegnalante) {
        String email = normalizzaEmail(emailSegnalante);
        if (email.isBlank()) {
            return false;
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Richiesta richiesta = entityManager.find(Richiesta.class, email);
            if (richiesta == null) {
                tx.commit();
                return false;
            }

            Long missioni = entityManager.createQuery(
                    "SELECT COUNT(m) FROM Missione m "
                    + "WHERE m.richiesta.email_segnalante = :email",
                    Long.class
            ).setParameter("email", email)
             .getSingleResult();

            if (missioni > 0) {
                tx.commit();
                return false;
            }

            entityManager.remove(richiesta);
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

    private String normalizzaEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
