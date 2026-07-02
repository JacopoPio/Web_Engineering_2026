package dao;

import java.util.List;
import model.Aggiornamento;
import model.Materiale;
import model.Mezzo;
import model.Missione;
import model.Operatore;
import model.Richiesta;

public interface DaoInterfaceMissione {

    Missione save(Missione missione);

    Missione creaMissioneCompleta(
            Richiesta richiesta,
            String nomeSquadra,
            String descrizione,
            String posizione,
            List<Operatore> operatori,
            List<Mezzo> mezzi,
            List<Materiale> materiali
    );

    List<Missione> findAll();

    Missione findById(int idMissione);

    List<Missione> findByOperatore(String emailOperatore);

    List<Missione> findByMezzo(String targaMezzo);

    List<Missione> findByMateriale(Long idMateriale);

    Missione update(Missione missione);

    boolean delete(int idMissione);

    boolean existsByRichiesta(Richiesta richiesta);

    Missione aggiungiAggiornamento(int idMissione, Aggiornamento aggiornamento);

    Missione chiudiMissione(int idMissione, int successo, String commentoFinale);
}
