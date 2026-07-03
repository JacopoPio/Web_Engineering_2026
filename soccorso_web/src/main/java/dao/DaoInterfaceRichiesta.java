package dao;

import java.time.LocalDateTime;
import java.util.List;
import model.Richiesta;

public interface DaoInterfaceRichiesta {

    Richiesta save(Richiesta richiesta);

    List<Richiesta> findAll();

    Richiesta findByEmail(String emailSegnalante);
 
    void archivia(String email);
    
    Richiesta findByToken(String token);

    List<Richiesta> findByStato(String stato);

    boolean existsIpRecente(byte[] ipOrigine, LocalDateTime limite);

    Richiesta update(Richiesta richiesta);

    Richiesta updateStato(String emailSegnalante, String nuovoStato);

    boolean delete(String emailSegnalante);
}
