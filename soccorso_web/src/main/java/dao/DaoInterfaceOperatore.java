package dao;

import java.util.List;
import model.Operatore;

public interface DaoInterfaceOperatore {

    public Operatore save(Operatore operatore);

    public List<Operatore> findAll();

    public Operatore findByEmail(String email);

    public Operatore update(Operatore operatore);

    public boolean delete(String emailOp);
    
    public List<Operatore> findDisponibili();
    
    public boolean isCaposquadra(String email);
}