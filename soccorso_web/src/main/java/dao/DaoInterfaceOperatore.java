package dao;

import java.util.List;
import model.Operatore;

public interface DaoInterfaceOperatore {

    Operatore save(Operatore operatore);

    List<Operatore> findAll();

    Operatore findByEmail(String email);

    Operatore update(Operatore operatore);

    boolean delete(String emailOp);

    List<Operatore> findDisponibili();

    boolean isCaposquadra(String email);

    boolean isDisponibile(String email);

    boolean rimuoviDaSquadra(String email);

    int rimuoviTuttiDaSquadra(int idSquadra);
}