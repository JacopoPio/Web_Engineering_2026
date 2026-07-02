package dao.dao_impl;

import dao.DaoInterfaceMissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Aggiornamento;
import model.Materiale;
import model.Mezzo;
import model.Missione;
import model.Operatore;
import model.Richiesta;
import model.Squadra;
import model.StatoRichiesta;

public class DaoInterfaceMissioneImpl implements DaoInterfaceMissione {

    private final EntityManager entityManager;

    public DaoInterfaceMissioneImpl(EntityManager entityManager) {
        if (entityManager == null) {
            throw new IllegalArgumentException("EntityManager non può essere null");
        }
        this.entityManager = entityManager;
    }

    @Override
    public Missione save(Missione missione) {
        if (missione == null || missione.getSquadra() == null || missione.getRichiesta() == null) {
            throw new IllegalArgumentException("Missione, squadra e richiesta sono obbligatorie");
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            entityManager.persist(missione);
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

    @Override
    public Missione creaMissioneCompleta(
            Richiesta richiesta,
            String nomeSquadra,
            String descrizione,
            String posizione,
            List<Operatore> operatori,
            List<Mezzo> mezzi,
            List<Materiale> materiali) {

        if (richiesta == null
                || richiesta.getEmail_segnalante() == null
                || richiesta.getEmail_segnalante().isBlank()) {
            throw new IllegalArgumentException("Richiesta non valida");
        }
        if (operatori == null || operatori.isEmpty()) {
            throw new IllegalArgumentException("La missione deve avere almeno un operatore");
        }
        if (descrizione == null || descrizione.isBlank()
                || posizione == null || posizione.isBlank()) {
            throw new IllegalArgumentException("Obiettivo e posizione sono obbligatori");
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            String emailRichiesta = richiesta.getEmail_segnalante().trim().toLowerCase();
            Richiesta richiestaGestita = entityManager.find(
                    Richiesta.class,
                    emailRichiesta,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (richiestaGestita == null
                    || !StatoRichiesta.ATTIVA.equalsIgnoreCase(richiestaGestita.getStato())) {
                throw new IllegalStateException("La richiesta non è più disponibile");
            }

            Long missioniEsistenti = entityManager.createQuery(
                    "SELECT COUNT(m) FROM Missione m "
                    + "WHERE LOWER(m.richiesta.email_segnalante) = :email",
                    Long.class
            ).setParameter("email", emailRichiesta)
             .getSingleResult();

            if (missioniEsistenti > 0) {
                throw new IllegalStateException("Esiste già una missione per questa richiesta");
            }

            List<Operatore> operatoriGestiti = new ArrayList<>();
            for (Operatore operatore : operatori) {
                if (operatore == null || operatore.getEmail() == null) {
                    throw new IllegalStateException("Operatore non valido");
                }
                Operatore gestito = entityManager.find(
                        Operatore.class,
                        operatore.getEmail().trim().toLowerCase(),
                        LockModeType.PESSIMISTIC_WRITE
                );
                if (gestito == null || !gestito.isAttivo() || gestito.getSquadra() != null) {
                    throw new IllegalStateException(
                            "Operatore non disponibile: " + operatore.getEmail()
                    );
                }
                operatoriGestiti.add(gestito);
            }

            List<Mezzo> mezziGestiti = new ArrayList<>();
            if (mezzi != null) {
                for (Mezzo mezzo : mezzi) {
                    Mezzo gestito = entityManager.find(
                            Mezzo.class,
                            mezzo.getTarga().trim().toUpperCase(),
                            LockModeType.PESSIMISTIC_WRITE
                    );
                    if (gestito == null || mezzoOccupato(gestito.getTarga())) {
                        throw new IllegalStateException(
                                "Mezzo non disponibile: " + mezzo.getTarga()
                        );
                    }
                    mezziGestiti.add(gestito);
                }
            }

            List<Materiale> materialiGestiti = new ArrayList<>();
            if (materiali != null) {
                for (Materiale materiale : materiali) {
                    Materiale gestito = entityManager.find(
                            Materiale.class,
                            materiale.getId(),
                            LockModeType.PESSIMISTIC_WRITE
                    );
                    if (gestito == null || materialeOccupato(gestito.getId())) {
                        throw new IllegalStateException(
                                "Materiale non disponibile: " + materiale.getId()
                        );
                    }
                    materialiGestiti.add(gestito);
                }
            }

            Squadra squadra = new Squadra();
            squadra.setNome(nomeSquadra == null || nomeSquadra.isBlank()
                    ? "Squadra missione"
                    : nomeSquadra.trim());
            squadra.setOperatori(new ArrayList<>());
            entityManager.persist(squadra);

            for (Operatore operatore : operatoriGestiti) {
                operatore.setSquadra(squadra);
                squadra.getOperatori().add(operatore);
            }

            Missione missione = new Missione();
            missione.setDescrizione(descrizione.trim());
            missione.setPosizione(posizione.trim());
            missione.setDataInizio(LocalDateTime.now());
            missione.setRichiesta(richiestaGestita);
            missione.setSquadra(squadra);
            missione.setOperatori(new ArrayList<>(operatoriGestiti));
            missione.setMezzi(new ArrayList<>(mezziGestiti));
            missione.setMateriali(new ArrayList<>(materialiGestiti));
            missione.setAggiornamenti(new ArrayList<>());

            entityManager.persist(missione);
            squadra.setMissione(missione);
            richiestaGestita.setStato(StatoRichiesta.IN_CORSO);

            entityManager.flush();
            tx.commit();

            inizializzaCollezioni(missione);
            return missione;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Missione> findAll() {
        List<Missione> missioni = entityManager.createQuery(
                "SELECT DISTINCT m FROM Missione m "
                + "LEFT JOIN FETCH m.squadra "
                + "LEFT JOIN FETCH m.richiesta "
                + "ORDER BY m.id DESC",
                Missione.class
        ).getResultList();

        missioni.forEach(this::inizializzaCollezioni);
        return missioni;
    }

    @Override
    public Missione findById(int idMissione) {
        if (idMissione <= 0) {
            return null;
        }
        try {
            Missione missione = entityManager.createQuery(
                    "SELECT DISTINCT m FROM Missione m "
                    + "LEFT JOIN FETCH m.squadra "
                    + "LEFT JOIN FETCH m.richiesta "
                    + "WHERE m.id = :id",
                    Missione.class
            ).setParameter("id", idMissione)
             .getSingleResult();
            inizializzaCollezioni(missione);
            return missione;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Missione> findByOperatore(String emailOperatore) {
        if (emailOperatore == null || emailOperatore.isBlank()) {
            return List.of();
        }
        List<Missione> missioni = entityManager.createQuery(
                "SELECT DISTINCT m FROM Missione m "
                + "JOIN m.operatori o "
                + "LEFT JOIN FETCH m.squadra "
                + "LEFT JOIN FETCH m.richiesta "
                + "WHERE LOWER(o.email) = :email "
                + "ORDER BY m.id DESC",
                Missione.class
        ).setParameter("email", emailOperatore.trim().toLowerCase())
         .getResultList();
        missioni.forEach(this::inizializzaCollezioni);
        return missioni;
    }

    @Override
    public List<Missione> findByMezzo(String targaMezzo) {
        if (targaMezzo == null || targaMezzo.isBlank()) {
            return List.of();
        }
        List<Missione> missioni = entityManager.createQuery(
                "SELECT DISTINCT m FROM Missione m JOIN m.mezzi me "
                + "LEFT JOIN FETCH m.squadra LEFT JOIN FETCH m.richiesta "
                + "WHERE UPPER(me.targa) = :targa ORDER BY m.id DESC",
                Missione.class
        ).setParameter("targa", targaMezzo.trim().toUpperCase())
         .getResultList();
        missioni.forEach(this::inizializzaCollezioni);
        return missioni;
    }

    @Override
    public List<Missione> findByMateriale(Long idMateriale) {
        Integer id = convertiIdMateriale(idMateriale);
        if (id == null) {
            return List.of();
        }
        List<Missione> missioni = entityManager.createQuery(
                "SELECT DISTINCT m FROM Missione m JOIN m.materiali ma "
                + "LEFT JOIN FETCH m.squadra LEFT JOIN FETCH m.richiesta "
                + "WHERE ma.id = :id ORDER BY m.id DESC",
                Missione.class
        ).setParameter("id", id)
         .getResultList();
        missioni.forEach(this::inizializzaCollezioni);
        return missioni;
    }

    @Override
    public Missione update(Missione missione) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Missione aggiornata = entityManager.merge(missione);
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
    public boolean delete(int idMissione) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Missione missione = entityManager.find(Missione.class, idMissione);
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

    @Override
    public boolean existsByRichiesta(Richiesta richiesta) {
        if (richiesta == null
                || richiesta.getEmail_segnalante() == null
                || richiesta.getEmail_segnalante().isBlank()) {
            return false;
        }
        Long count = entityManager.createQuery(
                "SELECT COUNT(m) FROM Missione m "
                + "WHERE LOWER(m.richiesta.email_segnalante) = :email",
                Long.class
        ).setParameter("email", richiesta.getEmail_segnalante().trim().toLowerCase())
         .getSingleResult();
        return count > 0;
    }

    @Override
    public Missione aggiungiAggiornamento(int idMissione, Aggiornamento aggiornamento) {
        if (aggiornamento == null) {
            throw new IllegalArgumentException("Aggiornamento non valido");
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Missione missione = entityManager.find(
                    Missione.class,
                    idMissione,
                    LockModeType.PESSIMISTIC_WRITE
            );
            if (missione == null) {
                tx.commit();
                return null;
            }
            if (!StatoRichiesta.IN_CORSO.equalsIgnoreCase(
                    missione.getRichiesta().getStato())) {
                throw new IllegalStateException(
                        "Gli aggiornamenti sono consentiti solo sulle missioni in corso"
                );
            }

            entityManager.persist(aggiornamento);
            missione.getAggiornamenti().add(aggiornamento);
            entityManager.flush();
            tx.commit();

            inizializzaCollezioni(missione);
            return missione;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Missione chiudiMissione(int idMissione, int successo, String commentoFinale) {
        if (successo < 0 || successo > 5) {
            throw new IllegalArgumentException("Il successo deve essere compreso tra 0 e 5");
        }

        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            Missione missione = entityManager.find(
                    Missione.class,
                    idMissione,
                    LockModeType.PESSIMISTIC_WRITE
            );
            if (missione == null) {
                tx.commit();
                return null;
            }
            if (!StatoRichiesta.IN_CORSO.equalsIgnoreCase(
                    missione.getRichiesta().getStato())) {
                throw new IllegalStateException("La missione non è in corso");
            }

            missione.setSuccesso(successo);
            missione.setCommentoFinale(
                    commentoFinale == null || commentoFinale.isBlank()
                            ? null
                            : commentoFinale.trim()
            );
            missione.setDataFine(LocalDateTime.now());
            missione.getRichiesta().setStato(StatoRichiesta.CHIUSA);

            /*
             * Si libera l'assegnazione corrente degli operatori,
             * ma si conserva missione.operatori per lo storico.
             * Mezzi e materiali tornano disponibili grazie alle query
             * che considerano solo missioni in corso.
             */
            if (missione.getSquadra() != null
                    && missione.getSquadra().getOperatori() != null) {
                List<Operatore> componenti =
                        new ArrayList<>(missione.getSquadra().getOperatori());
                for (Operatore operatore : componenti) {
                    operatore.setSquadra(null);
                }
                missione.getSquadra().getOperatori().clear();
            }

            entityManager.flush();
            tx.commit();

            inizializzaCollezioni(missione);
            return missione;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    private boolean mezzoOccupato(String targa) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(mi) FROM Missione mi JOIN mi.mezzi me "
                + "WHERE me.targa = :targa "
                + "AND LOWER(TRIM(mi.richiesta.stato)) = :stato",
                Long.class
        ).setParameter("targa", targa)
         .setParameter("stato", StatoRichiesta.IN_CORSO)
         .getSingleResult();
        return count > 0;
    }

    private boolean materialeOccupato(int id) {
        Long count = entityManager.createQuery(
                "SELECT COUNT(mi) FROM Missione mi JOIN mi.materiali ma "
                + "WHERE ma.id = :id "
                + "AND LOWER(TRIM(mi.richiesta.stato)) = :stato",
                Long.class
        ).setParameter("id", id)
         .setParameter("stato", StatoRichiesta.IN_CORSO)
         .getSingleResult();
        return count > 0;
    }

    private Integer convertiIdMateriale(Long id) {
        if (id == null || id <= 0 || id > Integer.MAX_VALUE) {
            return null;
        }
        return id.intValue();
    }

    private void inizializzaCollezioni(Missione missione) {
        if (missione == null) {
            return;
        }
        missione.getOperatori().size();
        missione.getMezzi().size();
        missione.getMateriali().size();
        missione.getAggiornamenti().size();
        if (missione.getSquadra() != null
                && missione.getSquadra().getOperatori() != null) {
            missione.getSquadra().getOperatori().size();
        }
    }
}
