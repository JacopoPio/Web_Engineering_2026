package dao;

import java.util.List;
import model.Richiesta;

public interface DaoInterfaceRichiesta {

    public Richiesta save(Richiesta richiesta);

    public List<Richiesta> findAll();

    public Richiesta findByEmail(String email_segnalante);

    public List<Richiesta> findByStato(String stato);

    public Richiesta update(Richiesta richiesta);

    public Richiesta updateStato(String email_segnalante, String nuovoStato);

    public boolean delete(String email_segnalante);
}